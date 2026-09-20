package content.global.handlers.item

import content.global.skill.runecrafting.Altar
import content.global.skill.runecrafting.MysteriousRuinListener
import content.region.kandarin.ardougne.quest.plaguecity.PlagueCityListeners.Companion.ARDOUGNE_TELE_ATTRIBUTE
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item
import core.game.world.map.Location
import org.rs09.consts.Items

class TeleTabsListener : InteractionListener {

    enum class TeleTabs(val item: Int, val location: Location, val exp: Double, val requirementCheck: (Player) -> Boolean = { true }) {
        ADDOUGNE_TELEPORT(Items.ARDOUGNE_TELEPORT_8011, Location.create(2662, 3307, 0), 61.0, {
            player -> getAttribute(player, ARDOUGNE_TELE_ATTRIBUTE, false)
        }),
        CAMELOT_TELEPORT(Items.CAMELOT_TELEPORT_8010, Location.create(2757, 3477, 0), 55.5),
        FALADOR_TELEPORT(Items.FALADOR_TELEPORT_8009, Location.create(2966, 3380, 0), 47.0),
        LUMBRIDGE_TELEPORT(Items.LUMBRIDGE_TELEPORT_8008, Location.create(3222, 3218, 0), 41.0),
        VARROCK_TELEPORT(Items.VARROCK_TELEPORT_8007, Location.create(3212, 3423, 0), 35.00),
        WATCH_TOWER_TELEPORT(Items.WATCHTOWER_TPORT_8012, Location.create(2548, 3114, 0), 68.00),

        // runecrafting tabs have the same requirements that the altars do: https://runescape.wiki/w/Runecrafting_teleport_tablets?oldid=665784
        RC_GUILD_TELEPORT(Items.RUNECRAFTING_GUILD_TELEPORT_13598, Location.create(1696, 5460, 2), 0.0),
        AIR_ALTAR_TELEPORT(Items.AIR_ALTAR_TELEPORT_13599, Location.create(2978, 3296, 0), 0.0, {
                player -> MysteriousRuinListener.checkReq(player, Altar.AIR)
        }),
        MIND_ALTAR_TELEPORT(Items.MIND_ALTAR_TELEPORT_13600, Location.create(2979, 3510, 0), 0.0, {
                player -> MysteriousRuinListener.checkReq(player, Altar.MIND)
        }),
        WATER_ALTAR_TELEPORT(Items.WATER_ALTAR_TELEPORT_13601, Location.create(3182, 3162, 0), 0.0, {
                player -> MysteriousRuinListener.checkReq(player, Altar.WATER)
        }),
        EARTH_ALTAR_TELEPORT(Items.EARTH_ALTAR_TELEPORT_13602, Location.create(3304, 3472, 0), 0.0, {
                player -> MysteriousRuinListener.checkReq(player, Altar.EARTH)
        }),
        FIRE_ALTAR_TELEPORT(Items.FIRE_ALTAR_TELEPORT_13603, Location.create(3311, 3252, 0), 0.0, {
                player -> MysteriousRuinListener.checkReq(player, Altar.FIRE)
        }),
        BODY_ALTAR_TELEPORT(Items.BODY_ALTAR_TELEPORT_13604, Location.create(3055, 3443, 0), 0.0, {
                player -> MysteriousRuinListener.checkReq(player, Altar.BODY)
        }),
        COSMIC_ALTAR_TELEPORT(Items.COSMIC_ALTAR_TELEPORT_13605, Location.create(2411, 4380, 0), 0.0, {
                player -> MysteriousRuinListener.checkReq(player, Altar.COSMIC)
        }),
        CHAOS_ALTAR_TELEPORT(Items.CHAOS_ALTAR_TELEPORT_13606, Location.create(3058, 3593, 0), 0.0, {
                player -> MysteriousRuinListener.checkReq(player, Altar.CHAOS)
        }),
        NATURE_ALTAR_TELEPORT(Items.NATURE_ALTAR_TELEPORT_13607, Location.create(2868, 3013, 0), 0.0, {
                player -> MysteriousRuinListener.checkReq(player, Altar.NATURE)
        }),
        LAW_ALTAR_TELEPORT(Items.LAW_ALTAR_TELEPORT_13608, Location.create(2857, 3378, 0), 0.0, {
                player -> MysteriousRuinListener.checkReq(player, Altar.LAW)
        }),
        DEATH_ALTAR_TELEPORT(Items.DEATH_ALTAR_TELEPORT_13609, Location.create(1863, 4639, 0), 0.0, {
                player -> MysteriousRuinListener.checkReq(player, Altar.DEATH)
        }),
        BLOOD_ALTAR_TELEPORT(Items.BLOOD_ALTAR_TELEPORT_13610, Location.create(3559, 9778, 0), 0.0, {
                player -> MysteriousRuinListener.checkReq(player, Altar.BLOOD)
        }),
        ASTRAL_ALTAR_TELEPORT(Items.ASTRAL_ALTAR_TELEPORT_13611, Location.create(2156, 3862, 0), 0.0, {
                player -> MysteriousRuinListener.checkReq(player, Altar.ASTRAL)
        });

        companion object {
            val idMap = values().map { it.item to it }.toMap()
            fun forId(id: Int): TeleTabs? {
                return idMap[id]
            }
        }
    }

    override fun defineListeners() {
        val tabIDs = TeleTabs.values().map { it.item }.toIntArray()
        on(tabIDs, IntType.ITEM, "break") {player, node ->
            val tab = node.id
            val tabEnum = TeleTabs.forId(tab)
            if (tabEnum != null && inInventory(player,tab)) {
                val tabloc = tabEnum.location
                if (inInventory(player, tab)) {
                    if (tabEnum.requirementCheck(player)){
                        if (teleport(player, tabloc, TeleportManager.TeleportType.TELETABS)) {
                            removeItem(player, Item(node.id, 1))
                        }
                    }
                    else {
                        when (tabEnum){
                            TeleTabs.ADDOUGNE_TELEPORT -> sendMessage(player, "You need to complete Plague City to use this tablet.")
                            else -> sendMessage(player, "A strange power blocks your teleport.")
                        }
                    }
                }
            }
            return@on true
        }
    }
}