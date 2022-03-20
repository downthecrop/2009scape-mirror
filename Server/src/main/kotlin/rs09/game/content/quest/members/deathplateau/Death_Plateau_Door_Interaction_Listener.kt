package rs09.game.content.quest.members.deathplateau

import api.getScenery
import api.location
import api.openDialogue
import core.game.content.global.action.DoorActionHandler
import org.rs09.consts.Scenery
import rs09.game.interaction.InteractionListener

/**
 * @author qmqz
 */

class Death_Plateau_Door_Interaction_Listener : InteractionListener() {

    override fun defineListeners() {
        on(Scenery.DOOR_3747, SCENERY, "open") { player, _ ->
            if (player.location == location(2906, 3543, 1)) {
                openDialogue(player, Death_Plateau_Door_Dialogue_File())
            } else {
                DoorActionHandler.handleAutowalkDoor(player, getScenery(2906, 3543, 1))
            }

            return@on true
        }
    }
}