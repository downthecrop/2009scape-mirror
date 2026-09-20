package content.region.misthalin.wiztower.handlers.rcguild

import content.global.skill.runecrafting.Tiara
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import org.rs09.consts.*
import kotlin.collections.toIntArray

/**
 * The Runecrafting Guild
 *
 * Source: https://www.youtube.com/watch?v=hEMS7v7y_PU
 * Source: https://web.archive.org/web/20081217121955/http://runescape.wikia.com/wiki/Runecrafting_Guild
 */

class RunecraftingGuildListeners : InteractionListener  {

    companion object {
        const val SPHERES_ANIM = 10128
        const val RUNESTONE_ANIM = 10196
        const val CONTAINMENT_ANIM = 10197
        const val GYRO_ANIM = 10127
    }

    // hats that can have goggles up or down
    val rcHats = intArrayOf(
        // goggles up                // goggles down
        Items.RUNECRAFTER_HAT_13615, Items.RUNECRAFTER_HAT_13616, // yellow
        Items.RUNECRAFTER_HAT_13625, Items.RUNECRAFTER_HAT_13626, // blue
        Items.RUNECRAFTER_HAT_13620, Items.RUNECRAFTER_HAT_13621  // green
    )

    // talismans that can be used on the guild map
    val rcTalismans = Tiara.values().map { it.item.id }.toIntArray()

