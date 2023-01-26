package content.global.handlers.item.withobject

import core.api.addItem
import core.api.removeItem
import core.api.replaceScenery
import core.api.sendMessage
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import core.game.interaction.IntType
import core.game.interaction.InteractionListener

/**
 * Listener for using key on pirate chest in Blue Moon Inn
 * @author Byte
 */
@Suppress("unused")
class ChestKeyListener : InteractionListener {

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, Items.CHEST_KEY_432, Scenery.CHEST_2079) { player, used, with ->
            if (!removeItem(player, used)) {
                return@onUseWith false
            }

            replaceScenery(with as core.game.node.scenery.Scenery, 2080, 3)

            addItem(player, Items.PIRATE_MESSAGE_433)
            sendMessage(player, "You unlock the chest.")
            sendMessage(player, "All that's in the chest is a message...")
            sendMessage(player, "You take the message from the chest.")

            return@onUseWith true
        }
    }
}
