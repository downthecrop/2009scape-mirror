package org.rs09.client.filestore.resources.configs.enums

import org.runite.client.CacheIndex
import org.runite.client.DataBuffer
import org.rs09.client.data.NodeCache

object EnumDefinitionProvider {
    private val cache = NodeCache<EnumDefinition>(128)
    private lateinit var index: CacheIndex

    @JvmStatic
    fun provide(id: Int): EnumDefinition {
        var def = cache.get(id.toLong())
        if (def != null) return def

        def = EnumDefinition()
        val bytes = index.getFile(id ushr 8, id and 0xff)
        if (bytes != null) def.decode(DataBuffer(bytes))

        cache.put(id.toLong(), def)
        return def
    }

    @JvmStatic
    fun setIndex(index: CacheIndex) {
        this.index = index
    }

}