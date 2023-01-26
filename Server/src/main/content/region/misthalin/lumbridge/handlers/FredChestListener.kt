package content.region.misthalin.lumbridge.handlers

import core.api.*
import org.rs09.consts.Scenery
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

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