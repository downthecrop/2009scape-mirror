package core.game.interaction.`object`

import api.getUsedOption
import api.openBankAccount
import core.game.content.global.shop.CulinomancerShop
import org.rs09.consts.Scenery
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType

/**
 * Handles the culino chest options.
 * @author Ceikry
 */
class CulinoChestListener : InteractionListener {
    companion object {
        private const val CULINO_CHEST = Scenery.CHEST_12309
    }

    override fun defineListeners() {
        on(CULINO_CHEST, IntType.SCENERY, "buy-items", "buy-food"){player, _ ->
            CulinomancerShop.openShop(player, food = getUsedOption(player).lowercase() == "buy-food")
            return@on true
        }

        on(CULINO_CHEST, IntType.SCENERY, "bank"){player, _ ->
            openBankAccount(player)
            return@on true
        }
    }
}