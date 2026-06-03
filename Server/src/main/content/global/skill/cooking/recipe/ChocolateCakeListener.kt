package content.global.skill.cooking.recipe

import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Items

class ChocolateCakeListener : InteractionListener {

    val chocolate = intArrayOf(Items.CHOCOLATE_BAR_1973, Items.CHOCOLATE_DUST_1975)

    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.CAKE_1891, *chocolate) { player, used, with ->
            val product = Items.CHOCOLATE_CAKE_1897
            val level = 50
            val experience = 30.0
            val message = ""
            val failMessage = "You need a Cooking level of $level in order to do that."

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