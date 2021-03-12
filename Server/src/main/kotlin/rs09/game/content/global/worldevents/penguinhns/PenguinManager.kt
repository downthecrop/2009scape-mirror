package rs09.game.content.global.worldevents.penguinhns

import core.game.node.entity.npc.NPC
import rs09.game.system.SystemLogger
import core.game.world.map.Location
import java.util.*

class PenguinManager{
    companion object {
        var penguins = ArrayList<PenguinSpawner.Penguin>()
        var npcs = ArrayList<NPC>()
        val spawner = PenguinSpawner()
        var tagMapping = HashMap<Location,ArrayList<String>>()
    }

    fun rebuildVars() {
        for(p in npcs){
            p.clear()
            p.isActive = false
        }
        npcs.clear()
        penguins  = spawner.spawnPenguins(6)
        tagMapping.clear()
        for(p in penguins){
            tagMapping.put(p.loc, ArrayList())
        }
    }

    fun log(message: String){
        SystemLogger.logInfo("[Penguins] $message")
    }
}