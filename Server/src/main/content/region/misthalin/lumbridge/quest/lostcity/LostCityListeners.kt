package content.region.misthalin.lumbridge.quest.lostcity

import core.api.*
import core.game.node.entity.player.link.TeleportManager.TeleportType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.Items
import core.game.interaction.IntType
import org.rs09.consts.Scenery as Sceneries
import core.game.interaction.InteractionListener
import core.game.world.GameWorld
import content.data.Quests

/**
 * This class covers some listeners for the Lost City quest
 * @author lila
 * @author Player Name
 */
@Initializable
class LostCityListeners : InteractionListener {

    override fun defineListeners() {

        // the shed teleport, to allow players to access zanaris if they enter the shed while wielding the dramen staff
        on(Sceneries.DOOR_2406, IntType.SCENERY,"open"){ player, node ->
            core.game.global.action.DoorActionHandler.handleAutowalkDoor(player,node as Scenery)
            val isOutsideShed = player.location.x < node.location.x
            val canDramenTeleport = inEquipment(player,Items.DRAMEN_STAFF_772) && getQuestStage(player, Quests.LOST_CITY) > 20 && isOutsideShed
            if (canDramenTeleport) {
                var count = 0
                // pulser to handle the teleport. after 2 ticks it checks if the player hasn't completed Lost City; if so, then it finishes the quest after the teleport
                GameWorld.Pulser.submit(object : Pulse(2) {
                    override fun pulse(): Boolean {
                        when (count++) {
                            0 -> {
                                if (player.isTeleBlocked()) {
                                    sendMessage(player,"A magical force has stopped you from teleporting.")
                                } else {
                                    sendMessage(player,"The world starts to shimmer...")
                                    teleport(player, Location(2452, 4473, 0), TeleportType.FAIRY_RING)
                                }
                            }
                            1 -> return isQuestComplete(player, Quests.LOST_CITY)
                            2 -> {
                                finishQuest(player, Quests.LOST_CITY)
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