    override fun defineListeners() {

        // entry/exit portal
        on(Scenery.PORTAL_38279, IntType.SCENERY, "enter") { player, node ->
            if (player.location.y < 4000) {
                // requires 50 rc to enter
                if (getStatLevel(player, Skills.RUNECRAFTING) < 50) {
                    sendMessage(player, "You need level 50 Runecrafting to access the Runecrafting Guild.")
                    return@on false
                } else {
                    // if you are at wizard's tower, tele to guild
                    face(player, node)
                    teleport(player, Location.create(1696, 5460, 2), TeleportManager.TeleportType.RC_GUILD)
                    sendMessage(player, "You enter the portal.")
                }
            } else {
                // if you are in guild, tele back to tower
                face(player, node)
                teleport(player, Location.create(3106, 3160, 1), TeleportManager.TeleportType.RC_GUILD)
                sendMessage(player, "You enter the portal.")
            }
            return@on true
        }

        // todo one of the spheres is at a diagonal and animateScenery moves it to a cardinal direction
        on(Scenery.GLASS_SPHERES_38331, IntType.SCENERY, "activate") { _, node ->
            val spheres = node as core.game.node.scenery.Scenery
            animateScenery(spheres, SPHERES_ANIM)
            return@on true
        }

        on(Scenery.RUNESTONE_ACCELERATOR_38329, IntType.SCENERY, "activate") { _, node ->
            animateScenery(node as core.game.node.scenery.Scenery, RUNESTONE_ANIM)
            return@on true
        }

        on(Scenery.CONTAINMENT_UNIT_38327, IntType.SCENERY, "activate") { _, node ->
            animateScenery(node as core.game.node.scenery.Scenery, CONTAINMENT_ANIM)
            return@on true
        }

        on(Scenery.GYROSCOPE_38330, IntType.SCENERY, "activate") { _, node ->
            animateScenery(node as core.game.node.scenery.Scenery, GYRO_ANIM)
            return@on true
        }

        // rune altar map
        on(Scenery.MAP_TABLE_38315, IntType.SCENERY, "study") { player, _ ->
            openInterface(player, Components.RCGUILD_MAP_780)
            return@on true
        }

        // rune altar map
        onUseWith(IntType.SCENERY, rcTalismans, Scenery.MAP_TABLE_38315) { player, used, _ ->
            when(used.id) {
                Items.AIR_TALISMAN_1438 -> setAttribute(player, MapTableInterface.attrAir, true)
                Items.MIND_TALISMAN_1448 -> setAttribute(player, MapTableInterface.attrMind, true)
                Items.WATER_TALISMAN_1444 -> setAttribute(player, MapTableInterface.attrWater, true)
                Items.EARTH_TALISMAN_1440 -> setAttribute(player, MapTableInterface.attrEarth, true)
                Items.FIRE_TALISMAN_1442 -> setAttribute(player, MapTableInterface.attrFire, true)
                Items.BODY_TALISMAN_1446 -> setAttribute(player, MapTableInterface.attrBody, true)
                Items.COSMIC_TALISMAN_1454 -> setAttribute(player, MapTableInterface.attrCosmic, true)
                Items.CHAOS_TALISMAN_1452 -> setAttribute(player, MapTableInterface.attrChaos, true)
                Items.NATURE_TALISMAN_1462 -> setAttribute(player, MapTableInterface.attrNature, true)
                Items.LAW_TALISMAN_1458 -> setAttribute(player, MapTableInterface.attrLaw, true)
                Items.DEATH_TALISMAN_1456 -> setAttribute(player, MapTableInterface.attrDeath, true)
                Items.BLOOD_TALISMAN_1450 -> setAttribute(player, MapTableInterface.attrBlood, true)
                Items.ELEMENTAL_TALISMAN_5516 -> {
                    setAttribute(player, MapTableInterface.attrAir, true)
                    setAttribute(player, MapTableInterface.attrWater, true)
                    setAttribute(player, MapTableInterface.attrEarth, true)
                    setAttribute(player, MapTableInterface.attrFire, true)
                }
                Items.OMNI_TALISMAN_13649 -> {
                    setAttribute(player, MapTableInterface.attrAir, true)
                    setAttribute(player, MapTableInterface.attrMind, true)
                    setAttribute(player, MapTableInterface.attrWater, true)
                    setAttribute(player, MapTableInterface.attrEarth, true)
                    setAttribute(player, MapTableInterface.attrFire, true)
                    setAttribute(player, MapTableInterface.attrBody, true)
                    setAttribute(player, MapTableInterface.attrCosmic, true)
                    setAttribute(player, MapTableInterface.attrChaos, true)
                    setAttribute(player, MapTableInterface.attrNature, true)
                    setAttribute(player, MapTableInterface.attrLaw, true)
                    setAttribute(player, MapTableInterface.attrDeath, true)
                    setAttribute(player, MapTableInterface.attrBlood, true)

                }
            }
            return@onUseWith true
        }

        // bookcase scenery I noticed in a vid had different search text than default
        on(intArrayOf(Scenery.BOOKCASE_38322, Scenery.BOOKCASE_38323, Scenery.BOOKCASE_38324), IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You search the books...")
            sendMessage(player, "None of them look very interesting.")
            return@on true
        }

        // goggles on or off
        on(rcHats, IntType.ITEM, "goggles") { player, item ->
            when(item.id) {
                Items.RUNECRAFTER_HAT_13615 -> replaceSlot(player, item.asItem().slot, Item(Items.RUNECRAFTER_HAT_13616))
                Items.RUNECRAFTER_HAT_13616 -> replaceSlot(player, item.asItem().slot, Item(Items.RUNECRAFTER_HAT_13615))
                Items.RUNECRAFTER_HAT_13625 -> replaceSlot(player, item.asItem().slot, Item(Items.RUNECRAFTER_HAT_13626))
                Items.RUNECRAFTER_HAT_13626 -> replaceSlot(player, item.asItem().slot, Item(Items.RUNECRAFTER_HAT_13625))
                Items.RUNECRAFTER_HAT_13620 -> replaceSlot(player, item.asItem().slot, Item(Items.RUNECRAFTER_HAT_13621))
                Items.RUNECRAFTER_HAT_13621 -> replaceSlot(player, item.asItem().slot, Item(Items.RUNECRAFTER_HAT_13620))
            }
            return@on true
        }

        // goggles on or off
        on(rcHats, IntType.ITEM, "operate") { player, item ->
            when(item.id) {
                Items.RUNECRAFTER_HAT_13615 -> {
                    replaceSlot(player, item.asItem().slot, Item(Items.RUNECRAFTER_HAT_13616), container = Container.EQUIPMENT)
                }
                Items.RUNECRAFTER_HAT_13616 -> {
                    replaceSlot(player, item.asItem().slot, Item(Items.RUNECRAFTER_HAT_13615), container = Container.EQUIPMENT)
                }
                Items.RUNECRAFTER_HAT_13625 -> {
                    replaceSlot(player, item.asItem().slot, Item(Items.RUNECRAFTER_HAT_13626), container = Container.EQUIPMENT)
                }
                Items.RUNECRAFTER_HAT_13626 -> {
                    replaceSlot(player, item.asItem().slot, Item(Items.RUNECRAFTER_HAT_13625), container = Container.EQUIPMENT)
                }
                Items.RUNECRAFTER_HAT_13620 -> {
                    replaceSlot(player, item.asItem().slot, Item(Items.RUNECRAFTER_HAT_13621), container = Container.EQUIPMENT)
                }
                Items.RUNECRAFTER_HAT_13621 -> {
                    replaceSlot(player, item.asItem().slot, Item(Items.RUNECRAFTER_HAT_13620), container = Container.EQUIPMENT)
                }
            }
            return@on true
        }
    }
}