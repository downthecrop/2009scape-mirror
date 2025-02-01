package content.region.asgarnia.rimmington.handler

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import org.rs09.consts.Items
import org.rs09.consts.Sounds

/**
 * @author Player Name
 */

class RimmingtonListeners : InteractionListener {
    override fun defineListeners() {
        on(9662, IntType.SCENERY, "take") { player, node ->
            if (!hasSpaceFor(player, Item(Items.SPADE_952))) {
                sendMessage(player, "You don't have enough inventory space to hold that item.")
                return@on true
            }
            animate(player, 535)
            playAudio(player, Sounds.PICK2_2582)
            addItem(player, Items.SPADE_952)
            replaceScenery(node as Scenery, 0, 50)
            return@on true
        }
    }
}
