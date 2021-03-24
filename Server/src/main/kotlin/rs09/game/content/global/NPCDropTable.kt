package rs09.game.content.global

import core.game.content.ttrail.ClueLevel
import core.game.content.ttrail.ClueScrollPlugin
import core.game.node.entity.npc.drop.RareDropTable
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs09.consts.Items

private val CHARMS = intArrayOf(Items.CRIMSON_CHARM_12160,Items.BLUE_CHARM_12163,Items.GREEN_CHARM_12159,Items.GOLD_CHARM_12158)
class NPCDropTable : WeightBasedTable() {
    val charmDrops = WeightBasedTable()

    override fun add(element: WeightedItem): Boolean {
        return if(CHARMS.contains(element.id)) charmDrops.add(element)
        else super.add(element)
    }

    override fun roll(player: Player?): ArrayList<Item> {
        val items= ArrayList<Item>(3)
        items.addAll(guaranteedItems.map { it.getItem() }.toList())

        if(RandomFunction.random(1,15) == 5){
            items.addAll(charmDrops.roll(null))
        }
        if(size == 1){
            items.add(get(0).getItem())
            return items
        }
        if(isNotEmpty()) {
            var tempWeight = RandomFunction.randomDouble(totalWeight)
            for (item in shuffled()) {
                tempWeight -= item.weight
                if (tempWeight <= 0) {
                    when(item.id){
                        SLOT_CLUE_EASY -> items.add(ClueScrollPlugin.getClue(ClueLevel.EASY))
                        SLOT_CLUE_MEDIUM -> items.add(ClueScrollPlugin.getClue(ClueLevel.MEDIUM))
                        SLOT_CLUE_HARD -> items.add(ClueScrollPlugin.getClue(ClueLevel.HARD))
                        SLOT_RDT -> items.add(RareDropTable.retrieve())
                        else -> items.add(item.getItem())
                    }
                    break
                }
            }
        }
        return items
    }

}