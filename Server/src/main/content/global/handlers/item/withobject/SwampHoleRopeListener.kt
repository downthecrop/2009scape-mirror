package content.global.handlers.item.withobject

import core.api.*
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import core.game.interaction.IntType
import core.game.interaction.InteractionListener

/**
 * Listener for using rope on Lumbridge swamp hole
 * @author Byte
 */
@Suppress("unused")
class SwampHoleRopeListener : InteractionListener {

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, Items.ROPE_954, Scenery.DARK_HOLE_5947) { player, used, _ ->
            if (player.savedData.globalData.hasTiedLumbridgeRope()) {
                sendDialogue(player, "There is already a rope tied to the entrance.")
                return@onUseWith true
            }

            if (!removeItem(player, used)) {
                return@onUseWith false
            }

            sendItemDialogue(player, used, "You tie the rope to the top of the entrance and throw it down.")
            player.savedData.globalData.setLumbridgeRope(true)

            return@onUseWith true
        }
    }
}
