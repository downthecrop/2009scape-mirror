package rs09.game.interaction.region.lumbridge

import api.*
import core.game.world.map.Direction
import org.rs09.consts.Scenery
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType

/**
 * @author bushtail
 */

class FredChestListener : InteractionListener {
    val SHUT = Scenery.CLOSED_CHEST_37009
    val OPEN = Scenery.OPEN_CHEST_37010

    override fun defineListeners() {
        on(SHUT, IntType.SCENERY, "open") { _, node ->
            replaceScenery(node.asScenery(), OPEN, -1, node.location)
            return@on true
        }
        on(OPEN, IntType.SCENERY, "shut") { _, node ->
            replaceScenery(node.asScenery(), SHUT, -1, node.location)
            return@on true
        }
        on(OPEN, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You search the chest but find nothing.")
            return@on true
        }
    }
}