package org.rs09.client.util

object CRC {
    private val CRC32 = IntArray(256)
    private val CRC64 = LongArray(256)

    init {
        // Generate CRC tables
        for (i in 0 until 256) {
            var var0 = i
            for (var2 in 0 until 8) {
                var0 = if (1 == 1 and var0) {
                    var0 ushr 1 xor -306674912
                } else {
                    var0 ushr 1
                }
            }
            CRC32[i] = var0
        }

    }

    fun crc32(data: ByteArray, length: Int): Int = crc32(data, 0, length)

    fun crc32(data: ByteArray, offset: Int, end: Int): Int {
        var hash = -1
        var i = offset
        while (end > i) {
            hash = hash ushr 8 xor CRC32[hash xor data[i].toInt() and 0xff]
            ++i
        }
        hash = hash.inv()
        return hash
    }
}