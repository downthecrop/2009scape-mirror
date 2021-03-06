package rs09.net.packet.`in`

import core.cache.crypto.ISAACCipher
import core.cache.crypto.ISAACPair
import core.cache.misc.buffer.ByteBufferUtils
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.PlayerDetails
import core.game.node.entity.player.info.UIDInfo
import core.game.node.entity.player.info.login.LoginType
import core.net.Constants
import core.net.IoSession
import core.tools.StringUtils
import proto.management.JoinClanRequest
import proto.management.PlayerStatusUpdate
import proto.management.RequestContactInfo
import rs09.ServerConstants
import rs09.auth.AuthResponse
import rs09.game.node.entity.player.info.login.LoginParser
import rs09.game.system.SystemLogger
import rs09.game.world.GameWorld
import rs09.game.world.repository.Repository
import rs09.worker.ManagementEvents
import rs09.worker.ManagementEvents.publish
import java.math.BigInteger
import java.nio.BufferUnderflowException
import java.nio.ByteBuffer

object Login {
    private const val ENCRYPTION_VERIFICATION_BYTE: Int = 10
    private const val NORMAL_LOGIN_OP = 16
    private const val RECONNECT_LOGIN_OP = 18
    const val CACHE_INDEX_COUNT = 29

    fun decodeFromBuffer(buffer: ByteBuffer) : Pair<AuthResponse, LoginInfo?> {
        try {
            val info = LoginInfo.createDefault()

            info.opcode = buffer.get().toInt()
            if (buffer.short.toInt() != buffer.remaining()) {
                return Pair(AuthResponse.BadSessionID, null)
            }
            val revision = buffer.int
            if (revision != Constants.REVISION) {
                return Pair(AuthResponse.Updated, null)
            }
            if (info.opcode != NORMAL_LOGIN_OP && info.opcode != RECONNECT_LOGIN_OP) {
                SystemLogger.logInfo("Invalid Login Opcode: ${info.opcode}")
                return Pair(AuthResponse.InvalidLoginServer, null)
            }

            noop(buffer)
            info.showAds = buffer.get().toInt() == 1
            noop(buffer)
            info.windowMode = buffer.get().toInt()
            info.screenWidth = buffer.short.toInt()
            info.screenHeight = buffer.short.toInt()
            info.displayMode = buffer.get().toInt()
            noop(buffer, 24) //Skip past a bunch of random (actually random) data the client sends
            ByteBufferUtils.getString(buffer) //same as above
            info.adAffiliateId = buffer.int
            info.settingsHash = buffer.int
            info.currentPacketCount = buffer.short.toInt()

            //Read client-reported CRC sums
            for (i in 0 until CACHE_INDEX_COUNT) info.crcSums[i] = buffer.int

            val decryptedBuffer = decryptRSABuffer(buffer, ServerConstants.EXPONENT, ServerConstants.MODULUS)
            decryptedBuffer.rewind()

            if (decryptedBuffer.get().toInt() != ENCRYPTION_VERIFICATION_BYTE) {
                return Pair(AuthResponse.UnexpectedError, info)
            }

            info.isaacPair = produceISAACPairFrom(decryptedBuffer)
            info.username = StringUtils.longToString(decryptedBuffer.long)
            info.password = ByteBufferUtils.getString(decryptedBuffer)

            if (Repository.getPlayerByName(info.username) != null) {
                return Pair(AuthResponse.AlreadyOnline, info)
            }

            return Pair(AuthResponse.Success, info)
        } catch (e: Exception) {
            SystemLogger.logErr("Exception encountered during login packet parsing! See stack trace below.")
            e.printStackTrace()
            return Pair(AuthResponse.UnexpectedError, null)
        }
    }

    private fun produceISAACPairFrom(buffer: ByteBuffer): ISAACPair {
        val incomingSeed = IntArray(4)
        for(i in incomingSeed.indices) {
            incomingSeed[i] = buffer.int
        }
        val inCipher = ISAACCipher(incomingSeed)
        for(i in incomingSeed.indices) {
            incomingSeed[i] += 50
        }
        val outCipher = ISAACCipher(incomingSeed)
        return ISAACPair(inCipher, outCipher)
    }

    @JvmStatic fun decryptRSABuffer(buffer: ByteBuffer, exponent: BigInteger, modulus: BigInteger): ByteBuffer {
        return try {
            val numBytes = buffer.get().toInt() and 0xFF
            val encryptedBytes = ByteArray(numBytes)
            buffer[encryptedBytes]

            val encryptedBigInt = BigInteger(encryptedBytes)
            ByteBuffer.wrap(encryptedBigInt.modPow(exponent, modulus).toByteArray())
        } catch (e: BufferUnderflowException) {
            ByteBuffer.wrap(byteArrayOf(-1))
        }
    }

    private fun noop(buffer: ByteBuffer, amount: Int = 1) {buffer[ByteArray(amount)]}

    fun proceedWith(session: IoSession, details: PlayerDetails, opcode: Int) {
        if (!Repository.LOGGED_IN_PLAYERS.contains(details.username))
            Repository.LOGGED_IN_PLAYERS.add(details.username)
        details.session = session
        details.info.translate(UIDInfo(details.ipAddress, "DEPRECATED", "DEPRECATED", "DEPRECATED"))
        val player = Player(details)
        if (Repository.getPlayerByName(player.name) == null) {
            Repository.addPlayer(player)
        }
        session.lastPing = System.currentTimeMillis()
        try {
            LoginParser(details, LoginType.fromType(opcode)).initialize(player, opcode == RECONNECT_LOGIN_OP)
            sendMSEvents(details)
        } catch (e: Exception) {
            e.printStackTrace()
            session.disconnect()
            Repository.removePlayer(player)
            player.clear(true)
        }
    }

    private fun sendMSEvents(details: PlayerDetails) {
        val statusEvent = PlayerStatusUpdate.newBuilder()
        statusEvent.username = details.username
        statusEvent.world = GameWorld.settings!!.worldId
        statusEvent.notifyFriendsOnly = false
        publish(statusEvent.build())

        val contactEvent = RequestContactInfo.newBuilder()
        contactEvent.username = details.username
        contactEvent.world = GameWorld.settings!!.worldId
        publish(contactEvent.build())

        if (!details.communication.currentClan.isNullOrEmpty() && details.communication.clan == null) {
            val clanEvent = JoinClanRequest.newBuilder()
            clanEvent.username = details.username
            clanEvent.clanName = details.communication.currentClan
            publish(clanEvent.build())
        }
    }
}
