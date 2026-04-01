package content.global.handlers.item.withitem

import core.api.addItem
import core.api.animate
import core.api.removeItem
import core.game.interaction.InteractionListener
import org.rs09.consts.Items

class CalquatKeg : InteractionListener{
    override fun defineListeners() {
        onUseWith(ITEM, Items.CALQUAT_FRUIT_5980, Items.KNIFE_946) { player, _, _ ->
            if (removeItem(player, Items.CALQUAT_FRUIT_5980)){
                animate(player,2290)
                addItem(player, Items.CALQUAT_KEG_5769)
            }
            return@onUseWith true
        }
    }
}