package content.region.misthalin.varrock.quest.dragonslayer

import core.api.sendMessage
import org.rs09.consts.Scenery
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

class DSListeners : InteractionListener {
    override fun defineListeners() {
        on(Scenery.CELL_DOOR_40184, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "It's locked tight.")
            return@on true
        }
    }
}