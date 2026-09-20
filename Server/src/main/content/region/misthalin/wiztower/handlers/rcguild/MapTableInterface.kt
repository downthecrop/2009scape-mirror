package content.region.misthalin.wiztower.handlers.rcguild

import core.api.*
import core.api.setComponentVisibility
import core.game.container.access.InterfaceContainer
import core.game.interaction.InterfaceListener
import core.game.node.item.Item
import org.rs09.consts.Components
import org.rs09.consts.Items

/**
 * Runecrafting Guild Map Table
 */

class MapTableInterface : InterfaceListener {

    // this is partially controlled with 728.cs2

    // attributes used to track using a talisman with the map
    companion object {
        const val attrAir = "rcmap-air"
        const val attrMind = "rcmap-mind"
        const val attrWater = "rcmap-water"
        const val attrEarth = "rcmap-earth"
        const val attrFire = "rcmap-fire"
        const val attrBody = "rcmap-body"
        const val attrCosmic = "rcmap-cosmic"
        const val attrChaos = "rcmap-chaos"
        const val attrNature = "rcmap-nature"
        const val attrLaw = "rcmap-law"
        const val attrDeath = "rcmap-death"
        const val attrBlood = "rcmap-blood"
    }

    // interface components
    private val MAP = Components.RCGUILD_MAP_780
    private val MAP_SIDE = Components.RCGUILD_SIDE_778

    // child components of the main map interface
    private val AIR = 35
    private val BODY = 36
    private val MIND = 37
    private val EARTH = 38
    private val WATER = 39
    private val FIRE = 40
    private val CHAOS = 41
    private val LAW = 42
    private val BLOOD = 43
    private val NATURE = 44
    private val DEATH = 47
    private val COSMIC = 48

    override fun defineInterfaceListeners() {
        onOpen(MAP) { player, _ ->

            // the side tab where you can click talismans to show the altars
            openSingleTab(player, MAP_SIDE)

            // show components if the player used the talisman with the map earlier
            if (getAttribute(player, attrAir, false))    setComponentVisibility(player, MAP, AIR, false)
            if (getAttribute(player, attrMind, false))   setComponentVisibility(player, MAP, MIND, false)
            if (getAttribute(player, attrWater, false))  setComponentVisibility(player, MAP, WATER, false)
            if (getAttribute(player, attrEarth, false))  setComponentVisibility(player, MAP, EARTH, false)
            if (getAttribute(player, attrFire, false))   setComponentVisibility(player, MAP, FIRE, false)
            if (getAttribute(player, attrBody, false))   setComponentVisibility(player, MAP, BODY, false)
            if (getAttribute(player, attrCosmic, false)) setComponentVisibility(player, MAP, COSMIC, false)
            if (getAttribute(player, attrChaos, false))  setComponentVisibility(player, MAP, CHAOS, false)
            if (getAttribute(player, attrNature, false)) setComponentVisibility(player, MAP, NATURE, false)
            if (getAttribute(player, attrLaw, false))    setComponentVisibility(player, MAP, LAW, false)
            if (getAttribute(player, attrDeath, false))  setComponentVisibility(player, MAP, DEATH, false)
            if (getAttribute(player, attrBlood, false))  setComponentVisibility(player, MAP, BLOOD, false)

            return@onOpen true
        }

        // restore all tabs
        onClose(MAP) { player, _ ->
            player.interfaceManager.closeSingleTab()
            return@onClose true
        }

        // populate the side interface with the player's inventory
        onOpen(MAP_SIDE) { player, _ ->
            InterfaceContainer.generateItems(
                player,
                player.inventory.toArray(),
                arrayOf("Use"),
                MAP_SIDE,
                0,
                7,
                4
            )
            return@onOpen true
        }

        // when the player clicks an inventory item, display the altar if it's a talisman
        on(MAP_SIDE) { player, _, opcode, _, slot, _ ->

            // get the item clicked
            val id = player.inventory[slot].id

            // the use option
            if (opcode == 155) {
                when(id) {
                    Items.AIR_TALISMAN_1438 ->    setComponentVisibility(player, MAP, AIR, false)
                    Items.MIND_TALISMAN_1448 ->   setComponentVisibility(player, MAP, MIND, false)
                    Items.WATER_TALISMAN_1444 ->  setComponentVisibility(player, MAP, WATER, false)
                    Items.EARTH_TALISMAN_1440 ->  setComponentVisibility(player, MAP, EARTH, false)
                    Items.FIRE_TALISMAN_1442 ->   setComponentVisibility(player, MAP, FIRE, false)
                    Items.BODY_TALISMAN_1446 ->   setComponentVisibility(player, MAP, BODY, false)
                    Items.COSMIC_TALISMAN_1454 -> setComponentVisibility(player, MAP, COSMIC, false)
                    Items.CHAOS_TALISMAN_1452 ->  setComponentVisibility(player, MAP, CHAOS, false)
                    Items.NATURE_TALISMAN_1462 -> setComponentVisibility(player, MAP, NATURE, false)
                    Items.LAW_TALISMAN_1458 ->    setComponentVisibility(player, MAP, LAW, false)
                    Items.DEATH_TALISMAN_1456 ->  setComponentVisibility(player, MAP, DEATH, false)
                    Items.BLOOD_TALISMAN_1450 ->  setComponentVisibility(player, MAP, BLOOD, false)
                    Items.ELEMENTAL_TALISMAN_5516 -> {
                        setComponentVisibility(player, MAP, AIR, false)
                        setComponentVisibility(player, MAP, WATER, false)
                        setComponentVisibility(player, MAP, EARTH, false)
                        setComponentVisibility(player, MAP, FIRE, false)
                    }
                    Items.OMNI_TALISMAN_13649 -> {
                        setComponentVisibility(player, MAP, AIR, false)
                        setComponentVisibility(player, MAP, MIND, false)
                        setComponentVisibility(player, MAP, WATER, false)
                        setComponentVisibility(player, MAP, EARTH, false)
                        setComponentVisibility(player, MAP, FIRE, false)
                        setComponentVisibility(player, MAP, BODY, false)
                        setComponentVisibility(player, MAP, COSMIC, false)
                        setComponentVisibility(player, MAP, CHAOS, false)
                        setComponentVisibility(player, MAP, NATURE, false)
                        setComponentVisibility(player, MAP, LAW, false)
                        setComponentVisibility(player, MAP, DEATH, false)
                        setComponentVisibility(player, MAP, BLOOD, false)

                    }
                }
            }

            // the examine option
            if (opcode == 9) {
                val examine = Item(id).definition.examine
                sendMessage(player, "$examine")
            }

            return@on true
        }
    }
}