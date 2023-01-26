package content.global.handlers.item.withitem

import core.api.Container
import core.api.*
import org.rs09.consts.Items
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

class HaySackOnSpear : InteractionListener {
    val HAYSACK = Items.HAY_SACK_6057
    val SPEAR = Items.BRONZE_SPEAR_1237

    override fun defineListeners() {
        onUseWith(IntType.ITEM, HAYSACK, SPEAR){ player, used, _ ->
            if(removeItem(player, used.asItem(), Container.INVENTORY)){
                addItem(player, Items.HAY_SACK_6058, 1)
                sendMessage(player, "You stab the hay sack with a bronze spear")
                removeItem(player, SPEAR, Container.INVENTORY)
            }
            return@onUseWith true
        }
    }
}