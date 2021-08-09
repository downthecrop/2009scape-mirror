package rs09.game

import core.cache.def.impl.VarbitDefinition
import core.game.node.entity.player.Player
import core.net.packet.PacketRepository
import core.net.packet.context.VarbitContext
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import rs09.game.node.entity.skill.farming.FarmingPatch

/**
 * Manages the collection of a player's varps.
 * Also handles saving and loading of saved varps.
 * @author Ceikry
 */
class VarpManager(val player: Player) {
    val varps = Array(3500) { Varp(it) }

    fun get(index: Int): Varp{
        return varps[index]
    }

    fun send(index: Int){
        player.packetDispatch.sendVarp(get(index))
    }

    fun set(index: Int, value: Int){
        get(index).varbits.clear()
        get(index).varbits.add(Varbit(value,0))
        get(index).send(player)
    }

    fun setVarbit(def: VarbitDefinition, value: Int){
        get(def.configId).setVarbit(def.bitShift,value).send(player)
    }

    fun setVarbit(varbitIndex: Int, value: Int){
        PacketRepository.send(core.net.packet.out.Varbit::class.java, VarbitContext(player, varbitIndex, value))
    }

    fun flagSave(index: Int){
        get(index).save = true
    }

    fun unflagSave(index: Int){
        get(index).save = false
    }

    fun sendAllVarps(){
        for(varp in varps){
            if(varp.getValue() != 0){
                player.packetDispatch.sendVarp(varp)
            }
        }
    }

    fun getSavedVarps(): ArrayList<Varp>{
        val list = ArrayList<Varp>()
        for(varp in varps){
            if(varp.save) list.add(varp)
        }
        return list
    }

    fun save(root: JSONObject){
        val varps = JSONArray()
        for(varp in getSavedVarps()){
            val varpobj = JSONObject()
            varpobj.put("index",varp.index.toString())
            val bitArray = JSONArray()
            for(varbit in varp.varbits){
                val varbitobj = JSONObject()
                varbitobj.put("offset",varbit.offset.toString())
                varbitobj.put("value",varbit.value.toString())
                bitArray.add(varbitobj)
            }
            varpobj.put("bitArray",bitArray)
            varps.add(varpobj)
        }
        root.put("varps",varps)
    }

    fun parse(data: JSONArray){
        val patch_varps = FarmingPatch.values().map { it.varpIndex }.toIntArray()
        val bin_varps = FarmingPatch.values().map { it.varpIndex }.toIntArray()
        for(varpobj in data){
            val vobj = varpobj as JSONObject
            val index = vobj["index"].toString().toInt()
            if(patch_varps.contains(index)) continue
            if(bin_varps.contains(index)) continue
            val varp = get(index)
            val bits = vobj["bitArray"] as JSONArray
            for(vbit in bits){
                val varbit = vbit as JSONObject
                val offset = varbit["offset"].toString().toInt()
                val value = varbit["value"].toString().toInt()
                varp.setVarbit(offset,value)
                varp.save = true
            }
        }
    }
}