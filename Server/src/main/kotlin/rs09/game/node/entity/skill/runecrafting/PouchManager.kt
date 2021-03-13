package rs09.game.node.entity.skill.runecrafting

import core.game.container.Container
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.rs09.consts.Items
import rs09.tools.stringtools.colorize

/**
 * A class for managing rune pouches.
 * @param player the player this manager instance belongs to.
 * @author Ceikry
 */
class PouchManager(val player: Player) {

    val pouches = mapOf(
            Items.SMALL_POUCH_5509 to RCPouch(3,1),
            Items.MEDIUM_POUCH_5510 to RCPouch(6,25),
            Items.LARGE_POUCH_5512 to RCPouch(9,50),
            Items.GIANT_POUCH_5514 to RCPouch(12,75)
    )

    /**
     * Method to add essence to a pouch
     * @param pouchId the id of the pouch we are adding to
     * @param amount the amount of essence to add
     * @param essence the ID of the essence item we are trying to add
     * @author Ceikry
     */
    fun addToPouch(pouchId: Int, amount: Int, essence: Int){
        if(!checkRequirement(pouchId)){
            player.sendMessage(colorize("%RYou lack the required level to use this pouch."))
            return
        }
        var amt = amount
        val pouch = pouches[pouchId]
        val otherEssence = when(essence){
            Items.RUNE_ESSENCE_1436 -> Items.PURE_ESSENCE_7936
            Items.PURE_ESSENCE_7936 -> Items.RUNE_ESSENCE_1436
            else -> 0
        }
        pouch ?: return
        if(amount > pouch.container.freeSlots()){
            amt = pouch.container.freeSlots()
        }
        if(amt == 0){
            player.sendMessage("This pouch is already full.")
        }
        if(pouch.container.contains(otherEssence,1)){
            player.sendMessage("You can only store one type of essence in each pouch.")
            return
        }
        player.inventory.remove(Item(essence,amt))
        pouch.container.add(Item(essence,amt))
    }


    /**
     * Method to withdraw rune essence from a pouch.
     * @param pouchId the item ID of the pouch to withdraw from
     * @author Ceikry
     */
    fun withdrawFromPouch(pouchId: Int){
        val pouch = pouches[pouchId]
        pouch ?: return
        val playerFree = player.inventory.freeSlots()
        var amount = pouch.currentCap - pouch.container.freeSlots()
        if (amount > playerFree) amount = playerFree
        player.debug("$amount")
        if(amount == 0) return
        val essence = Item(pouch.container.get(0).id,amount)
        pouch.container.remove(essence)
        pouch.container.shift()
        player.inventory.add(essence)
        if(pouch.charges-- <= 0){
            pouch.currentCap -= when(pouchId){
                5510 -> 1
                5512 -> 2
                5514 -> 3
                else -> 0
            }
            if(pouch.currentCap <= 0){
                player.inventory.remove(Item(pouchId))
                player.inventory.add(Item(pouchId + 1))
                player.sendMessage(colorize("%RYour ${Item(pouchId).name} has degraded completely."))
            }
            pouch.remakeContainer()
            pouch.charges = 10
            if(pouchId != 5509) {
                player.sendMessage(colorize("%RYour ${Item(pouchId).name.toLowerCase()} has degraded slightly from use."))
            }
        }

    }


    /**
     * Method to save pouches to a root JSONObject
     * @param root the JSONObject we are adding the "pouches" JSONArray to
     * @author Ceikry
     */
    fun save(root: JSONObject){
        val pouches = JSONArray()

        for(i in this.pouches){
            val pouch = JSONObject()
            pouch.put("id",i.key.toString())
            val items = JSONArray()
            for(item in i.value.container.toArray()){
                item ?: continue
                val it = JSONObject()
                it.put("itemId",item.id.toString())
                it.put("amount",item.amount.toString())
                items.add(it)
            }
            pouch.put("container",items)
            pouch.put("charges",i.value.charges.toString())
            pouch.put("currentCap",i.value.currentCap.toString())
            pouches.add(pouch)
        }
        root.put("pouches",pouches)
    }


    /**
     * Method to parse save data from a JSONArray
     * @param data the JSONArray that contains the data to parse
     * @author Ceikry
     */
    fun parse(data: JSONArray){
        for(e in data){
            val pouch = e as JSONObject
            val id = pouch["id"].toString().toInt()
            val p = pouches[id]
            p ?: return
            val charges = pouch["charges"].toString().toInt()
            val currentCap = pouch["currentCap"].toString().toInt()
            p.charges = charges
            p.currentCap = currentCap
            p.remakeContainer()
            for(i in pouch["container"] as JSONArray){
                val it = i as JSONObject
                it["itemId"] ?: continue
                val item = it["itemId"].toString().toInt()
                val amount = it["amount"].toString().toInt()
                p.container.add(Item(item,amount))
            }
        }
    }


    /**
     * Method for checking the level requirement for a given pouch.
     * @param pouchId the item ID of the pouch to check
     * @author Ceikry
     */
    fun checkRequirement(pouchId: Int): Boolean{
        val p = pouches[pouchId]
        p ?: return false
        return player.skills.getLevel(Skills.RUNECRAFTING) >= p.levelRequirement
    }


    /**
     * Method for sending the player a message about how much space is left in a pouch
     * @param pouchId the item ID of the pouch to check
     * @author Ceikry
     */
    fun checkAmount(pouchId: Int){
        val p = pouches[pouchId]
        p ?: return
        player.sendMessage("This pouch has space for ${p.container.freeSlots()} more essence.")
    }


    fun isDecayedPouch(pouchId: Int): Boolean{
        if(pouchId == 5510) return false
        return pouches[pouchId - 1] != null
    }

    /**
     * A class that represents a runecrafting pouch.
     * @author Ceikry
     */
    class RCPouch(val capacity: Int, val levelRequirement: Int){
        var container = Container(capacity)
        var currentCap = capacity
        var charges = 10
        fun remakeContainer(){
            this.container = Container(currentCap)
        }
    }
}