package core.game.system.config

import core.ServerConstants
import core.cache.def.impl.NPCDefinition
import core.game.node.item.WeightedChanceItem
import core.game.system.SystemLogger
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.FileReader
import java.util.*

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
                val mainTable: List<WeightedChanceItem> = parseTable(tab["main"] as JSONArray)
                val charmTable: List<WeightedChanceItem> = parseTable(tab["charm"] as JSONArray)
                val defaultTable: List<WeightedChanceItem> = parseTable(tab["default"] as JSONArray)
                def.mainTable.addAll(mainTable)
                def.charmTable.addAll(charmTable)
                def.defaultTable.addAll(defaultTable)
            }
        }
        SystemLogger.logInfo("Parsed $count drop tables.")
    }

    private fun parseTable(data: JSONArray): List<WeightedChanceItem> {
        val table: MutableList<WeightedChanceItem> = ArrayList()
        for(it in data){
            val item = it as JSONObject
            val id = item["id"].toString().toInt()
            val minAmount = item["minAmount"].toString().toInt()
            val maxAmount = item["maxAmount"].toString().toInt()
            val weight = item["weight"].toString().toInt()
            val newItem = WeightedChanceItem(id,minAmount,maxAmount,weight)
            table.add(newItem)
        }
        return table
    }
}