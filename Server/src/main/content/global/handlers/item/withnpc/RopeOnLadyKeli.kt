package content.global.handlers.item.withnpc

import core.api.*
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import content.data.Quests

class RopeOnLadyKeli : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.NPC, Items.ROPE_954, NPCs.LADY_KELI_919) { player, used, with ->
            if(getQuestStage(player, Quests.PRINCE_ALI_RESCUE) in 40..50 && getAttribute(player, "guard-drunk", false)){
                if(removeItem(player, used.asItem())){
                    sendDialogue(player, "You overpower Keli, tie her up, and put her in a cupboard.")
                    setQuestStage(player, Quests.PRINCE_ALI_RESCUE, 50)
                    setAttribute(player, "keli-gone", getWorldTicks() + 350)
                }
            } else {
                if (getQuestStage(player, Quests.PRINCE_ALI_RESCUE) == 40){
                    sendMessage(player, "You need to do something about the guard first.")
                }
            }
            return@onUseWith true
        }
    }
}