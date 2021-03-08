package org.rs09.client.filestore.compression

import org.rs09.client.DataBuffer
import java.util.zip.Inflater

object GzipDecompressor {
    private var inflater = Inflater(true)

    fun decompress(buffer: DataBuffer, out: ByteArray) {
        if (buffer.buffer[buffer.index].toInt() != 31 || buffer.buffer[buffer.index + 1].toInt() != -117)
            throw RuntimeException("Invalid GZIP header!")

        try {
            inflater.setInput(buffer.buffer, buffer.index + 10, -8 - (10 + buffer.index) + buffer.buffer.size)
            inflater.inflate(out)
        } catch (var5: Exception) {
            inflater.reset()
            throw RuntimeException("Invalid GZIP compressed data!")
        }
        inflater.reset()
    }
}