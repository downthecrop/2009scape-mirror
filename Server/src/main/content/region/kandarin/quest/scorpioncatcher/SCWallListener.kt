package content.region.kandarin.quest.scorpioncatcher

import core.api.getQuestStage
import core.api.sendMessage
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import org.rs09.consts.Scenery

class SCWallListener : InteractionListener {

    override fun defineListeners() {
        on(Scenery.OLD_WALL_2117, IntType.SCENERY, "search"){ player, node ->
            // You can only go through if you are doing the quest
            //https://youtu.be/crc-47rwjvE?feature=shared&t=841
            // Otherwise the crack reverts back to normal
            // Doesn't make any sense but that's authentic...
            if ((ScorpionCatcher.QUEST_STATE_DARK_PLACE .. 99).contains(getQuestStage(player, "Scorpion Catcher"))) {
                // Check what side the player is on and teleport them to the other
                if (player.location == Location(2875, 9799, 0)){
                    sendMessage(player, "You've found a secret door")
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                }
                else{
                    // We're leaving the room
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                }
            }
            else{
                sendMessage(player, "You search the wall but find nothing.")
            }
            return@on true
        }
    }
}