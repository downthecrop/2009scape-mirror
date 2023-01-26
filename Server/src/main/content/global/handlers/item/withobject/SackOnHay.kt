package content.global.handlers.item.withobject

import core.api.Container
import core.api.*
import org.rs09.consts.Items
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

class SackOnHay : InteractionListener {
    val SACK = Items.EMPTY_SACK_5418
    val HAY = intArrayOf(36892, 36894, 36896, 300, 34593, 298, 299)

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, SACK, *HAY){ player, used, _ ->
            if(removeItem(player, used.asItem(), Container.INVENTORY)){
                addItem(player, Items.HAY_SACK_6057, 1)
                sendMessage(player, "You fill the sack with hay.")
            }
            return@onUseWith true
        }
    }
}