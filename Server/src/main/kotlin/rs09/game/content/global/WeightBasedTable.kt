package rs09.game.content.global

import core.cache.def.impl.ItemDefinition
import core.game.content.ttrail.ClueLevel
import core.game.content.ttrail.ClueScrollPlugin
import core.game.node.entity.npc.drop.RareDropTable
import core.game.node.entity.npc.drop.CELEMinorTable
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs09.consts.Items

open class WeightBasedTable : ArrayList<WeightedItem>() {
    private var totalWeight = 0.0
    private val guaranteedItems = ArrayList<WeightedItem>()

    override fun add(element: WeightedItem): Boolean {
        return if (element.guaranteed) {
            guaranteedItems.add(element)
        } else {
            totalWeight += element.weight
            super.add(element)
        }
    }

    open fun roll(): ArrayList<Item>{
        val items = ArrayList<WeightedItem>()
        items.addAll(guaranteedItems)

        if (size == 1) {
            items.add(get(0))
        }
        else if (isNotEmpty()) {
            var rngWeight = RandomFunction.randomDouble(totalWeight)
            for (item in shuffled()) {
                rngWeight -= item.weight
                if (rngWeight <= 0) {
                    items.add(item)
                    break
                }
            }
        }

        return convertWeightedItems(items)
    }

    private fun convertWeightedItems(weightedItems: ArrayList<WeightedItem>): ArrayList<Item> {
        val safeItems = ArrayList<Item>(weightedItems.size)
        weightedItems.forEach { e ->
            val safeItem = when (e.id) {
                SLOT_CLUE_EASY -> ClueScrollPlugin.getClue(ClueLevel.EASY)
                SLOT_CLUE_MEDIUM -> ClueScrollPlugin.getClue(ClueLevel.MEDIUM)
                SLOT_CLUE_HARD -> ClueScrollPlugin.getClue(ClueLevel.HARD)
                SLOT_RDT -> RareDropTable.retrieve()
                SLOT_CELEDT -> CELEMinorTable.retrieve()
                else -> e.getItem()
            }
            safeItems.add(safeItem)
        }
        return safeItems
    }

    open fun canRoll(player: Player): Boolean{
        val guaranteed = guaranteedItems.map { it.getItem() }.toTypedArray()
        return (guaranteed.isNotEmpty() && player.inventory.hasSpaceFor(*guaranteed)) || !player.inventory.isFull
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

    fun insertCELEDTRoll(weight: Double): WeightBasedTable{
        this.add(WeightedItem(SLOT_CELEDT,1,1,weight,false))
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
        val SLOT_CELEDT = Items.NULL_799
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