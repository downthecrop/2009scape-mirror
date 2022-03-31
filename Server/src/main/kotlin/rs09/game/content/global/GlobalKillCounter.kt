package rs09.game.content.global

import api.ShutdownListener
import api.StartupListener
import core.game.node.entity.player.Player
import rs09.game.system.SystemLogger
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import rs09.ServerConstants
import core.game.node.item.Item
import rs09.game.system.SystemLogger.logShutdown
import rs09.game.system.SystemLogger.logStartup

class GlobalKillCounter : StartupListener, ShutdownListener {
    override fun startup() {
        logStartup("Parsing Global Kill Counts")
        val file = File(ServerConstants.DATA_PATH + File.separator + "global_kill_stats.json")
        if(!file.exists()) {
            return
        }

        val reader = FileReader(file)
        val parser = JSONParser()
        try {
            val data = parser.parse(reader) as JSONObject
            val tmp_kills = data.get("kills")
            populate(kills, tmp_kills)
            val tmp_rare_drops = data.get("rare_drops")
            populate(rare_drops, tmp_rare_drops)
        } catch (e: Exception){
            SystemLogger.logErr("Failed parsing ${file.name} - stack trace below.")
            e.printStackTrace()
        }
    }

    override fun shutdown() {
        logShutdown("Saving Global Kill Counts")
        val data = JSONObject()
        data.put("kills", saveField(kills))
        data.put("rare_drops", saveField(rare_drops))
        val file = File(ServerConstants.DATA_PATH + File.separator + "global_kill_stats.json")
        FileWriter(file).use { it.write(data.toJSONString()); it.flush(); it.close() }
    }

    companion object {
        val kills: HashMap<String, HashMap<Long, Long>> = HashMap()
        val rare_drops: HashMap<String, HashMap<Long, Long>> = HashMap()

        @JvmStatic
        fun populate(field: HashMap<String, HashMap<Long, Long>>, obj: Any?) {
            if(obj != null && obj is JSONObject) {
                for((player, tmp_kc) in obj.asIterable()) {
                    if(player is String) {
                        val kc: HashMap<Long, Long> = HashMap()
                        for((npc_id, count) in (tmp_kc as JSONObject).asIterable()) {
                            kc.put(java.lang.Long.parseLong(npc_id as String), count as Long)
                        }
                        field.put(player, kc)
                    }
                }
            }
        }

        @JvmStatic
        fun saveField(field: HashMap<String, HashMap<Long, Long>>): JSONObject {
            val tmp_kills = JSONObject()
            for((player, kc) in field.asIterable()) {
                val tmp_kc = JSONObject()
                for((id, count) in kc.asIterable()) {
                    tmp_kc.put(id, count)
                }
                tmp_kills.put(player, tmp_kc)
            }
            return tmp_kills
        }

        @JvmStatic
        fun save() {

        }

        @JvmStatic
        fun incrementKills(player: Player, npc_id: Int) {
            val player_kills = kills.getOrPut(player.username, { HashMap() })
            val old_amount = player_kills.getOrElse(npc_id.toLong(), { 0 })
            player_kills.put(npc_id.toLong(), 1 + old_amount)
        }

        @JvmStatic
        fun incrementRareDrop(player: Player, item: Item) {
            val player_drops = rare_drops.getOrPut(player.username, { HashMap() })
            val old_amount = player_drops.getOrElse(item.id.toLong(), { 0 })
            player_drops.put(item.id.toLong(), item.amount + old_amount)
        }

        @JvmStatic
        fun getKills(player: Player, npc_id: Int): Long {
            return kills.getOrElse(player.username, { HashMap() }).getOrElse(npc_id.toLong(), { 0 })
        }

        @JvmStatic
        fun getKills(player: Player, npc_ids: IntArray): Long {
            var sum: Long = 0
            for(npc_id in npc_ids) {
                sum += getKills(player, npc_id)
            }
            return sum
        }

        @JvmStatic
        fun getRareDrops(player: Player, item_id: Int): Long {
            return rare_drops.getOrElse(player.username, { HashMap() }).getOrElse(item_id.toLong(), { 0 })
        }
    }
}
