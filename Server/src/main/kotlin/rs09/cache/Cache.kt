package rs09.cache

import com.displee.cache.CacheLibrary
import com.displee.cache.ProgressListener
import com.displee.cache.index.Index
import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.SceneryDefinition
import rs09.game.system.SystemLogger
import rs09.net.event.js5.DataProvider
import java.nio.ByteBuffer

object Cache {

    private lateinit var cacheLibrary: CacheLibrary
    private lateinit var progressListener: ProgressListener
    lateinit var versionTable: ByteArray
    lateinit var provider: DataProvider

    @JvmStatic
    fun init(cachePath: String?) {
        progressListener = SystemLogger.CreateProgressListener()
        cacheLibrary = CacheLibrary(cachePath.toString(), false, progressListener)
        versionTable = generateUKeys()
        provider = DataProvider(cacheLibrary)
        parseDefinitions()
    }

    private fun parseDefinitions() {
        ItemDefinition.parse()
        SceneryDefinition.parse()
    }

    private fun generateUKeys(): ByteArray {
        val buffer = ByteBuffer.allocate(cacheLibrary.indices().size * 8)
        for (index in cacheLibrary.indices()) {
            buffer.putInt(index.crc)
            buffer.putInt(index.revision)
        }
        return buffer.array()
    }

    fun getIndex(index: CacheIndex): Index {
        return cacheLibrary.index(index.id)
    }

    @JvmStatic
    fun getData(index: CacheIndex, archive: Int, file: Int): ByteArray? {
        return cacheLibrary.data(index.id, archive, file, null)
    }

    @JvmStatic
    fun getData(index: CacheIndex, archive: CacheArchive, file: Int): ByteArray? {
        return cacheLibrary.data(index.id, archive.id, file, null)
    }

    @JvmStatic
    fun getData(index: CacheIndex, archive: String): ByteArray? {
        return cacheLibrary.data(index.id, archive, 0)
    }

    @JvmStatic
    fun getData(index: CacheIndex, archive: String, xtea: IntArray): ByteArray? {
        return cacheLibrary.data(index.id, archive, 0, xtea)
    }

    @JvmStatic
    fun getArchiveId(index: CacheIndex, archive: String): Int {
        return cacheLibrary.index(index.id).archiveId(archive)
    }

    @JvmStatic
    fun getArchiveCapacity(index: CacheIndex, archive: CacheArchive): Int {
        return cacheLibrary.index(index.id).archive(archive.id)?.files()?.size ?: -1
    }

    @JvmStatic
    fun getIndexCapacity(index: CacheIndex): Int {
        val lastArchive = (cacheLibrary.index(index.id).archives().last())
        return (lastArchive.files().size) + (lastArchive.id * 256)
    }
}