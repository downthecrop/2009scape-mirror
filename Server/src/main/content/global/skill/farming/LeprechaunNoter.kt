package content.global.skill.farming

import core.api.*
import core.game.node.item.Item
import org.rs09.consts.NPCs
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

class LeprechaunNoter : InteractionListener {

    val CROPS = Plantable.values().map{ it.harvestItem }.toIntArray()
    val LEPRECHAUNS = intArrayOf(NPCs.TOOL_LEPRECHAUN_3021,NPCs.GOTH_LEPRECHAUN_8000,NPCs.TOOL_LEPRECHAUN_4965,NPCs.TECLYN_2861)

    override fun defineListeners() {
        onUseAnyWith(IntType.NPC,*LEPRECHAUNS) { player, used, with ->
            val usedItem = used.asItem()
            val npc = with.asNpc()
            val expr = when(npc.id){
                NPCs.TOOL_LEPRECHAUN_3021 -> core.game.dialogue.FacialExpression.OLD_NORMAL
                else -> core.game.dialogue.FacialExpression.FRIENDLY
            }
            when {
                (!usedItem.definition.isUnnoted) -> { // If the item is a banknote
                    player.dialogueInterpreter.sendDialogues(npc.id,expr,"That IS a banknote!")
                }
                (usedItem.id !in CROPS || usedItem.definition.noteId < 0) -> { // If the item is not a crop or cannot be noted
                    player.dialogueInterpreter.sendDialogues(npc.id,expr,"Nay, I can't turn that into a banknote.")
                }
                else -> { // Note the item
                    val amt = amountInInventory(player, usedItem.id)
                    if (removeItem(player, Item(usedItem.id, amt))) {
                        addItem(player, usedItem.noteChange, amt)
                    }
                    sendItemDialogue(player, usedItem.id, "The leprechaun exchanges your items for banknotes.")
                }
            }
            return@onUseAnyWith true
        }
    }
}