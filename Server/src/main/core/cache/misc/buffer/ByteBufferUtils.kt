package core.cache.misc.buffer

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

object ByteBufferUtils {
    /**
     * Gets a string from the byte buffer.
     * @param buffer The byte buffer.
     * @return The string.
     */
	@JvmStatic
	fun getString(buffer: ByteBuffer): String {
        val sb = StringBuilder()
        var b: Byte
        while (buffer.get().also { b = it }.toInt() != 0) {
            sb.append(Char(b.toUShort()))
        }
        return sb.toString()
    }

    /**
     * Puts a string on the byte buffer.
     * @param s The string to put.
     * @param buffer The byte buffer.
     */
	@JvmStatic
	fun putString(s: String, buffer: ByteBuffer) {
        buffer.put(s.toByteArray(StandardCharsets.UTF_8)).put(0.toByte())
    }

    /**
     * Gets a string from the byte buffer.
     * @param s The string.
     * @param buffer The byte buffer.
     * @return The string.
     */
    fun putGJ2String(s: String, buffer: ByteBuffer): ByteBuffer {
        val packed = ByteArray(256)
        val length = packGJString2(0, packed, s)
        return buffer.put(0.toByte()).put(packed, 0, length).put(0.toByte())
    }

    /**
     * Decodes the XTEA encryption.
     * @param keys The keys.
     * @param start The start index.
     * @param end The end index.
     * @param buffer The byte buffer.
     */
    fun decodeXTEA(keys: IntArray, start: Int, end: Int, buffer: ByteBuffer) {
        val l = buffer.position()
        buffer.position(start)
        val length = (end - start) / 8
        for (i in 0 until length) {
            var firstInt = buffer.getInt()
            var secondInt = buffer.getInt()
            var sum = -0x3910c8e0
            val delta = -0x61c88647
            var j = 32
            while (j-- > 0) {
                secondInt -= keys[sum and 0x1c84 ushr 11] + sum xor (firstInt ushr 5 xor (firstInt shl 4)) + firstInt
                sum -= delta
                firstInt -= (secondInt ushr 5 xor (secondInt shl 4)) + secondInt xor keys[sum and 3] + sum
            }
            buffer.position(buffer.position() - 8)
            buffer.putInt(firstInt)
            buffer.putInt(secondInt)
        }
        buffer.position(l)
    }

    /**
     * Converts a String to an Integer?
     * @param position The position.
     * @param buffer The buffer used.
     * @param string The String to convert.
     * @return The Integer.
     */
	@JvmStatic
	fun packGJString2(position: Int, buffer: ByteArray, string: String): Int {
        val length = string.length
        var offset = position
        var i = 0
        while (length > i) {
            val character = string[i].code
            if (character > 127) {
                if (character > 2047) {
                    buffer[offset++] = (character or 919275 shr 12).toByte()
                    buffer[offset++] = (128 or (character shr 6 and 63)).toByte()
                    buffer[offset++] = (128 or (character and 63)).toByte()
                } else {
                    buffer[offset++] = (character or 12309 shr 6).toByte()
                    buffer[offset++] = (128 or (character and 63)).toByte()
                }
            } else buffer[offset++] = character.toByte()
            i++
        }
        return offset - position
    }

    /**
     * Gets a tri-byte from the buffer.
     * @param buffer The buffer.
     * @return The value.
     */
	@JvmStatic
	fun getMedium(buffer: ByteBuffer): Int {
        return (buffer.get().toInt() and 0xFF shl 16) + (buffer.get().toInt() and 0xFF shl 8) + (buffer.get()
            .toInt() and 0xFF)
    }

    /**
     * Gets a smart from the buffer.
     * @param buffer The buffer.
     * @return The value.
     */
	@JvmStatic
	fun getSmart(buffer: ByteBuffer): Int {
        val peek = buffer.get().toInt() and 0xFF
        return if (peek <= Byte.MAX_VALUE) {
            peek
        } else (peek shl 8 or (buffer.get().toInt() and 0xFF)) - 32768
    }

    /**
     * Gets a smart from the buffer.
     * @param buffer The buffer.
     * @return The value.
     */
	@JvmStatic
	fun getBigSmart(buffer: ByteBuffer): Int {
        var value = 0
        var current = getSmart(buffer)
        while (current == 32767) {
            current = getSmart(buffer)
            value += 32767
        }
        value += current
        return value
    }
}