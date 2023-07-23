package content.global.handlers.item.withnpc

import core.api.*
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

class RopeOnLadyKeli : InteractionListener {
    override fun defineListeners() {
        val PAR = "Prince Ali Rescue"

        onUseWith(IntType.NPC, Items.ROPE_954, NPCs.LADY_KELI_919) { player, used, with ->
            if(getQuestStage(player, PAR) in 40..50 && getAttribute(player, "guard-drunk", false)){
                if(removeItem(player, used.asItem())){
                    sendDialogue(player, "You overpower Keli, tie her up, and put her in a cupboard.")
                    setQuestStage(player, PAR, 50)
                    setAttribute(player, "keli-gone", getWorldTicks() + 350)
                }
            } else {
                if (getQuestStage(player, PAR) == 40){
                    sendMessage(player, "You need to do something about the guard first.")
                }
            }
            return@onUseWith true
        }
    }
}