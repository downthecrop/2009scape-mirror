package rs09.game.interaction.item.withitem

import api.Container
import api.*
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

class HaySackOnSpear : InteractionListener() {
    val HAYSACK = Items.HAY_SACK_6057
    val SPEAR = Items.BRONZE_SPEAR_1237

    override fun defineListeners() {
        onUseWith(ITEM, HAYSACK, SPEAR){ player, used, _ ->
            if(removeItem(player, used.asItem(), Container.INVENTORY)){
                addItem(player, Items.HAY_SACK_6058, 1)
                sendMessage(player, "You stab the hay sack with a bronze spear")
                removeItem(player, SPEAR, Container.INVENTORY)
            }
            return@onUseWith true
        }
    }
}