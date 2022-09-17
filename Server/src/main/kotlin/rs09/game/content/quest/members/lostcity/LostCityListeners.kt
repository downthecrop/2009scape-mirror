package rs09.game.content.quest.members.lostcity

import api.*
import core.game.content.global.action.DoorActionHandler
import core.game.node.entity.player.link.TeleportManager.TeleportType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.Items
import rs09.game.interaction.IntType
import org.rs09.consts.Scenery as Sceneries
import rs09.game.interaction.InteractionListener
import rs09.game.world.GameWorld

/**
 * This class covers some listeners for the Lost City quest
 * @author lila
 */
@Initializable
class LostCityListeners : InteractionListener {

    override fun defineListeners() {

        // the shed teleport, to allow players to access zanaris if they enter the shed while wielding the dramen staff
        on(Sceneries.DOOR_2406,IntType.SCENERY,"open"){ player, node ->
            DoorActionHandler.handleAutowalkDoor(player,node as Scenery)
            val quest = "Lost City"
            val isOutsideShed = player.location.x < node.location.x
            val canDramenTeleport = inEquipment(player,Items.DRAMEN_STAFF_772) && ( questStage(player,quest) > 20 ) && isOutsideShed
            if(canDramenTeleport) {
                var count = 0
                // pulser to handle the teleport. after 2 ticks it checks if the player hasnt completed lost city; if so, then it finishes the quest after the teleport
                GameWorld.Pulser.submit(object : Pulse(2) {
                    override fun pulse(): Boolean {
                        when (count++) {
                            0 -> {
                                sendMessage(player,"The world starts to shimmer...")
                                teleport(player, Location(2452, 4473, 0), TeleportType.FAIRY_RING)
                            }
                            1 -> return isQuestComplete(player,quest)
                            2 -> {
                                finishQuest(player,quest)
                                return true
                            }
                        }
                        return false
                    }
                })
            }
            return@on true
        }
        // creating the dramen staff via using knife on dramen branch
        onUseWith(IntType.ITEM,Items.KNIFE_946,Items.DRAMEN_BRANCH_771){ player, _, _ ->
            if(!player.skills.hasLevel(Skills.CRAFTING,31)) {
                sendDialogue(player,"You need a crafting level of 31 to do this.")
                return@onUseWith false
            }
            runTask(player, 2) {
                if (removeItem(player, Item(Items.DRAMEN_BRANCH_771, 1), Container.INVENTORY)) {
                        sendDialogue(player,"You carve the branch into a staff.")
                        addItem(player, Items.DRAMEN_STAFF_772, 1, Container.INVENTORY)
                    }
                }
            return@onUseWith true
        }
    }
}
