package rs09.game.node.entity.skill.construction

import core.game.node.entity.skill.construction.RoomProperties

class RoomTemplate(properties: RoomProperties) {
    var hotspots: MutableList<Hotspot> = ArrayList<Hotspot>()

    init {
        for(property in properties.hotspots){
            hotspots.add(Hotspot(property.hotspot,property.chunkX,property.chunkY,property.chunkX2,property.chunkY2))
        }
    }

    fun rotate(clockwise: Boolean){
        hotspots = hotspots.map{
            if(!clockwise) {
                val newChunkX = it.chunkY
                val newChunkY = 7 - it.chunkX
                val newChunkX2 = it.chunkY2
                val newChunkY2 = 7 - it.chunkX2
                Hotspot(it.hotspot,newChunkX,newChunkY,newChunkX2,newChunkY2)
            } else {
                val newChunkY = it.chunkX
                val newChunkX = 7 - it.chunkY
                val newChunkY2 = it.chunkX2
                val newChunkX2 = 7 - it.chunkY2
                Hotspot(it.hotspot,newChunkX,newChunkY,newChunkX2,newChunkY2)
            }
        }.toMutableList()
    }



}