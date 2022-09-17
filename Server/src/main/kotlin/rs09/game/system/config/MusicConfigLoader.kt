package rs09.game.system.config

import core.cache.def.impl.DataMap
import rs09.ServerConstants
import core.game.node.entity.player.link.music.MusicEntry
import core.game.node.entity.player.link.music.MusicZone
import rs09.game.system.SystemLogger
import core.game.world.map.RegionManager
import core.game.world.map.zone.ZoneBorders
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.FileReader

class MusicConfigLoader {
    //1351 -> buttonID:songID
    //1345 -> buttonID:songName (capitalized)
    //1347 -> buttonID:songName (lowercase)

    val parser = JSONParser()
    var reader: FileReader? = null
    fun load(){
        var count = 0
        reader = FileReader(ServerConstants.CONFIG_PATH + "music_configs.json")
        var configs = parser.parse(reader) as JSONArray

        val songs = DataMap.get(1351)
        val names = DataMap.get(1345)

        for((index, songId) in songs.dataStore)
        {
            val entry = MusicEntry(songId as Int, names.getString(index as Int), index)
            MusicEntry.getSongs().putIfAbsent(songId, entry)
        }

        for(config in configs){
            val e = config as JSONObject
            val musicId = Integer.parseInt(e["id"].toString())
            val string = e["borders"].toString()
            val borderArray = string.split("-")
            var tokens: Array<String>? = null
            var borders: ZoneBorders? = null
            for (border in borderArray) {
                if(border.isEmpty()){
                    continue
                }
                tokens = border.replace("{", "").replace("}", "").split(",").toTypedArray()
                borders = ZoneBorders(tokens[0].toInt(), tokens[1].toInt(), tokens[2].toInt(), tokens[3].toInt())
                if (border.contains("[")) { //no exception borders
                    var exceptions: String? = ""
                    for (i in 4 until tokens.size) {
                        exceptions += tokens[i] + if (!tokens[i].contains("]~")) "," else if (tokens[i].contains("[")) "," else ""
                    }
                    tokens = exceptions!!.split("~".toRegex()).toTypedArray()
                    var e: Array<String>? = null
                    for (exception in tokens) {
                        e = exception.replace("[", "").replace("]", "").split(",".toRegex()).toTypedArray()
                        borders.addException(ZoneBorders(e[0].toInt(), e[1].toInt(), e[2].toInt(), e[3].toInt()))
                    }
                    e = null
                    exceptions = null
                }
                val zone = MusicZone(musicId, borders)
                for (id in borders.getRegionIds()) {
                    RegionManager.forId(id!!).musicZones.add(zone)
                }
            }
            count++
        }
        SystemLogger.logInfo(this::class.java, "Parsed $count music configs.")
    }
}