package rs09.game.interaction.item.withitem

import api.addItem
import api.removeItem
import api.sendMessage
import org.rs09.consts.Items
import rs09.game.interaction.IntType
import rs09.game.interaction.InteractionListener

/**
 * Listener for creating a crystal key
 * @author Byte
 */
@Suppress("unused")
class CrystalKeyCreateListener : InteractionListener {

    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.LOOP_HALF_OF_A_KEY_987, Items.TOOTH_HALF_OF_A_KEY_985) { player, used, with ->
            if (!removeItem(player, used)) {
                return@onUseWith false
            }

            if (!removeItem(player, with)) {
                return@onUseWith false
            }

            addItem(player, Items.CRYSTAL_KEY_989)
            sendMessage(player, "You join the loop half of a key and the tooth half of a key to make a crystal key.")

            return@onUseWith true
        }
    }
}
