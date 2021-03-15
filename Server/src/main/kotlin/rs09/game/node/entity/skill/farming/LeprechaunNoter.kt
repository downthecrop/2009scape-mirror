package rs09.game.node.entity.skill.farming

import core.game.content.dialogue.FacialExpression
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener

class LeprechaunNoter : InteractionListener() {

    val CROPS = Plantable.values().map{ it.harvestItem }.toIntArray()
    val LEPRECHAUNS = intArrayOf(NPCs.TOOL_LEPRECHAUN_3021,NPCs.GOTH_LEPRECHAUN_8000,NPCs.TOOL_LEPRECHAUN_4965)

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
                player.dialogueInterpreter.sendDialogues(npc.id,expr,"There ya go.")
            } else {
                player.dialogueInterpreter.sendDialogues(npc.id,expr,"But that's already a note!")
            }

            return@onUseWith true
        }
    }
}