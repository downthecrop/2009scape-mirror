package rs09.game.interaction.item.withnpc

import api.*
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener

class RopeOnLadyKeli : InteractionListener() {
    override fun defineListeners() {
        val PAR = "Prince Ali Rescue"

        onUseWith(NPC, Items.ROPE_954, NPCs.LADY_KELI_919) {player, used, with ->
            if(questStage(player, PAR) in 40..50 && getAttribute(player, "guard-drunk", false)){
                if(removeItem(player, used.asItem())){
                    sendDialogue(player, "You overpower Keli, tie her up, and put her in a cupboard.")
                    setQuestStage(player, PAR, 50)
                    setAttribute(player, "keli-gone", getWorldTicks() + 350)
                }
            } else {
                if (questStage(player, PAR) == 40){
                    sendMessage(player, "You need to do something about the guard first.")
                }
            }
            return@onUseWith true
        }
    }
}