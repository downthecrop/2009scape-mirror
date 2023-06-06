package core.game.node.entity.player

import content.global.skill.farming.FarmingPatch
import core.game.event.VarbitUpdateEvent
import core.cache.def.impl.VarbitDefinition
import org.json.simple.JSONArray
import org.json.simple.JSONObject

/**
 * Manages the collection of a player's varps.
 * Also handles saving and loading of saved varps.
 * @author Ceikry
 */
class VarpManager(val player: Player) {
    fun save(root: JSONObject){
    }

    fun parse(data: JSONArray){
        for(varpobj in data){
            val vobj = varpobj as JSONObject
            val index = vobj["index"].toString().toInt()
            val bits = vobj["bitArray"] as JSONArray
            var total = 0
            for(vbit in bits){
                val varbit = vbit as JSONObject
                val offset = varbit["offset"].toString().toInt()
                val value = varbit["value"].toString().toInt()
                total = total or (value shl offset)
            }
            player.varpMap[index] = total
            player.saveVarp[index] = true
        }
    }
}
