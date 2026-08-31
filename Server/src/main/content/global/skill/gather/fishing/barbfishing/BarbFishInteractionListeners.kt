package content.global.skill.gather.fishing.barbfishing

import content.region.kandarin.quest.barbariantraining.BarbarianTraining
import core.api.*
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

class BarbFishInteractionListeners : InteractionListener {
    override fun defineListeners() {

        on(Scenery.BARBARIAN_BED_25268, IntType.SCENERY, "search"){ player, _ ->
            // Check if player has started heavy rod training
            if(getAttribute(player, BarbarianTraining.attributeRod, 0) >= 1) {
                if(!inInventory(player, Items.BARBARIAN_ROD_11323)) {
                    addItemOrDrop(player, Items.BARBARIAN_ROD_11323, 1)
                    sendMessage(player, "You find a heavy fishing rod under the bed and take it.")
                } else {
                    sendMessage(player, "You find nothing under the bed.")
                }
            } else {
                sendMessage(player, "Maybe I should speak to Otto before looking under his bed.")
            }
            return@on true
        }

        on(NPCs.FISHING_SPOT_2722, IntType.NPC, "use-rod"){ player, _ ->
            submitIndividualPulse(player, BarbFishingPulse(player))
            sendMessage(player, "You cast out your line...")
            sendMessage(player, "You attempt to catch a fish.")
            return@on true
        }

    }
}