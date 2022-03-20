package rs09.game.interaction.item.withitem

import api.*
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import rs09.game.interaction.InteractionListener

class KeyOnPirateChest : InteractionListener() {
    override fun defineListeners() {
        onUseWith(SCENERY, Items.CHEST_KEY_432, Scenery.CHEST_2079){player, used, with ->
            val scenery = with as core.game.node.scenery.Scenery
            if(removeItem(player, used, Container.INVENTORY)){
                replaceScenery(scenery, 2080, 3)
                addItemOrDrop(player, Items.PIRATE_MESSAGE_433)
                sendMessage(player, "You unlock the chest.")
                sendMessage(player, "All that's in the chest is a message...")
                sendMessage(player, "You take the message from the chest.")
            }
            return@onUseWith true
        }
    }
}