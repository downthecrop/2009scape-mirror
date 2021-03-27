package js5server

import com.displee.cache.CacheLibrary

interface DataProvider {
    fun data(index: Int, archive: Int): ByteArray?

    companion object {
        operator fun invoke(cache: CacheLibrary) = object : DataProvider {
            override fun data(index: Int, archive: Int) =
                if (index == 255) {
                    cache.index255?.readArchiveSector(archive)?.data
                } else {
                    cache.index(index).readArchiveSector(archive)?.data
                }
        }
    }
}