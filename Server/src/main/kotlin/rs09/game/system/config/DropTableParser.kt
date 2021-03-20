package rs09.game.system.config

import core.cache.def.impl.NPCDefinition
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import rs09.ServerConstants
import rs09.game.content.global.WeightBasedTable
import rs09.game.content.global.WeightedItem
import rs09.game.system.SystemLogger
import java.io.FileReader

class DropTableParser {
    val parser = JSONParser()
    var reader: FileReader? = null
    fun load(){
        var count = 0
        reader = FileReader(ServerConstants.CONFIG_PATH + "drop_tables.json")
        val obj = parser.parse(reader) as JSONArray
        for(i in obj){
            val tab = i as JSONObject
            val ids = tab["ids"].toString().split(",")
            for(n in ids){
                val def = NPCDefinition.forId(n.toInt()).dropTables
                def ?: continue
                parseTable(tab["main"] as JSONArray, def.table, false)
                parseTable(tab["charm"] as JSONArray, def.table, false)
                parseTable(tab["default"] as JSONArray,def.table,true)
                count++
            }
        }
        SystemLogger.logInfo("Parsed $count drop tables.")
    }

    private fun parseTable(data: JSONArray, destTable: WeightBasedTable, isAlways: Boolean) {
        for(it in data){
            val item = it as JSONObject
            val id = item["id"].toString().toInt()
            val minAmount = item["minAmount"].toString().toInt()
            val maxAmount = item["maxAmount"].toString().toInt()
            val weight = item["weight"].toString().toInt()
            val newItem = WeightedItem(id,minAmount,maxAmount,weight.toDouble(),isAlways)
            destTable.add(newItem)
        }
    }
}