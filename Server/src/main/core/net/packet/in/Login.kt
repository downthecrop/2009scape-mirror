package core.net.packet.`in`

import com.moandjiezana.toml.Toml
import core.cache.crypto.ISAACCipher
import core.cache.crypto.ISAACPair
import core.cache.misc.buffer.ByteBufferUtils
import core.game.node.entity.player.Player
import core.net.Constants
import core.net.IoSession
import core.tools.StringUtils
import core.integrations.discord.Discord
import proto.management.JoinClanRequest
import proto.management.PlayerStatusUpdate
import proto.management.RequestContactInfo
import core.ServerConstants
import core.ServerStore
import core.ServerStore.Companion.addToList
import core.ServerStore.Companion.getList
import core.api.log
import core.auth.AuthResponse
import core.game.node.entity.player.info.*
import core.game.node.entity.player.info.login.LoginParser
import core.tools.SystemLogger
import core.game.world.GameWorld
import core.game.world.repository.Repository
import core.tools.Log
import core.worker.ManagementEvents.publish
import java.io.File
import java.io.IOException
import java.math.BigInteger
import java.nio.BufferUnderflowException
import java.nio.ByteBuffer

object Login {
    private const val ENCRYPTION_VERIFICATION_BYTE: Int = 10
    private const val NORMAL_LOGIN_OP = 16
    private const val RECONNECT_LOGIN_OP = 18
    const val CACHE_INDEX_COUNT = 29

    private var exceptionData: Toml? = null
    private var lastModifiedData = 0L

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
                log(this::class.java, Log.WARN, "Invalid Login Opcode: ${info.opcode}")
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
        } catch (buf: BufferUnderflowException) {
            //some issue in either the data they sent us or how we read it, either way out of scope of this class's handling.
            return Pair(AuthResponse.UnexpectedError, null)
        } catch (e: Exception) {
            log(this::class.java, Log.ERR,  "Exception encountered during login packet parsing! See stack trace below.")
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
            buffer.get(encryptedBytes)

            val encryptedBigInt = BigInteger(encryptedBytes)
            ByteBuffer.wrap(encryptedBigInt.modPow(exponent, modulus).toByteArray())
        } catch (e: BufferUnderflowException) {
            ByteBuffer.wrap(byteArrayOf(-1))
        }
    }

    private fun noop(buffer: ByteBuffer, amount: Int = 1) {buffer.get(ByteArray(amount))}

    fun proceedWith(session: IoSession, details: PlayerDetails, opcode: Int) {
        if (Repository.uid_map.contains(details.uid)) {
            session.write(AuthResponse.AlreadyOnline)
            return
        }
        details.session = session
        details.info.translate(UIDInfo(details.ipAddress, "DEPRECATED", "DEPRECATED", "DEPRECATED"))

        val archive = ServerStore.getArchive("flagged-ips")
        val flaggedIps = archive.getList<String>("ips")
        if (flaggedIps.contains(details.ipAddress)) {
            Discord.postPlayerAlert(details.username, "Login from flagged IP ${details.ipAddress}")
        }

        val player = Player(details)
        PlayerMonitor.log(player, LogType.IP_LOG, details.ipAddress)
        if (canBypassAccountLimitCheck(player)) {
            proceedWithAcceptableLogin(session, player, opcode)
        } else {
            if (checkAccountLimit(details.ipAddress, details.username)) {
                proceedWithAcceptableLogin(session, player, opcode)
            } else {
                session.write(AuthResponse.LoginLimitExceeded)
            }
        }
    }

    private fun canBypassAccountLimitCheck(player: Player): Boolean {
        return player.rights == Rights.ADMINISTRATOR || player.rights == Rights.PLAYER_MODERATOR
    }

    private fun proceedWithAcceptableLogin(session: IoSession, player: Player, opcode: Int) {
        Repository.addPlayer(player)
        session.lastPing = System.currentTimeMillis()
        try {
            LoginParser(player.details).initialize(player, opcode == RECONNECT_LOGIN_OP)
            sendMSEvents(player.details)
        } catch (e: Exception) {
            e.printStackTrace()
            session.disconnect()
        }
    }

    private fun checkAccountLimit(ipAddress: String, username: String): Boolean {
        var accountLimit = ServerConstants.DAILY_ACCOUNT_LIMIT

        if (File(ServerConstants.CONFIG_PATH + "account_limit_exceptions.conf").exists()) {
            try {
                val f = File(ServerConstants.CONFIG_PATH + "account_limit_exceptions.conf")
                if (f.lastModified() != lastModifiedData) {
                    exceptionData = Toml().read(f)
                    lastModifiedData = f.lastModified()
                }

                if (exceptionData?.contains("exceptions.\"${ipAddress}\"") == true) {
                    accountLimit = exceptionData?.getLong("exceptions.\"${ipAddress}\"", 0L)?.toInt() ?: accountLimit
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val archive = ServerStore.getArchive("daily-accounts")
        val accounts = archive.getList<String>(ipAddress)
        if (username in accounts) return true

        if (accounts.size >= accountLimit)
            return false

        archive.addToList(ipAddress, username)
        return true
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
