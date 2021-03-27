package js5server

import com.displee.cache.CacheLibrary
import io.ktor.utils.io.*
import js5server.ext.readUMedium
import kotlin.math.min

class FileServer(
    private val provider: DataProvider,
    private val versionTable: ByteArray
) {

    /**
     * Fulfills a request by sending the requested files data to the requester
     */
    suspend fun fulfill(read: ByteReadChannel, write: ByteWriteChannel, prefetch: Boolean) {
        val value = read.readUMedium()
        val index = value shr 16
        val archive = value and 0xffff
        val data = data(index, archive) ?: return println("Unable to fulfill request $index $archive $prefetch.")
        if (index == 255 && archive == 255) {
            serve255(write, data)
        } else {
            serve(write, index, archive, data, prefetch)
        }
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
     * Writes [source] [offset] [size] to [write] and starting at [headerSize] inserting a [SEPARATOR] every [split] bytes
     */
    private suspend fun serve255(write: ByteWriteChannel, source: ByteArray) {
        write.writeByte(255)
        write.writeShort(255)
        write.writeByte(0)
        write.writeInt(source.size)
        write.writeFully(source)
    }

    /**
     * Writes response header followed by the contents of [data] to [write]
     */
    suspend fun serve(write: ByteWriteChannel, index: Int, archive: Int, data: ByteArray, prefetch: Boolean) {
        val compression = data[0].toInt()
        val size = getInt(data[1], data[2], data[3], data[4]) + if (compression != 0) 8 else 4
        write.writeByte(index)
        write.writeShort(archive)
        write.writeByte(if (prefetch) compression or 0x80 else compression)
        serve(write, HEADER, data, OFFSET, size, SPLIT)
    }

    /**
     * Writes [source] [offset] [size] to [write] and starting at [headerSize] inserting a [SEPARATOR] every [split] bytes
     */
    suspend fun serve(write: ByteWriteChannel, headerSize: Int, source: ByteArray, offset: Int, size: Int, split: Int) {
        var length = min(size, split - headerSize)
        write.writeFully(source, offset, length)
        var written = length
        while (written < size) {
            write.writeByte(SEPARATOR)

            length = if (size - written < split) size - written else split - 1
            write.writeFully(source, written + offset, length)
            written += length
        }
    }

    companion object {

        private fun getInt(b1: Byte, b2: Byte, b3: Byte, b4: Byte) = b1.toInt() shl 24 or (b2.toInt() and 0xff shl 16) or (b3.toInt() and 0xff shl 8) or (b4.toInt() and 0xff)

        private const val SEPARATOR = 255
        private const val HEADER = 4
        private const val SPLIT = 512
        private const val OFFSET = 1
    }

    /**
     *  Length of archive with [name] in [index]
     */
    fun file(cacheLibrary: CacheLibrary, index: Int, name: String): Int {
        val idx = cacheLibrary.index(index)
        val archive = idx.archiveId(name)
        if (archive == -1) {
            return 0
        }
        return (idx.readArchiveSector(archive)?.size ?: 2) - 2
    }

    /**
     * Length of all archives in [index]
     */
    fun archive(cache: CacheLibrary, index: Int): Int {
        var total = 0
        val idx = cache.index(index)
        idx.archiveIds().forEach { archive ->
            total += idx.readArchiveSector(archive)?.size ?: 0
        }
        total += cache.index255?.readArchiveSector(index)?.size ?: 0
        return total
    }
}