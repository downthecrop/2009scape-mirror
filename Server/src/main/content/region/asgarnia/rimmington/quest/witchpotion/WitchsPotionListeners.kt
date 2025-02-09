package content.region.asgarnia.rimmington.quest.witchpotion

import content.data.Quests
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Scenery

/**
 * Interaction Listener for Witch's Potion Quest.
 */
class WitchsPotionListener : InteractionListener {

    override fun defineListeners() {

        on(Scenery.CAULDRON_2024, IntType.SCENERY, "drink from") { player, node ->
            if (getQuestStage(player, Quests.WITCHS_POTION) == 40) {
                sendDialogue(player, "You drink from the cauldron, it tastes horrible! You feel yourself imbued with power.")
                finishQuest(player, Quests.WITCHS_POTION)
            } else {
                sendDialogue(player, "As nice as that looks I think I'll give it a miss for now.")
            }
            return@on true
        }
    }
}
