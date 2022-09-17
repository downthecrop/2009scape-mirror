package rs09.game.interaction.item.withobject

import api.*
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType

/**
 * Handles coal truck interactions
 * @author ceik
 * @author vddCore
 */
class CoalTruckListener : InteractionListener {
    companion object {
        private const val ATTRIBUTE_COAL_TRUCK_INVENTORY = "coal-truck-inventory"
    }

    override fun defineListeners() {
        on(Scenery.COAL_TRUCK_2114, IntType.SCENERY, "remove-coal") { player, node ->
            var coalInTruck = getAttribute(player, ATTRIBUTE_COAL_TRUCK_INVENTORY, 0)

            if (coalInTruck == 0) {
                sendDialogue(player, "The coal truck is empty.")
                return@on true
            }

            var toRemove = freeSlots(player)

            if (toRemove > coalInTruck) {
                toRemove = coalInTruck
            }

            if (addItem(player, Items.COAL_453, toRemove)) {
                coalInTruck -= toRemove
                setAttribute(player, "/save:$ATTRIBUTE_COAL_TRUCK_INVENTORY", coalInTruck)
            }

            return@on true
        }

        on(Scenery.COAL_TRUCK_2114, IntType.SCENERY, "investigate") { player, _ ->
            val coalInTruck = getAttribute(player, ATTRIBUTE_COAL_TRUCK_INVENTORY, 0)

            sendDialogue(
                player,
                "There is currently $coalInTruck coal in the truck. "
                        + "The truck has space for " + (120 - coalInTruck) + " more coal."
            )
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.COAL_453, Scenery.COAL_TRUCK_2114) { player, _, _ ->
            var coalInTruck = getAttribute(player, ATTRIBUTE_COAL_TRUCK_INVENTORY, 0)
            var coalInInventory = amountInInventory(player, Items.COAL_453)

            if (coalInInventory + coalInTruck >= 120) {
                coalInInventory = 120 - coalInTruck
                sendMessage(player, "You have filled up the coal truck.")
            }

            if (removeItem(player, Item(Items.COAL_453, coalInInventory))) {
                coalInTruck += coalInInventory

                setAttribute(
                    player,
                    "/save:$ATTRIBUTE_COAL_TRUCK_INVENTORY",
                    coalInTruck
                )
            }

            return@onUseWith true
        }
    }
}