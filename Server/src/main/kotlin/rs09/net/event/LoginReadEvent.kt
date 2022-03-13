package rs09.net.event

import core.cache.Cache
import core.cache.crypto.ISAACCipher
import core.cache.crypto.ISAACPair
import core.cache.misc.buffer.ByteBufferUtils
import core.game.node.entity.player.info.ClientInfo
import core.game.node.entity.player.info.PlayerDetails
import core.game.node.entity.player.info.UIDInfo
import core.game.node.entity.player.info.login.LoginType
import core.game.node.entity.player.info.login.Response
import core.game.node.entity.player.info.portal.PlayerSQLManager
import core.game.system.task.TaskExecutor
import core.net.Constants
import core.net.IoReadEvent
import core.net.IoSession
import core.net.amsc.WorldCommunicator
import core.tools.StringUtils
import rs09.ServerConstants
import rs09.game.node.entity.player.info.login.LoginParser
import rs09.game.system.SystemLogger
import rs09.game.world.repository.Repository
import java.lang.Runnable
import java.math.BigInteger
import java.nio.ByteBuffer
import java.util.*

/**
 * Handles login reading events.
 * @author Emperor
 */
class LoginReadEvent
/**
 * Constructs a new `LoginReadEvent`.
 * @param session The session.
 * @param buffer The buffer with data to read from.
 */
(session: IoSession?, buffer: ByteBuffer?) : IoReadEvent(session, buffer) {
    override fun read(session: IoSession, buffer: ByteBuffer) {
        SystemLogger.logInfo("login read")
        val opcode: Int = buffer.get().toInt()
        if (buffer.short.toInt() != buffer.remaining()) {
            session.write(Response.BAD_SESSION_ID)
            return
        }
        val build = buffer.int
        if (build != Constants.REVISION) { // || buffer.getInt() != Constants.CLIENT_BUILD) {
            session.write(Response.UPDATED)
            return
        }
        when (opcode) {
            12 -> println("User details event detected")
            16, 18 -> decodeWorld(opcode, session, buffer)
            else -> {
                SystemLogger.logErr("[Login] Unhandled login type [opcode=$opcode]!")
                session.disconnect()
            }
        }
    }

    companion object {

        /**
         * Decodes a world login request.
         * @param session The session.
         * @param buffer The buffer to read from.
         */
        private fun decodeWorld(opcode: Int, session: IoSession, buffer: ByteBuffer) {
            var buffer = buffer
            val d = buffer.get() // Memory?
            val e = buffer.get() // no advertisement = 1
            val f = buffer.get() // 1
            val windowMode = buffer.get().toInt() // Screen size mode
            val screenWidth = buffer.short.toInt() // Screen size Width
            val screenHeight = buffer.short.toInt() // Screen size Height
            val displayMode = buffer.get().toInt() // Display mode
            val data = ByteArray(24) // random.dat data.
            buffer[data]
            ByteBufferUtils.getString(buffer)
            buffer.int // Affiliate id
            buffer.int // Hash containing a bunch of settings
            val curpackets = buffer.short //Current interface packet counter.
            for (i in Cache.getIndexes().indices) {
                val crc = if (Cache.getIndexes()[i] == null) 0 else Cache.getIndexes()[i].information.informationContainer.crc
                if (crc != buffer.int && crc != 0) {
                    /*session.write(Response.UPDATED);
				return;*/
                }
            }
            buffer = getRSABlock(buffer)
            buffer.rewind()
            if(buffer.get().toInt() != 10){
                session.write(Response.COULD_NOT_LOGIN)
                return
            }
            val isaacSeed = getISAACSeed(buffer)
            val inCipher = ISAACCipher(isaacSeed)
            for(i in 0..curpackets){
                inCipher.nextValue
            }
            for (i in 0..3) {
                isaacSeed[i] += 50
            }
            val outCipher = ISAACCipher(isaacSeed)
            session.isaacPair = ISAACPair(inCipher, outCipher)
            session.clientInfo = ClientInfo(displayMode, windowMode, screenWidth, screenHeight)
            val b = buffer
            SystemLogger.logInfo("spawning thread to handle login")
            TaskExecutor.executeSQL {
                SystemLogger.logInfo("login thread start")
                Thread.currentThread().name = "Login Password Response"
                SystemLogger.logInfo("login thread named")
                try {
                    val username = StringUtils.longToString(b.long)
                    SystemLogger.logInfo("got username")
                    val password = ByteBufferUtils.getString(b)
                    SystemLogger.logInfo("got password")
                    val response = PlayerSQLManager.getCredentialResponse(username, password)
                    SystemLogger.logInfo("got sql response")
                    if (response != Response.SUCCESSFUL) {
                        SystemLogger.logInfo("not success :(")
                        session.write(response, true)
                        return@executeSQL
                    }
                    SystemLogger.logInfo("great success, attempting login")
                    login(PlayerDetails(username, password), session, b, opcode)
                    SystemLogger.logInfo("done")
                } catch (e: Exception) {
                    SystemLogger.logInfo("big whoops")
                    e.printStackTrace()
                    session.write(Response.COULD_NOT_LOGIN)
                }
                SystemLogger.logInfo("end login thread")
            }
        }

        /**
         * Handles the login procedure after we check an acc is registered & certified.
         * @param details the player's details.
         * @param session the session.
         * @param buffer the byte buffer.
         * @param opcode the opcode.
         */
        @JvmStatic
        private fun login(details: PlayerDetails, session: IoSession, buffer: ByteBuffer, opcode: Int) {
            SystemLogger.logInfo("login")
            if(!Repository.LOGGED_IN_PLAYERS.contains(details.username))
                Repository.LOGGED_IN_PLAYERS.add(details.username)
            val parser = LoginParser(details, LoginType.fromType(opcode))
            details.session = session
            details.info.translate(UIDInfo(details.ipAddress, ByteBufferUtils.getString(buffer), ByteBufferUtils.getString(buffer), ByteBufferUtils.getString(buffer)))
            if (WorldCommunicator.isEnabled()) {
                WorldCommunicator.register(parser)
            } else {
                TaskExecutor.executeSQL {parser.run()}
            }
        }

        /**
         * Gets the ISAAC seed from the buffer.
         * @param buffer The buffer to read from.
         * @return The ISAAC seed.
         */
        @JvmStatic
        fun getISAACSeed(buffer: ByteBuffer): IntArray {
            val seed = IntArray(4)
            for (i in 0..3) {
                seed[i] = buffer.int
            }
            return seed
        }

        /**
         * Gets the RSA block buffer.
         * @param buffer The buffer to get the RSA block from.
         * @return The RSA block buffer.
         */
        @JvmStatic
        fun getRSABlock(buffer: ByteBuffer): ByteBuffer {
            SystemLogger.logInfo("getRSABlock")
            fun ByteArray.toHex(): String = joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }
            SystemLogger.logInfo(buffer.array().sliceArray(0..1500).toHex())
            val numBytes = 256 + buffer.get()
            val encryptedByteArray = ByteArray(numBytes)
            buffer.get(encryptedByteArray)
            val encryptedBytes = BigInteger(encryptedByteArray)
            return ByteBuffer.wrap(encryptedBytes.modPow(ServerConstants.EXPONENT, ServerConstants.MODULUS).toByteArray())
        }
    }
}