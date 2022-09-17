package rs09.game.content.dialogue.region.alkharid

import api.addItem
import api.openDialogue
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.DialogueFile
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType
import rs09.tools.END_DIALOGUE

/**
 * @author bushtail
 */

class AliTheLeafletDropperListener : InteractionListener {
    override fun defineListeners() {
        on(NPCs.ALI_THE_LEAFLET_DROPPER_3680, IntType.NPC, "Take-flyer") { player, node ->
            if(player.inventory.containItems(Items.AL_KHARID_FLYER_7922)) {
                openDialogue(player, DropperDialogue(2), node as NPC)
            } else {
                if(addItem(player, Items.AL_KHARID_FLYER_7922)) {
                    openDialogue(player, DropperDialogue(1), node as NPC)
                } else {
                    return@on false
                }
            }
            return@on true
        }
    }
}

class DropperDialogue(val it : Int) : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when(it) {
            1 -> when(stage) {
                0 -> npcl(FacialExpression.CHILD_NORMAL, "Here! Take one and let me get back to work.").also { stage++ }
                1 -> npcl(FacialExpression.CHILD_THINKING, "I still have hundreds of these flyers to hand out. I wonder if Ali would notice if I quietly dumped them somewhere?").also { stage = END_DIALOGUE }
            }
            2 -> npcl(FacialExpression.CHILD_SUSPICIOUS, "Are you trying to be funny or has age turned your brain to mush? You already have a flyer!").also { stage = END_DIALOGUE }
        }
    }

}