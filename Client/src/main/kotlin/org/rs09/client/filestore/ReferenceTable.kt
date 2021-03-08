package org.rs09.client.filestore

import org.rs09.client.DataBuffer
import org.rs09.client.filestore.compression.Container
import org.rs09.client.util.CRC

class ReferenceTable(data: ByteArray, crc: Int) {
    val crc: Int = CRC.crc32(data, data.size)
    var revision = 0
    var validArchiveAmount = 0
    var archiveAmount = 0
    lateinit var validArchiveIds: IntArray
    lateinit var archiveRevisions: IntArray
    lateinit var validFileIds: Array<IntArray?>
    lateinit var archiveCRCs: IntArray
    lateinit var archiveLengths: IntArray
    lateinit var archiveFileLengths: IntArray
    lateinit var archiveNameHash: IntArray
    var aLookupTable_949: LookupTable? = null
    var aLookupTableArray962: Array<LookupTable?>? = null
    var fileNameHashes: Array<IntArray?>? = null

    private fun decode(data: ByteArray) {
        val buffer = DataBuffer(Container.decode(data))
        val format = buffer.readUnsignedByte()
        check(format == 5 || format == 6) { "Unexpected ReferenceTable format $format. Expected 5 or 6." }
        revision = if (format >= 6) buffer.readInt() else 0

        val fields = buffer.readUnsignedByte()

        validArchiveAmount = buffer.readUnsignedShort()
        validArchiveIds = IntArray(validArchiveAmount)

        var offset = 0
        var highest = -1

        for (i in 0 until validArchiveAmount) {
            offset += buffer.readUnsignedShort()
            validArchiveIds[i] = offset
            if (validArchiveIds[i] > highest)
                highest = validArchiveIds[i]
        }

        archiveAmount = highest - -1
        archiveRevisions = IntArray(archiveAmount)
        validFileIds = arrayOfNulls(archiveAmount)
        archiveCRCs = IntArray(archiveAmount)
        archiveLengths = IntArray(archiveAmount)
        archiveFileLengths = IntArray(archiveAmount)

        if (fields != 0) {
            archiveNameHash = IntArray(archiveAmount)

            for (i in 0 until archiveAmount)
                archiveNameHash[i] = -1

            for (i in 0 until validArchiveAmount)
                archiveNameHash[validArchiveIds[i]] = buffer.readInt()

            aLookupTable_949 = LookupTable(archiveNameHash)
        }

        for (i in 0 until validArchiveAmount)
            archiveCRCs[validArchiveIds[i]] = buffer.readInt()

        for (i in 0 until validArchiveAmount)
            archiveRevisions[validArchiveIds[i]] = buffer.readInt()

        for (i in 0 until validArchiveAmount)
            archiveFileLengths[validArchiveIds[i]] = buffer.readUnsignedShort()

        for (i in 0 until validArchiveAmount) {
            offset = 0
            val archiveId = validArchiveIds[i]
            val archiveFileCount = archiveFileLengths[archiveId]
            var highestFileId = -1
            validFileIds[archiveId] = IntArray(archiveFileCount)
            for (file in 0 until archiveFileCount) {
                offset += buffer.readUnsignedShort()
                validFileIds[archiveId]!![file] = offset
                if (offset > highestFileId) highestFileId = offset
            }
            archiveLengths[archiveId] = highestFileId + 1
            if (archiveFileCount == highestFileId + 1)
                validFileIds[archiveId] = null
        }

        if (fields != 0) {
            val aClass69Array962 = arrayOfNulls<LookupTable>(highest + 1)
            val fileNameHashes = arrayOfNulls<IntArray>(highest + 1)

            for (i in 0 until validArchiveAmount) {
                val archiveId = validArchiveIds[i]
                val archiveFileLength = archiveFileLengths[archiveId]
                val archiveFileNameHashes = IntArray(archiveLengths[archiveId])
                fileNameHashes[archiveId] = archiveFileNameHashes

                for (j in 0 until archiveLengths[archiveId])
                    archiveFileNameHashes[j] = -1

                for (j in 0 until archiveFileLength) {
                    val fileIndex = if (validFileIds[archiveId] == null) j else validFileIds[archiveId]!![j]
                    archiveFileNameHashes[fileIndex] = buffer.readInt()
                }
                aClass69Array962[archiveId] = LookupTable(archiveFileNameHashes)
            }

            this.fileNameHashes = fileNameHashes
            this.aLookupTableArray962 = aClass69Array962
        }
    }

    init {
        if (this.crc != crc)
            throw IllegalArgumentException("CRC mismatch - expected $crc, calculated ${this.crc}")
        decode(data)
    }
}