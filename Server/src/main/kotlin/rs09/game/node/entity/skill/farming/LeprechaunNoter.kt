package rs09.game.node.entity.skill.farming

import api.*
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener

class LeprechaunNoter : InteractionListener() {

    val CROPS = Plantable.values().map{ it.harvestItem }.toIntArray()
    val LEPRECHAUNS = intArrayOf(NPCs.TOOL_LEPRECHAUN_3021,NPCs.GOTH_LEPRECHAUN_8000,NPCs.TOOL_LEPRECHAUN_4965,NPCs.TECLYN_2861)

    override fun defineListeners() {
        onUseWith(NPC,CROPS,*LEPRECHAUNS){player, used, with ->
            val usedItem = used.asItem()
            val npc = with.asNpc()
            val expr = when(npc.id){
                3021 -> FacialExpression.OLD_NORMAL
                else -> FacialExpression.FRIENDLY
            }

            if(usedItem.noteChange != usedItem.id){
                val amt = player.inventory.getAmount(usedItem.id)
                if(player.inventory.remove(Item(usedItem.id,amt))){
                    player.inventory.add(Item(usedItem.noteChange,amt))
                }
                 sendItemDialogue(player,usedItem.id,"The leprechaun exchanges your items for banknotes.")
            } else {
			// Unsure why the line below no longer functions, despite only changing the line above to be more correct. Using your note(NOT CROP) on the leprechaun no longer functions because of this. - Crash
                player.dialogueInterpreter.sendDialogues(npc.id,expr,"That IS a banknote!") 
            }

            return@onUseWith true
        }
    }
}