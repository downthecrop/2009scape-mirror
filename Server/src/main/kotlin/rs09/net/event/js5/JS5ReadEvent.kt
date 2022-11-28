package rs09.net.event.js5

import core.net.IoReadEvent
import core.net.IoSession
import rs09.game.system.SystemLogger
import rs09.net.g1
import rs09.net.g2
import rs09.net.g3
import java.nio.ByteBuffer

class JS5ReadEvent(session: IoSession?, buffer: ByteBuffer?) : IoReadEvent(session, buffer) {

    override fun read(session: IoSession?, buffer: ByteBuffer?) {
        if (session != null && buffer != null) {
            while (buffer.hasRemaining()) {
                when (val opcode = buffer.g1()) {

                    /**
                     *  OPCODE 0 - Prefetch file request (Low priority request)
                     *  Handler for the prefetch file request opcode for the JS5 connection
                     *  The request information (subUID) is contained in a medium. To decode
                     *  first get the unsigned byte which is the Cache index ID, then read an unsigned short
                     *  which is the archive ID. (or just a medium 3 bytes)
                     *
                     *  On initial connection:
                     *     -Prefetch will NEVER be the first request on initial connection
                     *
                     *  After high priority sends 255 CRC info + user cache is incomplete:
                     *     -We will start receiving files that the client does not need immediately
                     *
                     *  Buffer size 4: [(byte) 0, (medium) request.subUID as Int]
                     */
                    PREFETCH_REQUEST -> {
                        val value = buffer.g3()
                        val index = value shr 16
                        val archive = value and 0xFFFF
                        session.write(Triple(index, archive, opcode == 0), true)
                    }

                    /**
                     *  OPCODE 1 - High priority file request
                     *  Handler for the high priority file request opcode for the JS5 connection
                     *  The request information (subUID) is contained in a medium. To decode
                     *  first get the unsigned byte which is the Cache Index ID, then read an unsigned short
                     *  which is the archive ID.
                     *
                     *  On initial connection:
                     *     -Requests index 255, archive 255 which contains the master record of index CRC information
                     *
                     *  After 255 CRC info sent + user has a brand new cache:
                     *     -We will start to receive requests for files the client needs IMMEDIATELY, i.e. login interfaces/music/models
                     *      The client stores and knows what files it needs upon moving past the main menu/login screen
                     *
                     *  Buffer size 4: [(byte) 1, (medium) request.subUID as Int]
                     */
                    PRIORITY_REQUEST -> {
                        val value = buffer.g3()
                        val index = value shr 16
                        val archive = value and 0xFFFF
                        session.write(Triple(index, archive, opcode == 0))
                    }

                    /**
                     *  OPCODE 2 - Client logged in
                     *  Handler for the client logged in opcode for the JS5 connection
                     *  If the client gameState is NOT on stage 5 || 10 || 28 then it sends OP2
                     *
                     *  Buffer size 4: [(byte) 2, (medium) 0]
                     */
                    STATUS_LOGGED_IN -> {
                        buffer.g3()
                    }

                    /**
                     *  OPCODE 3 - Client logged out
                     *  Handler for the client logged out opcode for the JS5 connection
                     *  If the client gameState is ON stage 5 || 10 || 28 then it sends OP3
                     *
                     *  Buffer size 4: [(byte) 3, (medium) 0]
                     */
                    STATUS_LOGGED_OUT -> {
                        buffer.g3()
                    }

                    /**
                     *  OPCODE 4 - Encryption
                     *  Handler for the encryption opcode for the JS5 connection
                     *  The client sends a byte encryption key that we'll send out with the requests
                     *
                     *  Buffer size 4: [(byte) 4, (byte) encryptionKey, (shortMirrored) 0]
                     */
                    ENCRYPTED -> {
                        val value = buffer.g1()
                        val mark = buffer.g2()
                        if (mark != 0) {
                            SystemLogger.logErr(this::class.java, "[$opcode] mark [$mark] not 0!")
                        }
                    }

                    /**
                     *  OPCODE 6 - Connection Initialized
                     *  Handler for the initial connection opcode for the JS5 connection
                     *  We connect the client then clear the buffer by skipping the remaining 3 bytes
                     *
                     *  Buffer size 4: [(byte) 6, (medium) 3]
                     */
                    ACKNOWLEDGE -> {
                        buffer.g3()
                    }

                    /**
                     *  OPCODE 7 - Connection Disconnect
                     *  Handler for the disconnection connection opcode for the JS5 connection
                     *  This is only sent by request of a client command
                     *
                     *  Buffer size 4: [(byte) 7, (medium) 3]
                     */
                    DISCONNECT -> {
                        buffer.g3()
                        session.disconnect()
                    }

                    else -> {
                        SystemLogger.logErr(this::class.java, "What the fuck is happening [$opcode]")
                    }
                }
            }
        }
    }

    companion object {
        // Opcodes
        const val PREFETCH_REQUEST = 0
        const val PRIORITY_REQUEST = 1
        const val STATUS_LOGGED_IN = 2
        const val STATUS_LOGGED_OUT = 3
        const val ENCRYPTED = 4
        const val ACKNOWLEDGE = 6
        const val DISCONNECT = 7
    }
}