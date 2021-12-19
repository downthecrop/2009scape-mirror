package rs09.game.interaction.region.dungeons.taverley

import api.*
import core.game.content.global.action.DoorActionHandler
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import rs09.game.interaction.InteractionListener

class TaverleyDungeonListeners : InteractionListener() {

    val BD_GATE = Scenery.GATE_2623
    val JAIL_DOOR = Scenery.DOOR_31838

    override fun defineListeners() {

        on(BD_GATE, SCENERY, "open"){player, node ->
            if(!inInventory(player, Items.DUSTY_KEY_1590)){
                sendMessage(player, "This gate seems to be locked.")
            } else {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            }
            return@on true
        }


        on(JAIL_DOOR, SCENERY, "open"){player, node ->
            when(player.location.y){
                9689 -> DoorActionHandler.handleAutowalkDoor(player, node.asScenery()) //inside the cell going out
                9690 -> {                                                              //outside the cell going in
                    if(!inInventory(player, Items.JAIL_KEY_1591)){
                        sendMessage(player, "This door is locked.")
                    } else {
                        DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                    }
                }
            }

            return@on true
        }

    }
}