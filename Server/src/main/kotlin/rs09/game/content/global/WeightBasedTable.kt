package rs09.game.content.global

import core.game.node.entity.player.Player
import core.game.node.item.Item

class WeightBasedTable : ArrayList<WeightedItem>() {
    var totalWeight = 0.0
    val guaranteedItems = ArrayList<WeightedItem>(5)

    override fun add(element: WeightedItem): Boolean {
        totalWeight += element.weight
        return if(element.guaranteed) guaranteedItems.add(element)
        else super.add(element)
    }

    fun roll(player: Player): ArrayList<Item>{
        val items= ArrayList<Item>(3)
        var tempWeight = totalWeight
        items.addAll(guaranteedItems.map { it.getItem() }.toList())

        if(player.inventory.isFull){
            return items
        }

        if(isNotEmpty()) {
            for (item in shuffled()) {
                tempWeight -= item.weight
                if (tempWeight <= 0) {
                    items.add(item.getItem())
                    break
                }
            }
        }
        return items
    }

    fun canRoll(player: Player): Boolean{
        val guaranteed = guaranteedItems.map { it.getItem() }.toTypedArray()
        return (player.inventory.hasSpaceFor(*guaranteed) && guaranteed.isNotEmpty()) || !player.inventory.isFull
    }

    companion object {
        @JvmStatic
        fun create(vararg items: WeightedItem): WeightBasedTable{
            val table = WeightBasedTable()
            items.forEach {
                table.add(it)
            }
            return table
        }
    }
}