package core.cache.misc

import core.cache.bzip2.BZip2Decompressor
import core.cache.gzip.GZipDecompressor
import java.nio.ByteBuffer
import java.util.*
import java.util.zip.CRC32

class ContainersInformation(informationContainerPackedData: ByteArray) {
    @JvmField
	val informationContainer: Container
    private var protocol = 0
    var revision = 0
    var containersIndexes: IntArray = intArrayOf()
    var containers: Array<FilesContainer?> = arrayOf()
    private var filesNamed = false // If files has to be named
    private var whirpool = false // Flag for whirpool
    val data: ByteArray

    init {
        data = Arrays.copyOf(informationContainerPackedData, informationContainerPackedData.size)
        informationContainer = Container()
        informationContainer.version =
            (informationContainerPackedData[informationContainerPackedData.size - 2].toInt() shl 8 and 0xff00) + (informationContainerPackedData[-1 + informationContainerPackedData.size].toInt() and 0xff)
        val crc32 = CRC32()
        crc32.update(informationContainerPackedData)
        informationContainer.crc = crc32.value.toInt()
        decodeContainersInformation(unpackCacheContainer(informationContainerPackedData))
    }

    fun decodeContainersInformation(data: ByteArray?) {
        val buffer = ByteBuffer.wrap(data)
        protocol = buffer.get().toInt() and 0xFF
        if (protocol != 5 && protocol != 6) {
            throw RuntimeException()
        }
        revision = if (protocol < 6) 0 else buffer.getInt()
        val nameHash = buffer.get().toInt() and 0xFF
        filesNamed = 0x1 and nameHash != 0
        whirpool = 0x2 and nameHash != 0
        containersIndexes = IntArray(buffer.getShort().toInt() and 0xFFFF)
        var lastIndex = -1
        for (index in containersIndexes.indices) {
            val containerIndex =
                (buffer.getShort().toInt() and 0xFFFF) + if (index == 0) 0 else containersIndexes[index - 1]
            containersIndexes[index] = containerIndex
            if (containerIndex > lastIndex) {
                lastIndex = containerIndex
            }
        }
        containers = arrayOfNulls(lastIndex + 1)
        for (containerIdx in containersIndexes) {
            containers[containerIdx] = FilesContainer()
        }
        if (filesNamed) {
            for (containerIdx in containersIndexes) {
                containers[containerIdx]!!.nameHash = buffer.getInt()
            }
        }
        var filesHashes: Array<ByteArray?>? = null
        if (whirpool) {
            filesHashes = arrayOfNulls(containers.size)
            for (containerIdx in containersIndexes) {
                filesHashes[containerIdx] = ByteArray(64)
                buffer[filesHashes[containerIdx], 0, 64]
            }
        }
        for (containerIdx in containersIndexes) {
            containers[containerIdx]!!.crc = buffer.getInt()
        }
        for (containerIdx in containersIndexes) {
            containers[containerIdx]!!.version = buffer.getInt()
        }
        for (containerIdx in containersIndexes) {
            containers[containerIdx]!!.filesIndexes = IntArray(buffer.getShort().toInt() and 0xFFFF)
        }
        for (containerIdx in containersIndexes) {
            val container = containers[containerIdx]
            val filesIndexes = container!!.filesIndexes
            var lastFileIndex = -1
            for (fileIndex in filesIndexes!!.indices) {
                val fileIdx =
                    (buffer.getShort().toInt() and 0xFFFF) + if (fileIndex == 0) 0 else filesIndexes[fileIndex - 1]
                filesIndexes[fileIndex] = fileIdx
                if (fileIdx > lastFileIndex) {
                    lastFileIndex = fileIdx
                }
            }
            val files = arrayOfNulls<Container>(lastFileIndex + 1)
            container.files = files
            for (fileIdx in filesIndexes) {
                files[fileIdx] = Container()
            }
        }
        if (whirpool) {
            for (containerIdx in containersIndexes) {
                val container = containers[containerIdx]
                val containerHashes = filesHashes!![containerIdx]
                val files = container!!.files
                for (fileIdx in container.filesIndexes!!) {
                    files!![fileIdx]!!.version = containerHashes!![fileIdx].toInt()
                }
            }
        }
        if (filesNamed) {
            for (containerIdx in containersIndexes) {
                val container = containers[containerIdx]
                val files = container!!.files
                for (fileIdx in container.filesIndexes!!) {
                    files!![fileIdx]!!.nameHash = buffer.getInt()
                }
            }
        }
    }

    companion object {
        @JvmStatic
		fun unpackCacheContainer(packedData: ByteArray?): ByteArray? {
            val buffer = ByteBuffer.wrap(packedData)
            val compression = buffer.get().toInt() and 0xFF
            val containerSize = buffer.getInt()
            if (containerSize < 0 || containerSize > 5000000) {
                return null
                // throw new RuntimeException();
            }
            if (compression == 0) {
                val unpacked = ByteArray(containerSize)
                buffer[unpacked, 0, containerSize]
                return unpacked
            }
            val decompressedSize = buffer.getInt()
            if (decompressedSize < 0 || decompressedSize > 20000000) {
                return null
                // throw new RuntimeException();
            }
            val decompressedData = ByteArray(decompressedSize)
            if (compression == 1) {
                BZip2Decompressor.decompress(decompressedData, packedData, containerSize, 9)
            } else {
                GZipDecompressor.decompress(buffer, decompressedData)
            }
            return decompressedData
        }
    }
}
