package rs09.game.content.global

import core.cache.def.impl.ItemDefinition
import core.game.content.ttrail.ClueLevel
import core.game.content.ttrail.ClueScrollPlugin
import core.game.node.entity.npc.drop.RareDropTable
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs09.consts.Items

open class WeightBasedTable : ArrayList<WeightedItem>() {
    var totalWeight = 0.0
    val guaranteedItems = ArrayList<WeightedItem>(5)

    override fun add(element: WeightedItem): Boolean {
        totalWeight += element.weight
        return if(element.guaranteed) guaranteedItems.add(element)
        else super.add(element)
    }

    fun roll(): ArrayList<Item>{
        return roll(null)
    }

    open fun roll(player: Player?): ArrayList<Item>{
        if(size == 0) return ArrayList()
        val items= ArrayList<Item>(3)
        var tempWeight = RandomFunction.randomDouble(totalWeight)
        items.addAll(guaranteedItems.map { it.getItem() }.toList())

        if(player?.inventory?.isFull == true){
            return items
        }

        if(isNotEmpty()) {
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

    open fun canRoll(player: Player): Boolean{
        val guaranteed = guaranteedItems.map { it.getItem() }.toTypedArray()
        return (player.inventory.hasSpaceFor(*guaranteed) && guaranteed.isNotEmpty()) || !player.inventory.isFull
    }


    fun insertEasyClue(weight: Double): WeightBasedTable{
        this.add(WeightedItem(SLOT_CLUE_EASY,1,1,weight,false))
        return this
    }

    fun insertMediumClue(weight: Double): WeightBasedTable{
        this.add(WeightedItem(SLOT_CLUE_MEDIUM,1,1,weight,false))
        return this
    }

    fun insertHardClue(weight: Double): WeightBasedTable{
        this.add(WeightedItem(SLOT_CLUE_HARD,1,1,weight,false))
        return this
    }

    fun insertRDTRoll(weight: Double): WeightBasedTable{
        this.add(WeightedItem(SLOT_RDT,1,1,weight,false))
        return this
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

        @JvmField
        val SLOT_RDT = Items.TINDERBOX_31
        val SLOT_CLUE_EASY = Items.TOOLKIT_1
        val SLOT_CLUE_MEDIUM = Items.ROTTEN_POTATO_5733
        val SLOT_CLUE_HARD = Items.GRANITE_LOBSTER_POUCH_12070
    }

    override fun toString(): String {
        val builder = StringBuilder()
        for(item in this){
            builder.append("${ItemDefinition.forId(item.id).name} || Weight: ${item.weight} || MinAmt: ${item.minAmt} || maxAmt: ${item.maxAmt}")
            builder.appendLine()
        }
        return builder.toString()
    }
}