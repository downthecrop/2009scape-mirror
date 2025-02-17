package core.game.system.config

import core.ServerConstants
import core.api.log
import core.tools.Log
import core.tools.SystemLogger
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.FileReader
import kotlin.collections.HashMap

class XteaParser {
    companion object{
        val REGION_XTEA = HashMap<Int,IntArray>()
        private val DEFAULT_REGION_KEYS = intArrayOf(0, 0, 0, 0)
        fun getRegionXTEA(regionId: Int): IntArray { //Uses the xtea's from the sql to unlock regions
            return  REGION_XTEA.getOrDefault(regionId, DEFAULT_REGION_KEYS)
        }
    }
    val parser = JSONParser()
    var reader: FileReader? = null


    fun load() {
        var count = 0
        reader = FileReader(ServerConstants.CONFIG_PATH + "xteas.json")
        val obj = parser.parse(reader) as JSONObject
        val configlist = obj["xteas"] as JSONArray
        for(config in configlist){
            val e = config as JSONObject
            val id = e["regionId"].toString().toInt()
            val keys = e["keys"].toString().split(",").map(String::toInt)
            REGION_XTEA[id] = intArrayOf(keys[0],keys[1],keys[2],keys[3])
            count++
        }
        log(this::class.java, Log.FINE,  "Parsed $count region keys.")
    }
}