package core.game.system.config

import core.ServerConstants
import core.api.log
import core.game.node.scenery.Scenery
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.tools.Log
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.FileReader

class ObjectOverrideParser {
    val parser = JSONParser()
    var reader: FileReader? = null
    fun load() {
        var count = 0
        FileReader(ServerConstants.CONFIG_PATH + "object_overrides.json").use { reader ->
            val overridelist = parser.parse(reader) as JSONArray
            for (entry in overridelist) {
                val e = entry as JSONObject

                // Mandatory fields
                val mode = e["mode"].toString()
                val id = e["id"].toString().toInt()
                val loc = Location.fromString(e["location"].toString())

                // Optional fields
                val type = e["type"]?.toString()?.toInt() ?: 10
                val direction = e["direction"]?.toString() ?: "n"

                // Now create a Scenery object and assign it to its region
                val rotation = when (direction) {
                    "ne" -> Direction.NORTH_EAST
                    "nw" -> Direction.NORTH_WEST
                    "w"  -> Direction.WEST
                    "e"  -> Direction.EAST
                    "sw" -> Direction.SOUTH_WEST
                    "se" -> Direction.SOUTH_EAST
                    "s"  -> Direction.SOUTH
                    else -> Direction.NORTH
                }.ordinal
                val obj = Scenery(id, loc, type, rotation)
                val region = RegionManager.forId(loc.regionId)
                when (mode) {
                    "remove" -> if (obj !in region.removeSceneries) region.removeSceneries.add(obj) // type and rotation are currently ignored, but this can be changed in Region.java if needed
                    "add" -> if (obj !in region.addSceneries) region.addSceneries.add(obj)
                    else -> log(this::class.java, Log.ERR, "Ignored unknown ObjectOverride mode $mode!")
                }
                count++
            }
        }
        log(this::class.java, Log.FINE, "Parsed $count object overrides.")
    }
}

