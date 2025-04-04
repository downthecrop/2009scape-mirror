package core.game.system.config

import core.ServerConstants
import core.api.log
import core.tools.Log
import core.tools.SystemLogger
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.FileReader

class DoorConfigLoader {
    companion object {
        val DOORS = hashMapOf<Int, Door>()

        fun forId(id: Int): Door?{
            return DOORS[id]
        }
    }
    val parser = JSONParser()
    var reader: FileReader? = null

    fun load(){
        var count = 0
        reader = FileReader(ServerConstants.CONFIG_PATH + "door_configs.json")
        var configs = parser.parse(reader) as JSONArray
        for(config in configs){
            val e = config as JSONObject
            val door = Door(e["id"].toString().toInt())
            door.replaceId = e["replaceId"].toString().toInt()
            door.isFence = e["fence"].toString().toBoolean()
            door.isMetal = e["metal"].toString().toBoolean()
            door.isAutoWalk = e["autowalk"]?.toString()?.toBoolean() ?: false
            DOORS[door.id] = door
            val replacedDoor = Door(door.replaceId)
            replacedDoor.replaceId = door.id
            replacedDoor.isFence = door.isFence
            replacedDoor.isMetal = door.isMetal
            replacedDoor.isAutoWalk = door.isAutoWalk
            DOORS[door.replaceId] = replacedDoor
            count++
        }
        log(this::class.java, Log.FINE,  "Parsed $count door configs.")
    }


    class Door
    /**
     * Constructs a new `DoorManager` `Object`.
     */(
            /**
             * The door's object id.
             */
            val id: Int) {
        /**
         * Gets the id.
         * @return The id.
         */

        /**
         * Gets the replaceId.
         * @return The replaceId.
         */
        /**
         * Sets the replaceId.
         * @param replaceId The replaceId to set.
         */
        /**
         * The door's replace object id.
         */
        var replaceId = 0

        /**
         * Gets the fence.
         * @return The fence.
         */
        /**
         * Sets the fence.
         * @param fence The fence to set.
         */
        /**
         * If the door is closed.
         */
        var isFence = false

        /**
         * Gets the autoWalk.
         * @return The autoWalk.
         */
        /**
         * Sets the autoWalk.
         * @param autoWalk The autoWalk to set.
         */
        /**
         * If the player should automatically walk through it.
         */
        var isAutoWalk = false

        /**
         * Is the door metal?
         */
        var isMetal = false

        var questRequirement = ""

    }
}
