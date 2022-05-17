package rs09.game.node.entity.npc.city.alkharid

import api.addItem
import api.sendNPCDialogue
import core.game.content.dialogue.FacialExpression
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener
import rs09.tools.END_DIALOGUE

class AliTheLeafletDropperListener : InteractionListener {
    override fun defineListeners() {
        on(NPCs.ALI_THE_LEAFLET_DROPPER_3680, "take-flyer") { player, node ->
            if(player.inventory.containItems(Items.AL_KHARID_FLYER_7922)) {
                sendNPCDialogue(player, NPCs.ALI_THE_LEAFLET_DROPPER_3680,
                "Are you trying to be funny or has age turned your brain to mush? You already have a flyer!",
                FacialExpression.CHILD_SUSPICIOUS).also { END_DIALOGUE }
            } else {
                if(addItem(player, Items.AL_KHARID_FLYER_7922)) {
                    sendNPCDialogue(player, NPCs.ALI_THE_LEAFLET_DROPPER_3680, "Here! Take one and let me get back to work.",
                    FacialExpression.CHILD_NORMAL)
                    sendNPCDialogue(player, NPCs.ALI_THE_LEAFLET_DROPPER_3680, "I still have hundreds of these flyers to hand out. I wonder if Ali would notice if I quietly dumped them somewhere?",
                    FacialExpression.CHILD_THINKING)
                } else {
                    return@on false
                }
            }
            return@on true
        }
    }

}