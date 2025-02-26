package core.game.system.config

import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import core.ServerConstants
import core.api.log
import core.game.world.GameWorld
import core.game.world.repository.Repository
import core.tools.Log
import java.io.*

class GroundSpawnLoader {
    val parser = JSONParser()
    var reader: FileReader? = null

    fun load() {
        var count = 0
        reader = FileReader(ServerConstants.CONFIG_PATH + "ground_spawns.json")
        var configs = parser.parse(reader) as JSONArray
        for(config in configs){
            try {
                val e = config as JSONObject
                val datas = e["loc_data"].toString().split("-")
                val id = e["item_id"].toString().toInt()
                for(d in datas){
                    if (d.isNullOrEmpty()) continue
                    val tokens = d.replace("{", "").replace("}", "").split(",".toRegex()).toTypedArray()
                    val spawn = GroundSpawn(tokens[4].toInt(), Item(id, tokens[0].toInt()), Location(Integer.valueOf(tokens[1]), Integer.valueOf(tokens[2]), Integer.valueOf(tokens[3])))
                    spawn.init()
                    count++
                }
            } catch (e: Exception) {
                val sw = StringWriter()
                val pw = PrintWriter(sw)
                e.printStackTrace(pw)
                log(this::class.java, Log.ERR, "Error parsing config entry ${config.toString()}: $sw")
            }
        }
        log(this::class.java, Log.FINE,  "Initialized $count ground items.")
    }
    class GroundSpawn
    /**
     * Constructs a new `GroundItemParser` `Object`.
     * @param item the item.
     * @param location the location.
     */(
            /**
             * Represents the rate at which a ground item will respawn.
             */
            var respawnRate: Int, item: Item?, location: Location?) : GroundItem(item, location) {
        /**
         * Gets the respawn rate.
         * @return the rate.
         */

        override fun toString(): String {
            return "GroundSpawn [name=" + getName() + ", respawnRate=" + respawnRate + ", loc=" + getLocation() + "]"
        }

        /**
         * Method used to initialize this spawn.
         */
        fun init(): GroundItem {
            return GroundItemManager.create(this)
        }

        override fun isActive(): Boolean {
            return true
        }

        override fun isPrivate(): Boolean {
            return false
        }

        override fun isAutoSpawn(): Boolean {
            return true
        }

        override fun respawn() {
            GameWorld.Pulser.submit(object : Pulse(respawnDuration) {
                override fun pulse(): Boolean {
                    GroundItemManager.create(this@GroundSpawn)
                    return true
                }
            })
        }

        /**
         * Method used to set the respawn rate.
         * @param min the min.
         * @param max the max.
         */
        fun setRespawnRate(min: Int, max: Int) {
            respawnRate = min or max shl 16
        }

        /**
         * Gets the current respawn duration (in ticks).
         * @return The respawn duration.
         */
        val respawnDuration: Int
            get() {
                val minimum = respawnRate and 0xFFFF
                val maximum = respawnRate shr 16 and 0xFFFF
                val playerRatio = ServerConstants.MAX_PLAYERS / Repository.players.size.toDouble()
                return (minimum + (maximum - minimum) / playerRatio).toInt()
            }

    }
}
