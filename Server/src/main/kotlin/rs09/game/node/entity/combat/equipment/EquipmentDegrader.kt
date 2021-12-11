package rs09.game.node.entity.combat.equipment

import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs09.primextends.getNext
import org.rs09.primextends.isLast
import org.rs09.primextends.isNextLast

/**
 * Handles equipment degrading
 * @author ceik
 */
class EquipmentDegrader{

    companion object StaticDegrader { //Use a static companion object for lists and registers so they persist across instances
        val itemList = arrayListOf<Int>()
        val setList = arrayListOf<ArrayList<Int>>()
        val itemCharges = hashMapOf<Int, Int>()
        fun register(charges: Int, item: Int){
            itemList.add(item)
            itemCharges.put(item, charges)
        }

        fun registerSet(charges: Int, items: Array<Int>){
            setList.add(ArrayList(items.map { item -> item.also { register(charges, item) } }))
        }
    }



    var p: Player? = null

    fun Int.canDegrade(): Boolean { //extension function that checks if an item can degrade
        return itemList.contains(this)
    }

    fun checkArmourDegrades(player: Player?){ //loops the player slots and if the item in that slot is non-null, checks if it can degrade etc
        p = player
        for(slot in 0..12){
            if(slot == 3){
                continue
            }
            player?.equipment?.get(slot)?.let { if(it.id.canDegrade()) it.degrade(slot) }
        }
    }

    fun checkWeaponDegrades(player: Player?){
        p = player
        player?.equipment?.get(3)?.let { if(it.id.canDegrade()) it.degrade(3) }
    }

    fun Item.degrade(slot: Int){ //extension function that degrades items
        val set = getDegradableSet(this.id)
        this.charge--
        if(set?.indexOf(this.id) == 0 && !this.name.contains("100")){
            charge = 0
        }
        if(this.charge <= 0) {
            val charges = itemCharges.getOrElse(this.id, { 1000 })
            if (set?.size!! > 2) {
                p?.equipment?.remove(this)
                p?.sendMessage("Your $name has degraded.")
                if (set.isNextLast(set.indexOf(this.id))) {
                    p?.inventory?.add(Item(set.getNext(this.id)))
                    p?.sendMessage("Your $name has broken.")
                } else {
                    p?.equipment?.add(Item(set.getNext(this.id), 1, charges), slot, false, false)
                    p?.equipment?.refresh()
                }
            } else if (set.size == 2) {
                if(set.isLast(set.indexOf(this.id))){
                    p?.equipment?.remove(this)
                    p?.sendMessage("Your $name degrades into dust.")
                } else {
                    p?.equipment?.remove(this)
                    p?.sendMessage("Your $name has degraded.")
                    p?.equipment?.add(Item(set.getNext(this.id), 1, charges), slot, false, false)
                    p?.equipment?.refresh()
                }
            }
        }
    }

    private fun getDegradableSet(item: Int): ArrayList<Int>?{ //gets the set of items this item belongs to
        for(set in setList){
            if(set.contains(item))
                return set
        }
        return null
    }
}
