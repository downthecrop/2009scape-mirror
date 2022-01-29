package rs09.game.node.entity.skill.farming

import api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.audio.Audio
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.RandomFunction
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.rs09.consts.Items
import rs09.game.system.SystemLogger
import java.util.concurrent.TimeUnit

class CompostBin(val player: Player, val bin: CompostBins) {
    private var items = ArrayList<Int>()
    var isSuperCompost = true
    var isTomatoes = true
    var isClosed = false
    var finishedTime = 0L
    var isFinished = false

    fun isFull() : Boolean {
        return items.size == 15
    }

    fun close() {
        isClosed = true
        player.audioManager.send(2428)          
        finishedTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(RandomFunction.random(35,50).toLong())
        updateBit()
    }

    fun open(){
        isClosed = false
        player.audioManager.send(2429)        
        updateBit()
    }

    fun takeItem(): Item?{
        if(items.isEmpty()) return null
        val item = items[0]
        items.remove(item)
        if(items.isEmpty()){
            isFinished = false
            finishedTime = 0L
            isTomatoes = true
            isSuperCompost = true
            isClosed = false
        }
        player.audioManager.send(Audio (2443, 1, 1))
        updateBit()
        if(isSuperCompost) rewardXP(player, Skills.FARMING, 8.5)
        else rewardXP(player, Skills.FARMING, 4.5)
        return Item(item)
    }

    fun isReady(): Boolean {
        return System.currentTimeMillis() > finishedTime && finishedTime != 0L
    }

    fun checkSuperCompostItem(id: Int): Boolean{
        return when (id){
            Items.WATERMELON_5982,
            Items.PINEAPPLE_2114,
            Items.CALQUAT_FRUIT_5980,
            Items.OAK_ROOTS_6043,
            Items.WILLOW_ROOTS_6045,
            Items.MAPLE_ROOTS_6047,
            Items.YEW_ROOTS_6049,
            Items.MAGIC_ROOTS_6051,
            Items.COCONUT_5974,
            Items.COCONUT_SHELL_5978,
            Items.PAPAYA_FRUIT_5972,
            Items.JANGERBERRIES_247,
            Items.WHITE_BERRIES_239,
            Items.POISON_IVY_BERRIES_6018,
            Items.CLEAN_TOADFLAX_2998,
            Items.CLEAN_AVANTOE_261,
            Items.CLEAN_KWUARM_263,
            Items.CLEAN_CADANTINE_265,
            Items.CLEAN_DWARF_WEED_267,
            Items.CLEAN_TORSTOL_269,
            Items.CLEAN_LANTADYME_2481,
            Items.CLEAN_SNAPDRAGON_3000,
            Items.GRIMY_TOADFLAX_3049,
            Items.GRIMY_KWUARM_213,
            Items.GRIMY_AVANTOE_211,
            Items.GRIMY_TORSTOL_219,
            Items.GRIMY_DWARF_WEED_217,
            Items.GRIMY_LANTADYME_2485,
            Items.GRIMY_SNAPDRAGON_3051,
            Items.GRIMY_CADANTINE_215 -> true
            else -> false
        }
    }

    fun addItem(item: Int){
        if(!isFull()) {
            items.add(item)
            if (!checkSuperCompostItem(item)) {
                isSuperCompost = false
            }
            if(item != Items.TOMATO_1982) isTomatoes = false
        }
        updateBit()
    }

    fun addItem(item: Item){
        val remaining = 15 - items.size
        val amount = if(item.amount > remaining){
            remaining
        } else item.amount
        for(i in 0 until amount) {

            player.audioManager.send(2441)
            addItem(item.id)
        }
    }

    fun updateBit(){
        player.varpManager.get(bin.varpIndex).clearBitRange(bin.varpOffest,bin.varpOffest + 7)
        if(items.isNotEmpty()) {
            if (isFinished) {
                player.varpManager.get(bin.varpIndex).setVarbit(bin.varpOffest + 1, if(items.size == 15) 15 else 14)
                if (isTomatoes) player.varpManager.get(bin.varpIndex).setVarbit(bin.varpOffest + 7, 1)
                else if (isSuperCompost) player.varpManager.get(bin.varpIndex).setVarbit(bin.varpOffest + 5, 1)
            } else {
                player.varpManager.get(bin.varpIndex).setVarbit(bin.varpOffest, items.size)
                if (isTomatoes) player.varpManager.get(bin.varpIndex).setVarbit(bin.varpOffest + 7, 1)
            }
            if (isClosed) player.varpManager.get(bin.varpIndex).setVarbit(bin.varpOffest + 6, 1)
        }
        SystemLogger.logAlert("Sending " + player.varpManager.get(bin.varpIndex).getValue())
        player.varpManager.get(bin.varpIndex).send(player)
    }

    fun save(root: JSONObject){
        val binObject = JSONObject()
        binObject.put("isSuper",this.isSuperCompost)
        val items = JSONArray()
        for(id in this.items){
            items.add(id)
        }
        binObject.put("items",items)
        binObject.put("finishTime",finishedTime)
        binObject.put("isTomato",isTomatoes)
        binObject.put("isClosed",isClosed)
        binObject.put("isFinished",isFinished)
        root.put("binData",binObject)
    }

    fun parse(_data: JSONObject){
        val isSuper = if(_data.containsKey("isSuper")) (_data["isSuper"] as Boolean) else true
        if(_data.containsKey("items")){
            (_data["items"] as JSONArray).forEach {
                addItem(it.toString().toInt())
            }
        }
        if(_data.containsKey("finishTime")) finishedTime = _data["finishTime"].toString().toLong()
        if(_data.containsKey("isTomato")) isTomatoes = _data["isTomato"] as Boolean
        if(_data.containsKey("isClosed")) isClosed = _data["isClosed"] as Boolean
        if(_data.containsKey("isFinished")) isFinished = _data["isFinished"] as Boolean
        updateBit()
    }

    fun finish(){
        if(isTomatoes) items = items.map { Items.ROTTEN_TOMATO_2518 } as ArrayList<Int>
        else if(isSuperCompost) items = items.map { Items.SUPERCOMPOST_6034 } as ArrayList<Int>
        else items = items.map { Items.COMPOST_6032 } as ArrayList<Int>
        isFinished = true
    }

    fun convert(){
        if(!isSuperCompost){
            items = items.map { Items.SUPERCOMPOST_6034 } as ArrayList<Int>
            isSuperCompost = true
            updateBit()
        }
    }
}