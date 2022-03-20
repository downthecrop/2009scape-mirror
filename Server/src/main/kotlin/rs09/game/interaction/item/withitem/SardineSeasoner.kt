package rs09.game.interaction.item.withitem

import api.addItem
import api.removeItem
import api.sendDialogue
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

class SardineSeasoner : InteractionListener() {
    override fun defineListeners() {
        onUseWith(ITEM, Items.DOOGLE_LEAVES_1573, Items.RAW_SARDINE_327) {player, used, with ->
            if(removeItem(player, used.asItem()) && removeItem(player, with.asItem())){
                addItem(player, Items.DOOGLE_SARDINE_1552)
                sendDialogue(player, "You rub the doogle leaves over the sardine.")
            }
            return@onUseWith true
        }
    }
}