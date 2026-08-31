package content.global.skill.construction

import content.global.skill.construction.decoration.workshop.WorkbenchListeners
import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.interaction.InterfaceListener
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.tools.Log
import org.rs09.consts.Components
import org.rs09.consts.Scenery as SceneryObj

/**
 * Handles three interfaces related to the Construction skill, namely those which allow a player to build decorations,
 * toggle building mode/expel guests, and select a room to add to the POH.
 * @author Bishop
 */

class ConstructionInterface : InterfaceListener {

    companion object {
        const val ATTRIBUTE_HOTSPOT                  = "con:hotspot"
        const val ATTRIBUTE_HOTSPOT_OBJ              = "con:hsobject"
        private const val DECO_BUTTON_BUILD          = 132
        private const val HOUSE_BUTTON_BUILDMODE_ON  = 14
        private const val HOUSE_BUTTON_BUILDMODE_OFF = 1
        private const val HOUSE_BUTTON_EXPEL_GUESTS  = 15
        private const val HOUSE_BUTTON_LEAVE_HOUSE   = 13

        // Register furniture here when it is only obtainable by upgrading, to send the correct rejection message.
        private val upgradeOnly = arrayOf(
            Decoration.WORKBENCH_WITH_VICE,
            Decoration.WORKBENCH_WITH_LATHE,
        )
    }

    override fun defineInterfaceListeners() {
        /**
         * Handles the interface for selecting a decoration to build.
         */
        on(Components.POH_DECORATION_396) { player, _, _, buttonID, slot, _ ->
            when (buttonID) {
                DECO_BUTTON_BUILD -> {
                    val hotspot = getAttribute<Hotspot?>(player, ATTRIBUTE_HOTSPOT, null)
                    val hotspotObj = getAttribute<Scenery?>(player, ATTRIBUTE_HOTSPOT_OBJ, null)
                    val flatpackMode = getAttribute(player, WorkbenchListeners.ATTRIBUTE_FLATPACK_MODE, false)
                    if (!flatpackMode) {
                        closeInterface(player)
                    }
                    if ((hotspot == null || hotspotObj == null) && !flatpackMode) {
                        log(this.javaClass, Log.ERR, "Failed building decoration $hotspot : $hotspotObj")
                        return@on false
                    }
                    // Translates slot data to index data, as slots are not in order of level requirement in the interface, but decoration data is organized that way
                    val decoIndex = (if (slot % 2 != 0) 4 else 0) + (slot shr 1)
                    val buildHotspot = if (flatpackMode) {
                        getAttribute<BuildHotspot?>(player, WorkbenchListeners.ATTRIBUTE_WORKBENCH_SELECTION, null)?: return@on false
                    } else {
                        hotspot!!.hotspot
                    }
                    if (decoIndex >= buildHotspot.decorations.size) {
                        return@on false
                    }
                    val deco = buildHotspot.decorations[decoIndex]
                    if (!player.isAdmin) {
                        if (getDynLevel(player, Skills.CONSTRUCTION) < deco.level) {
                            closeInterface(player)
                            sendMessage(player, "You need to have a Construction level of ${deco.level} to build that.")
                            return@on true
                        }
                        if (deco in upgradeOnly) {
                            closeInterface(player)
                            sendMessage(player, "That can only be built by upgrading the previous piece of furniture.")
                            return@on true
                        }
                        if (!player.inventory.containsItems(*deco.items)) { // No clean ContentAPI substitute for this
                            closeInterface(player)
                            sendMessage(player, "You don't have the right materials.")
                            return@on true
                        }
                        for (tool in deco.tools) {
                            if (tool == BuildingUtils.WATERING_CAN) {
                                var hasWaterInCan = false
                                for (i in 0..8) {
                                    if (inInventory(player, tool - i)) {
                                        hasWaterInCan = true
                                        break
                                    }
                                }
                                if (!hasWaterInCan) {
                                    sendMessage(player, "You need a watering can to plant this.")
                                    return@on true
                                }
                                continue
                            }
                            if (!inInventory(player, tool)) {
                                closeInterface(player)
                                sendMessage(player, "You need a ${ItemDefinition.forId(tool).name} to build this.")
                                return@on true
                            }
                        }
                        if (flatpackMode && WorkbenchListeners.getBenchLevel(getAttribute(player, WorkbenchListeners.ATTRIBUTE_WORKBENCH_ID, SceneryObj.WORKBENCH_13704)) < deco.level) {
                            closeInterface(player)
                            sendMessage(player, "You cannot make this with this workbench.") // TODO: Find authentic text
                            return@on true
                        }
                    }
                    if (flatpackMode) {
                        WorkbenchListeners.produceFlatpack(player, deco)
                    } else {
                        BuildingUtils.buildDecoration(player, hotspot, deco, hotspotObj)
                    }
                    return@on true
                }
                else -> return@on false
            }
        }

        /**
         * Handles the interface for managing building mode and POH guests.
         */
        on(Components.POH_HOUSE_OPTIONS_398) { player, _, _, buttonID, _, _ ->
            when (buttonID) {
                HOUSE_BUTTON_BUILDMODE_ON -> {
                    player.houseManager.toggleBuildingMode(player, true)
                    return@on true
                }
                HOUSE_BUTTON_BUILDMODE_OFF -> {
                    player.houseManager.toggleBuildingMode(player, false)
                    return@on true
                }
                HOUSE_BUTTON_EXPEL_GUESTS -> {
                    player.houseManager.expelGuests(player)
                    return@on true
                }
                HOUSE_BUTTON_LEAVE_HOUSE -> {
                    if (!player.houseManager.isInHouse(player)) {
                        sendMessage(player, "You can't do this outside of your house.")
                        return@on true
                    }
                    HouseManager.leave(player)
                    return@on true
                }
                else -> return@on false
            }
        }

        /**
         * Handles the interface for selecting a new room to add to a POH.
         */
        on(Components.POH_ROOMS_402) { player, _, _, buttonID, _, _ ->
            val index = buttonID - 160
            if (index > -1 && index < RoomProperties.values().size) {
                player.dialogueInterpreter.open("con:room", RoomProperties.values()[index]) // ContentAPI impl won't work for this
            }
            return@on true
        }
    }
}