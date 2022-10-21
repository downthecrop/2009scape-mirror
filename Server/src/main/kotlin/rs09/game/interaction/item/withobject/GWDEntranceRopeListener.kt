package rs09.game.interaction.item.withobject

import api.removeItem
import api.setVarbit
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import rs09.game.interaction.IntType
import rs09.game.interaction.InteractionListener

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

            setVarbit(player,1048,0,1,true)

            return@onUseWith true
        }
    }
}
