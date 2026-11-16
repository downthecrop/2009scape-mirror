package content.region.asgarnia.handlers

import core.api.sendMessage
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import org.rs09.consts.Scenery

class TowerDoorListener : InteractionListener {
    override fun defineListeners() {
        on(Scenery.DOOR_24759, IntType.SCENERY,"open") { player, _ ->
            sendMessage(player, "The door is locked.") // TODO authentic message
            return@on true
        }
    }
}
