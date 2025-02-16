package content.region.kandarin.ardougne.westardougne.handlers

import content.data.Quests
import core.api.*
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

class MainGatesListener : InteractionListener {

    override fun defineListeners() {
        on(intArrayOf(Scenery.ARDOUGNE_WALL_DOOR_9738, Scenery.ARDOUGNE_WALL_DOOR_9330), IntType.SCENERY, "open") { player, node ->
            if (isQuestComplete(player, Quests.BIOHAZARD)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else if(inBorders(player, 2556, 3298, 2557, 3301)){
                lock(player,2)
                sendMessage(player, "You pull on the large wooden doors...")
                queueScript(player,2){
                    sendMessage(player, "...but they will not open.")
                    return@queueScript stopExecuting(player)
                }
            } else {
                face(player, Location.create(2559, 3302, 0))
                sendNPCDialogue(player, NPCs.MOURNER_2349, "Oi! What are you doing? Get away from there!")
            }
            return@on true
        }
    }
}