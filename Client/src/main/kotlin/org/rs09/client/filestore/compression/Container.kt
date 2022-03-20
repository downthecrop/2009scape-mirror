package org.rs09.client.filestore.compression

import org.rs09.client.DataBuffer
import org.runite.client.Bzip2Decompressor

object Container {
    private const val NONE = 0

    fun decode(data: ByteArray): ByteArray {
        val buffer = DataBuffer(data)
        val compression = buffer.readUnsignedByte()
        val size = buffer.readInt()

        check(size >= 0) { "Container had data size of < 0: $size with compression $compression" }

        if (compression == NONE) {
            return buffer.readBytes(size)
        }

        val uncompressedSize = buffer.readInt()
        check(uncompressedSize >= 0) { "Container had uncompressed data size of < 0: $size with compression $compression" }

        val uncompressed = ByteArray(uncompressedSize)
        if (compression == 1) {
            Bzip2Decompressor.decompress(uncompressed, uncompressedSize, data)
        } else {
            GzipDecompressor.decompress(buffer, uncompressed)
        }
        return uncompressed
    }
}