package content.global.handlers.item

import core.api.inInventory
import core.api.removeItem
import core.api.teleport
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item
import core.game.world.map.Location
import core.api.hasRequirement;

class TeleTabsListener : InteractionListener {

    enum class TeleTabs(val item: Int, val location: Location, val exp: Double, val requirementCheck: (Player) -> Boolean = { true }) {
        ADDOUGNE_TELEPORT(8011, Location.create(2662, 3307, 0), 61.0, {
            player -> hasRequirement(player, "Plague City");
        }),
        AIR_ALTAR_TELEPORT(13599, Location.create(2978, 3296, 0), 0.0),
        ASTRAL_ALTAR_TELEPORT(13611, Location.create(2156, 3862, 0), 0.0),
        BLOOD_ALTAR_TELEPORT(13610, Location.create(3559, 9778, 0), 0.0),
        BODY_ALTAR_TELEPORT(13604, Location.create(3055, 3443, 0), 0.0),
        CAMELOT_TELEPORT(8010, Location.create(2757, 3477, 0), 55.5),
        CHAOS_ALTAR_TELEPORT(13606, Location.create(3058, 3593, 0), 0.0),
        COSMIC_ALTAR_TELEPORT(13605, Location.create(2411, 4380, 0), 0.0),
        DEATH_ALTAR_TELEPORT(13609, Location.create(1863, 4639, 0), 0.0),
        EARTH_ALTAR_TELEPORT(13602, Location.create(3304, 3472, 0), 0.0),
        FALADOR_TELEPORT(8009, Location.create(2966, 3380, 0), 47.0),
        FIRE_ALTAR_TELEPORT(13603, Location.create(3311, 3252, 0), 0.0),
        LAW_ALTAR_TELEPORT(13608, Location.create(2857, 3378, 0), 0.0),
        LUMBRIDGE_TELEPORT(8008, Location.create(3222, 3218, 0), 41.0),
        MIND_ALTAR_TELEPORT(13600, Location.create(2979, 3510, 0), 0.0),
        NATURE_ALTAR_TELEPORT(13607, Location.create(2868, 3013, 0), 0.0),
        VARROCK_TELEPORT(8007, Location.create(3212, 3423, 0), 35.00),
        WATCH_TOWER_TELEPORT(8012, Location.create(2548, 3114, 0), 68.00),
        WATER_ALTAR_TELEPORT(13601, Location.create(3182, 3162, 0), 0.0);

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
                if (inInventory(player, tab) && tabEnum.requirementCheck(player)) {
                    if (teleport(player, tabloc, TeleportManager.TeleportType.TELETABS)) {
                        removeItem(player, Item(node.id, 1))
                    }
                }
            }
            return@on true
        }
    }
}