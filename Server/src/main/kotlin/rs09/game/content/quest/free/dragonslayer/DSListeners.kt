package rs09.game.content.quest.free.dragonslayer

import api.sendMessage
import org.rs09.consts.Scenery
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType

class DSListeners : InteractionListener {
    override fun defineListeners() {
        on(Scenery.CELL_DOOR_40184, IntType.SCENERY, "open") {player, _ ->
            sendMessage(player, "It's locked tight.")
            return@on true
        }
    }
}