package org.rs09.client

import org.rs09.client.util.ByteArrayPool
import org.runite.client.RSString

open class DataBuffer(@JvmField val buffer: ByteArray, @JvmField var index: Int) : Linkable() {
    constructor(buffer: ByteArray) : this(buffer, 0)
    constructor(length: Int) : this(ByteArrayPool.getByteArray(length), 0)

    fun readUnsignedByte(): Int {
        return buffer[index++].toInt() and 0xff
    }

    fun readUnsignedShort(): Int {
        index += 2
        return (buffer[index - 1].toInt() and 0xff) +
                (buffer[index - 2].toInt() and 0xff shl 8)
    }

    fun readInt(): Int {
        index += 4
        return (buffer[index - 4].toInt() and 0xff shl 24) +
                (buffer[index - 3].toInt() and 0xff shl 16) +
                (buffer[index - 2].toInt() and 0xff shl 8) +
                (buffer[index - 1].toInt() and 0xff)
    }

    fun writeInt(value: Int) {
        buffer[index++] = (value shr 24).toByte()
        buffer[index++] = (value shr 16).toByte()
        buffer[index++] = (value shr 8).toByte()
        buffer[index++] = value.toByte()
    }

    fun writeLong(value: Long) {
        buffer[index++] = (value shr 56).toInt().toByte()
        buffer[index++] = (value shr 48).toInt().toByte()
        buffer[index++] = (value shr 40).toInt().toByte()
        buffer[index++] = (value shr 32).toInt().toByte()
        buffer[index++] = (value shr 24).toInt().toByte()
        buffer[index++] = (value shr 16).toInt().toByte()
        buffer[index++] = (value shr 8).toInt().toByte()
        buffer[index++] = value.toInt().toByte()
    }

    fun readBytes(length: Int): ByteArray {
        val out = ByteArray(length)
        System.arraycopy(buffer, index, out, 0, length)
        index += length
        return out
    }

    fun write128Byte(value: Int) {
        buffer[index++] = (128 - value).toByte()
    }

    fun writeString(value: RSString) {
        index += value.method1580(buffer, index, value.length())
        buffer[index++] = 0
    }

    /*
     * TODO Refactor / rename everything beyond this point
     */

    fun method739(var2: Int, var3: Long) {
        var var2 = var2
        --var2
        if (var2 >= 0 && var2 <= 7) {
            var var5 = var2 * 8
            while (0 <= var5) {
                buffer[index++] = (var3 shr var5).toInt().toByte()
                var5 -= 8
            }
        } else {
            throw IllegalArgumentException()
        }
    }

    fun method741(): Int {
        var var2 = buffer[index++].toInt()
        var var3: Int
        var3 = 0
        while (0 > var2) {
            var3 = 127 and var2 or var3 shl 7
            var2 = buffer[index++].toInt()
        }
        return var2 or var3
    }

    fun method742(var2: Int) {
        buffer[index + -var2 - 4] = (var2 shr 24).toByte()
        buffer[index + -var2 - 3] = (var2 shr 16).toByte()
        buffer[index + -var2 - 2] = (var2 shr 8).toByte()
        buffer[index + -var2 - 1] = var2.toByte()
    }

    fun readSignedShort128(): Int {
        index += 2
        var value: Int = (buffer[index - 2].toInt() and 0xff shl 8) +
                (buffer[index - 1].toInt() - 128 and 0xff)
        if (value > 32767) {
            value -= 65536
        }
        return value
    }
}