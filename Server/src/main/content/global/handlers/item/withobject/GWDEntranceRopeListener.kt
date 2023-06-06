package content.global.handlers.item.withobject

import core.api.removeItem
import core.api.setVarbit
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import core.game.interaction.IntType
import core.game.interaction.InteractionListener

/**
 * Listener for using a rope directly on the GWD entrance hole
 * @author Byte
 */
@Suppress("unused")
class GWDEntranceRopeListener : InteractionListener {

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, Items.ROPE_954, Scenery.HOLE_26340) { player, used, _ ->
            if (!removeItem(player, used)) {
                return@onUseWith false
            }

            setVarbit(player, 3932, 1, true)

            return@onUseWith true
        }
    }
}
