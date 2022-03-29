package rs09.game.content.global.worldevents.penguinhns

import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import rs09.game.system.SystemLogger
import core.game.world.map.Location
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import rs09.ServerStore.Companion.toJSONArray
import java.util.*

class PenguinManager{
    companion object {
        var penguins: MutableList<Int> = ArrayList<Int>()
        var npcs = ArrayList<NPC>()
        val spawner = PenguinSpawner()
        var tagMapping: MutableMap<Int,JSONArray> = HashMap()

        fun registerTag(player: Player, location: Location){
            val ordinal = Penguin.forLocation(location)?.ordinal ?: -1
            if(tagMapping[ordinal] == null){
                tagMapping[ordinal] = JSONArray()
            }
            tagMapping[ordinal]?.add(player.username.toLowerCase())
            updateStoreFile()
        }

        fun hasTagged(player: Player, location: Location): Boolean{
            return tagMapping[Penguin.forLocation(location)?.ordinal]?.contains(player.username.toLowerCase()) ?: false
        }

        private fun updateStoreFile(){
            val jsonTags = JSONArray()
            tagMapping.forEach { (ordinal,taggers) ->
                SystemLogger.logInfo("$ordinal - ${taggers.first()}")
                val tag = JSONObject()
                tag["ordinal"] = ordinal
                tag["taggers"] = taggers
                jsonTags.add(tag)
            }

            PenguinHNSEvent.getStoreFile()["tag-mapping"] = jsonTags
        }
    }

    fun rebuildVars() {
        if(PenguinHNSEvent.getStoreFile().isEmpty()) {
            penguins = spawner.spawnPenguins(10)
            PenguinHNSEvent.getStoreFile()["spawned-penguins"] = penguins.toJSONArray()
            for (p in penguins) {
                tagMapping.put(p, JSONArray())
            }
        } else {
            val spawnedOrdinals = (PenguinHNSEvent.getStoreFile()["spawned-penguins"] as JSONArray).map { it.toString().toInt() }
            spawner.spawnPenguins(spawnedOrdinals)

            val storedTags = (PenguinHNSEvent.getStoreFile()["tag-mapping"] as? JSONArray)?.map { jRaw ->
                val jObj = jRaw as JSONObject
                jObj["ordinal"].toString().toInt() to (jObj["taggers"] as JSONArray)
            }?.toMap()?.toMutableMap() ?: HashMap<Int,JSONArray>()

            tagMapping = storedTags
            penguins = spawnedOrdinals.toMutableList()
        }
    }

    fun log(message: String){
        SystemLogger.logInfo("[Penguins] $message")
    }
}