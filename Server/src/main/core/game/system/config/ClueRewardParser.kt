package core.game.system.config

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

import core.ServerConstants
import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem

import java.io.FileReader

class ClueRewardParser {
    companion object {
        @JvmStatic var easyTable = WeightBasedTable()
        @JvmStatic var medTable = WeightBasedTable()
        @JvmStatic var hardTable = WeightBasedTable()
        @JvmStatic var rareTable = WeightBasedTable()
    }


    val parser = JSONParser()
    var reader: FileReader? = null
    
    fun load() {
        reader = FileReader(ServerConstants.CONFIG_PATH + "clue_rewards.json")

        val rawData = parser.parse(reader) as JSONObject
        easyTable = parseClueTable(rawData["easy"] as JSONArray)
        medTable  = parseClueTable(rawData["medium"] as JSONArray)
        hardTable = parseClueTable(rawData["hard"] as JSONArray)
        rareTable = parseClueTable(rawData["rare"] as JSONArray)
    }

    private fun parseClueTable(data: JSONArray) : WeightBasedTable {
        val table = WeightBasedTable()

        for (element in data) {
            val itemData = element as JSONObject

            table.add(WeightedItem(
                itemData["id"].toString().toInt(),
                itemData["minAmount"].toString().toInt(),
                itemData["maxAmount"].toString().toInt(),
                itemData["weight"].toString().toDouble(),
                false
            ))

        }

        return table
    }
}
