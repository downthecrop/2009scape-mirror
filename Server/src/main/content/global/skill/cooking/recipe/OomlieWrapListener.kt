package content.global.skill.cooking.recipe

import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Items

class OomlieWrapListener : InteractionListener {

    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.RAW_OOMLIE_2337, Items.PALM_LEAF_2339) { player, used, with ->
            val product = Items.WRAPPED_OOMLIE_2341
            val level = 50
            val experience = 0.0
            val message = "You wrap the raw oomlie in the palm leaf."
            val failMessage = "You need a Cooking level of at least $level in order to do that."

            return@onUseWith standardMix(
                player,
                used.asItem(),
                with.asItem(),
                product,
                level,
                experience,
                message,
                failMessage
            )
        }
    }
}