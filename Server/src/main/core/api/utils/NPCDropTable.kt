package core.api.utils

import core.game.node.entity.Entity
import core.game.node.item.Item

class NPCDropTable : WeightBasedTable() {
    private val charmDrops = WeightBasedTable()
    private val tertiaryDrops = WeightBasedTable()

    fun addToCharms(element: WeightedItem): Boolean {
        return charmDrops.add(element)
    }

    fun addToTertiary(element: WeightedItem) : Boolean {
        return tertiaryDrops.add(element)
    }

    override fun roll(receiver: Entity?, times: Int): ArrayList<Item> {
        val items = ArrayList<Item>()
        // Charms table is always rolled, and should contain explicit "Nothing" 
        // entries at the data level to account for the chance to not drop a charm.
        items.addAll(charmDrops.roll(receiver, times))
        items.addAll(tertiaryDrops.roll(receiver, times))
        items.addAll(super.roll(receiver, times))
        return items
    }

}
