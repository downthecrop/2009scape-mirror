package rs09.game.interaction.item.withitem

import api.Container
import api.ContentAPI
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

class HaySackOnSpear : InteractionListener() {
    val HAYSACK = Items.HAY_SACK_6057
    val SPEAR = Items.BRONZE_SPEAR_1237

    override fun defineListeners() {
        onUseWith(ITEM, HAYSACK, SPEAR){ player, used, _ ->
            if(ContentAPI.removeItem(player, used.asItem(), Container.INVENTORY)){
                ContentAPI.addItem(player, Items.HAY_SACK_6058, 1)
                ContentAPI.sendMessage(player, "You stab the hay sack with a bronze spear")
                ContentAPI.removeItem(player, SPEAR, Container.INVENTORY)
            }
            return@onUseWith true
        }
    }
}