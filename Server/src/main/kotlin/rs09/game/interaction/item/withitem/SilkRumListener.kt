package rs09.game.interaction.item.withitem

import api.asItem
import api.replaceSlot
import api.sendMessage
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

class SilkRumListener : InteractionListener() {
    override fun defineListeners() {
        onUseWith(ITEM, Items.KARAMJAN_RUM_431, Items.SILK_950){player, _, with ->
            replaceSlot(player, with.asItem().slot, Items.CLEANING_CLOTH_3188.asItem())
            sendMessage(player, "You pour some of the Karamjan rum over the silk.")
            return@onUseWith true
        }
    }
}