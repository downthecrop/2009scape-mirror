package content.global.skill.cooking.recipe

import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Items

class PieShellListener : InteractionListener {

    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.PIE_DISH_2313, Items.PASTRY_DOUGH_1953) { player, used, with ->
            val product = Items.PIE_SHELL_2315
            val level = 1
            val experience = 0.0
            val message = "You put the pastry dough into the pie dish to make a pie shell."
            val failMessage = ""

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