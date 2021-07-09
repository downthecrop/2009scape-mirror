package rs09.game.interaction.item.withobject

import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener
import java.util.*

/**
 * Handles coal truck interactions
 * @author ceik
 */
class CoalTruckInteractionListeners : InteractionListener() {

    val SEERS_VILLAGE_COAL_TRUCK_2114 = 2114
    val seersVillageTrucks = ZoneBorders(2690,3502,2699,3508)
    val COAL_TRUCK_2114 = 2114
    val COAL = Items.COAL_453

    override fun defineListeners() {
        on(SEERS_VILLAGE_COAL_TRUCK_2114, SCENERY, "remove-coal") { player, node ->

            var coalInTruck = player.getAttribute("coal-truck-inventory", 0)
            var freeSpace = player.inventory.freeSlots()
            val toAdd: MutableList<Item> = ArrayList()

            if (coalInTruck == 0) {
                player.dialogueInterpreter.sendDialogue("The coal truck is empty.")
                return@on true
            }

            var toRemove = player.inventory.freeSlots()

            if(toRemove > coalInTruck) toRemove = coalInTruck

            player.inventory.add(Item(Items.COAL_453,toRemove))

            coalInTruck -= toRemove
            player.setAttribute("/save:coal-truck-inventory", coalInTruck)

            if (!player.achievementDiaryManager.hasCompletedTask(DiaryType.SEERS_VILLAGE, 1, 2)
                && seersVillageTrucks.insideBorder(player)
                && player.getAttribute("diary:seers:coal-truck-full", false)) {
                player.removeAttribute("diary:seers:coal-truck-full")
                player.achievementDiaryManager.finishTask(player, DiaryType.SEERS_VILLAGE, 1, 2)
            }
            return@on true
        }

        on(SEERS_VILLAGE_COAL_TRUCK_2114, SCENERY, "investigate") { player, node ->
            var coalInTruck = player.getAttribute("coal-truck-inventory", 0)
            player.dialogueInterpreter.sendDialogue("There is currently $coalInTruck coal in the truck.", "The truck has space for " + (120 - coalInTruck) + " more coal.")
            return@on true
        }

        onUseWith(SCENERY,COAL,COAL_TRUCK_2114){ player, _, _ ->
            var coalInTruck = player.getAttribute("coal-truck-inventory", 0)

            var coalInInventory = player.inventory.getAmount(Items.COAL_453)

            if(coalInInventory + coalInTruck >= 120){
                coalInInventory = 120 - coalInTruck
                player.packetDispatch.sendMessage("You have filled up the coal truck.")

                //handle coal truck task for seer's village
                if (!player.achievementDiaryManager.getDiary(DiaryType.SEERS_VILLAGE).isComplete(1, 2)
                    && player.viewport.region.id == 10294) { // region 10294 is at coal truck mine, region 10806 is in seers village
                    player.setAttribute("/save:diary:seers:coal-truck-full", true)
                }
            }

            player.inventory.remove(Item(Items.COAL_453,coalInInventory))
            coalInTruck += coalInInventory

            player.setAttribute("/save:coal-truck-inventory",coalInTruck)
            return@onUseWith true
        }
    }
}