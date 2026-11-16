package core.game.system.config

import core.ServerConstants
import core.api.log
import core.tools.Log
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

    fun load() {
        var count = 0
        reader = FileReader(ServerConstants.CONFIG_PATH + "door_configs.json")
        val configs = parser.parse(reader) as JSONArray
        for (config in configs) {
            val e = config as JSONObject
            val door = Door(e["id"].toString().toInt())
            door.replaceId = e["replaceId"].toString().toInt()
            door.isFence = e["fence"].toString().toBoolean()
            door.isMetal = e["metal"].toString().toBoolean()
            door.isAutowalk = e["autowalk"]?.toString()?.toBoolean() ?: false
            door.turns = e["turns"]?.toString()?.toBoolean() ?: true
            door.moves = e["moves"]?.toString()?.toBoolean() ?: true
            door.extraRotation = e["extraRotation"]?.toString()?.toInt() ?: 0
            door.extraXStep = e["extraXStep"]?.toString()?.toInt() ?: 0
            door.extraMove = e["extraMove"]?.toString()?.toInt() ?: 0
            door.ignoreSecondDoor = e["ignoreSecondDoor"]?.toString()?.toBoolean() ?: false
            door.sound = e["sound"]?.toString()?.toBoolean() ?: true
            DOORS[door.id] = door
            val replacedDoor = Door(door.replaceId)
            replacedDoor.replaceId = door.id
            replacedDoor.isFence = door.isFence
            replacedDoor.isMetal = door.isMetal
            replacedDoor.isAutowalk = door.isAutowalk
            replacedDoor.turns = door.turns
            replacedDoor.moves = door.moves
            replacedDoor.extraRotation = door.extraRotation
            replacedDoor.extraXStep = door.extraXStep
            replacedDoor.extraMove = door.extraMove
            replacedDoor.ignoreSecondDoor = door.ignoreSecondDoor
            replacedDoor.sound = door.sound
            DOORS[door.replaceId] = replacedDoor
            count++
        }
        log(this::class.java, Log.FINE,  "Parsed $count door configs.")
    }


    class Door(val id: Int) {
        /**
         * The opened-door scenery id
         */
        var replaceId = 0

        /**
         * Is the door a fence?
         */
        var isFence = false

        /**
         * If the player should automatically walk through it.
         */
        var isAutowalk = false

        /**
         * Is the door metal? Only changes the sound.
         */
        var isMetal = false

        /**
         * Does the door turn when opened?
         */
        var turns = true

        /**
         * Does the door move when opened?
         */
        var moves = true

        /**
         * A rotation correction to add on top of the computed pivot rotation, in case this door needs to swing in a
         * special way.
         */
        var extraRotation = 0

        /**
         * Whether the player needs to take an extra X step. This only exists for the plague gate, which walks you west
         * an extra tile but not east (https://youtu.be/ZxXW0x86Ztc?t=154).
         */
        var extraXStep = 0

        /**
         * Whether the door itself needs to move an extra step relative to its rotation. This allows double doors to
         * open the other way.
         */
        var extraMove = 0

        /**
         * Whether to ignore second doors. Used in e.g. the Maze random, which has a lot of "potential" second doors for
         * purposes of our second-door-finding code, but which are all incorrect.
         */
        var ignoreSecondDoor = false

        /**
         * Whether the door has opening/closing sounds.
         */
        var sound = true
    }
}
