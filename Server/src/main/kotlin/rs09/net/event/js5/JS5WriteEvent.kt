package rs09.net.event.js5

import core.net.IoSession
import core.net.IoWriteEvent
import rs09.cache.Cache.provider
import rs09.cache.Cache.versionTable
import rs09.game.system.SystemLogger
import rs09.net.p1
import rs09.net.p2
import rs09.net.p4
import java.nio.ByteBuffer
import kotlin.math.min

class JS5WriteEvent(session: IoSession?, context: Any?) : IoWriteEvent(session, context) {

    override fun write(session: IoSession, context: Any) {
        val request = context as Triple<*, *, *>
        val index = request.first as Int
        val archive = request.second as Int
        val priority = request.third as Boolean
        val data = data(index, archive) ?: return SystemLogger.logErr(this::class.java,"Unable to fulfill request $index $archive $priority.")
        if (index == 255 && archive == 255) {
            val buffer = serve255(data)
            session.queue(buffer)
        } else {
            val buffer = serve(index, archive, data, priority)
            session.queue(buffer)
        }
        return
    }

    /**
     * @return data for an [index]'s [archive] file or [versionTable] when index and archive are both 255
     */
    fun data(index: Int, archive: Int): ByteArray? {
        if (index == 255 && archive == 255) {
            return versionTable
        }
        return provider.data(index, archive)
    }

    /**
     * Serve255 - Prepares a buffer to send the version table data
     *
     * @param data - The binary data retrieved from the cache
     * @return buffer containing version table information
     */
    private fun serve255(data: ByteArray): ByteBuffer {
        val buffer = ByteBuffer.allocate(8 + data.size)
        buffer.p1(255)
        buffer.p2(255)
        buffer.p1(0)
        buffer.p4(data.size)
        buffer.put(data)
        buffer.flip()
        return buffer
    }

    fun serve(index: Int, archive: Int, data: ByteArray, prefetch: Boolean): ByteBuffer {
        val compression = data[0].toInt()
        val size = getInt(data[1], data[2], data[3], data[4]) + if (compression != 0) 8 else 4 // 9 : 5?
        val buffer = ByteBuffer.allocate((size + 5) + (size / 512) + 10)
        buffer.p1(index)
        buffer.p2(archive)
        buffer.p1((if (prefetch) compression or 0x80 else compression))

        var length = min(size, SPLIT - HEADER)
        buffer.put(data, OFFSET, length)
        var written = length

        while (written < size) {
            buffer.p1(SEPARATOR)
            length = if (size - written < SPLIT) size - written else SPLIT - 1
            buffer.put(data, written + OFFSET, length)
            written += length
        }
        buffer.flip()
        return buffer
    }

    companion object {
        private fun getInt(b1: Byte, b2: Byte, b3: Byte, b4: Byte) = b1.toInt() shl 24 or (b2.toInt() and 0xff shl 16) or (b3.toInt() and 0xff shl 8) or (b4.toInt() and 0xff)

        private const val SEPARATOR = 255
        private const val HEADER = 4
        private const val SPLIT = 512
        private const val OFFSET = 1
    }
}