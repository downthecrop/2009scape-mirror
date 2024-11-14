package content.global.skill.runecrafting

import core.api.*
import core.game.container.Container
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.rs09.consts.Items

/**
 * A class for managing rune pouches.
 * @param player the player this manager instance belongs to.
 * @author Ceikry, Player Name
 */
class PouchManager(val player: Player) {
    val pouches = mapOf(
            Items.SMALL_POUCH_5509  to RCPouch(3, 3,   1),
            Items.MEDIUM_POUCH_5510 to RCPouch(6, 264, 25),
            Items.LARGE_POUCH_5512  to RCPouch(9, 186, 50),
            Items.GIANT_POUCH_5514  to RCPouch(12,140, 75)
    )

    /**
     * Method to add essence to a pouch
     * @param itemId the item ID of the pouch we are adding to
     * @param amount the amount of essence to add
     * @param essence the ID of the essence item we are trying to add
     * @author Ceikry, Player Name
     */
    fun addToPouch(itemId: Int, amount: Int, essence: Int) {
        val pouchId = if (isDecayedPouch(itemId)) itemId - 1 else itemId
        if (!checkRequirement(pouchId)) {
            sendMessage(player, "You lack the required level to use this pouch.")
            return
        }
        var amt = amount
        val pouch = pouches[pouchId]
        val otherEssence = when(essence) {
            Items.RUNE_ESSENCE_1436 -> Items.PURE_ESSENCE_7936
            Items.PURE_ESSENCE_7936 -> Items.RUNE_ESSENCE_1436
            else -> 0
        }
        pouch ?: return
        if (amount > pouch.container.freeSlots()) {
            amt = pouch.container.freeSlots()
        }
        if (amt == pouch.container.freeSlots()) {
            sendMessage(player, "Your pouch is full.") //https://www.youtube.com/watch?v=wbYtRwODKTo
        }
        if (pouch.container.contains(otherEssence,1)) {
            sendMessage(player, "You can only store one type of essence in each pouch.")
            return
        }

        var disappeared = false
        if (itemId != Items.SMALL_POUCH_5509) {
            pouch.charges -= amt
        }
        if (pouch.charges <= 0) {
            pouch.currentCap -= when (pouchId) {
                Items.MEDIUM_POUCH_5510 -> 1
                Items.LARGE_POUCH_5512  -> 2
                Items.GIANT_POUCH_5514  -> 3
                else /*small pouch*/    -> 0
            }
            if (pouch.currentCap <= 0) {
                // The pouch will disappear: https://runescape.wiki/w/Runecrafting_pouches?oldid=708494, https://oldschool.runescape.wiki/w/Essence_pouch
                // "Degraded pouches will continue to degrade and lose essence capacity until they disappear or are repaired." implies that this is the end result of a gradual decay process
                if (removeItem(player, itemId)) {
                    disappeared = true
                    sendMessage(player, "Your pouch has degraded completely.")
                    // Reset the pouch for when the player obtains a new one
                    pouch.currentCap = pouch.capacity
                    pouch.charges = pouch.maxCharges
                    pouch.remakeContainer()
                }
            } else {
                if (!isDecayedPouch(itemId)) {
                    val slot = player.inventory.getSlot(Item(itemId))
                    replaceSlot(player, slot, Item(itemId + 1))
                }
                sendMessage(player, "Your pouch has decayed through use.") //https://www.youtube.com/watch?v=FUcPYrgPUlQ
                pouch.charges = 9 * pouch.currentCap //implied by multiple contemporaneous sources, quantified only by https://oldschool.runescape.wiki/w/Large_pouch
                pouch.remakeContainer()
                if (amt > pouch.currentCap) {
                    amt = pouch.currentCap
                }
            }
        }
        val essItem = Item(essence, amt)
        if (!disappeared && removeItem(player, essItem)) {
            pouch.container.add(essItem)
        }
    }

    /**
     * Method to withdraw rune essence from a pouch.
     * @param itemId the item ID of the pouch to withdraw from
     * @author Ceikry, Player Name
     */
    fun withdrawFromPouch(itemId: Int) {
        val pouchId = if (isDecayedPouch(itemId)) itemId - 1 else itemId
        val pouch = pouches[pouchId]
        pouch ?: return
        val playerFree = freeSlots(player)
        var amount = pouch.currentCap - pouch.container.freeSlots()
        if (amount > playerFree) {
            amount = playerFree
        } else {
            sendMessage(player, "Your pouch has no essence left in it.") //https://www.youtube.com/watch?v=wbYtRwODKTo
            if (amount == 0) {
                return
            }
        }
        val essence = Item(pouch.container.get(0).id, amount)
        pouch.container.remove(essence)
        pouch.container.shift()
        addItem(player, essence.id, essence.amount)
    }

    /**
     * Method to save pouches to a root JSONObject
     * @param root the JSONObject we are adding the "pouches" JSONArray to
     * @author Ceikry
     */
    fun save(root: JSONObject) {
        val pouches = JSONArray()

        for(i in this.pouches) {
            val pouch = JSONObject()
            pouch.put("id",i.key.toString())
            val items = JSONArray()
            for(item in i.value.container.toArray()) {
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
    fun parse(data: JSONArray) {
        for (e in data){
            val pouch = e as JSONObject
            val id = pouch["id"].toString().toInt()
            val p = pouches[id]
            p ?: return
            val charges = pouch["charges"].toString().toInt()
            val currentCap = pouch["currentCap"].toString().toInt()
            p.charges = charges
            p.currentCap = currentCap
            p.remakeContainer()
            for (i in pouch["container"] as JSONArray) {
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
    fun checkRequirement(pouchId: Int): Boolean {
        val p = pouches[pouchId]
        p ?: return false
        return player.skills.getLevel(Skills.RUNECRAFTING) >= p.levelRequirement
    }

    /**
     * Method for sending the player a message about how much space is left in a pouch
     * @param itemId the item ID of the pouch to check
     * @author Ceikry, Player Name
     */
    fun checkAmount(itemId: Int) {
        val pouchId = if (isDecayedPouch(itemId)) itemId - 1 else itemId
        val p = pouches[pouchId]
        p ?: return
        player.sendMessage("This pouch has space for ${p.container.freeSlots()} more essence.")
    }

    fun isDecayedPouch(pouchId: Int): Boolean {
        if (pouchId == Items.MEDIUM_POUCH_5510) return false
        return pouches[pouchId - 1] != null
    }

    /**
     * A class that represents a runecrafting pouch.
     * @author Ceikry
     */
    class RCPouch(val capacity: Int, val maxCharges: Int, val levelRequirement: Int) {
        var container = Container(capacity)
        var currentCap = capacity
        var charges = maxCharges
        fun remakeContainer() {
            this.container = Container(currentCap)
        }
    }
}
