package rs09.game.system.config

import rs09.ServerConstants
import core.game.component.ComponentDefinition
import rs09.game.system.SystemLogger
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.FileReader

class InterfaceConfigParser{
    val parser = JSONParser()
    var reader: FileReader? = null
    fun load() {
        var count = 0
        reader = FileReader(ServerConstants.CONFIG_PATH + "interface_configs.json")
        val configlist = parser.parse(reader) as JSONArray
        for(config in configlist){
            val e = config as JSONObject
            val id = e["id"].toString().toInt()
            if (ComponentDefinition.getDefinitions().containsKey(id)) {
                ComponentDefinition.getDefinitions()[id]!!.parse(e["interfaceType"].toString(),e["walkable"].toString(),e["tabIndex"].toString())
            }
            ComponentDefinition.getDefinitions()[id] = ComponentDefinition().parse(e["interfaceType"].toString(),e["walkable"].toString(),e["tabIndex"].toString())
            count++
        }
        SystemLogger.logInfo("Parsed $count interface configs.")
    }
}