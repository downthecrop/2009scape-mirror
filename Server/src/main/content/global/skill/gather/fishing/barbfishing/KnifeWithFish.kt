package content.global.skill.gather.fishing.barbfishing

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Items

/**
 * Handles using a knife with barbarian leaping fish.
 * @author Bishop
 */

class KnifeWithFish : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.KNIFE_946, Items.LEAPING_TROUT_11328, Items.LEAPING_SALMON_11330, Items.LEAPING_STURGEON_11332) { player, _, fish ->
            submitIndividualPulse(player, FishCuttingPulse(player,fish.id))
            return@onUseWith true
        }
    }
}
