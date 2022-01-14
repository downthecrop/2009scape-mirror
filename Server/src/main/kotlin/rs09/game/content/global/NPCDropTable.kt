package rs09.game.content.global

import core.game.node.item.Item

class NPCDropTable : WeightBasedTable() {
    private val charmDrops = WeightBasedTable()

    fun addToCharms(element: WeightedItem): Boolean {
        return charmDrops.add(element)
    }

    override fun roll(): ArrayList<Item> {
        val items = ArrayList<Item>()
        // Charms table is always rolled, and should contain explicit "Nothing" 
        // entries at the data level to account for the chance to not drop a charm.
        items.addAll(charmDrops.roll())
        items.addAll(super.roll())
        return items
    }

}
