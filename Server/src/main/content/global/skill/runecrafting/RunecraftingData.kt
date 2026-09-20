package content.global.skill.runecrafting

import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import content.global.skill.runecrafting.MysteriousRuinListener.Companion.AIR_VARBIT
import content.global.skill.runecrafting.MysteriousRuinListener.Companion.BLOOD_VARBIT
import content.global.skill.runecrafting.MysteriousRuinListener.Companion.BODY_VARBIT
import content.global.skill.runecrafting.MysteriousRuinListener.Companion.CHAOS_VARBIT
import content.global.skill.runecrafting.MysteriousRuinListener.Companion.COSMIC_VARBIT
import content.global.skill.runecrafting.MysteriousRuinListener.Companion.DEATH_VARBIT
import content.global.skill.runecrafting.MysteriousRuinListener.Companion.EARTH_VARBIT
import content.global.skill.runecrafting.MysteriousRuinListener.Companion.FIRE_VARBIT
import content.global.skill.runecrafting.MysteriousRuinListener.Companion.LAW_VARBIT
import content.global.skill.runecrafting.MysteriousRuinListener.Companion.MIND_VARBIT
import content.global.skill.runecrafting.MysteriousRuinListener.Companion.NATURE_VARBIT
import content.global.skill.runecrafting.MysteriousRuinListener.Companion.WATER_VARBIT
import core.api.*
import org.rs09.consts.Items
import org.rs09.consts.Scenery

/**
 * RunecraftingData holds classes for the runecrafting staves, tiaras, talismans, mysterious ruins, and altars.
 */

/**
 * Helper extension to map related runecrafting enums by their shared element name (e.g., AIR to AIR).
 */
inline fun <reified T : Enum<T>> Enum<*>.mapped(): T? = runCatching { enumValueOf<T>(name) }.getOrNull()

/**
 * Represents a staff item and its corresponding information.
 */
enum class Staff(val item: Item) {
    AIR(Item(Items.AIR_TALISMAN_STAFF_13630)),
    MIND(Item(Items.MIND_TALISMAN_STAFF_13631)),
    WATER(Item(Items.WATER_TALISMAN_STAFF_13632)),
    EARTH(Item(Items.EARTH_TALISMAN_STAFF_13633)),
    FIRE(Item(Items.FIRE_TALISMAN_STAFF_13634)),
    BODY(Item(Items.BODY_TALISMAN_STAFF_13635)),
    COSMIC(Item(Items.COSMIC_TALISMAN_STAFF_13636)),
    CHAOS(Item(Items.CHAOS_TALISMAN_STAFF_13637)),
    NATURE(Item(Items.NATURE_TALISMAN_STAFF_13638)),
    LAW(Item(Items.LAW_TALISMAN_STAFF_13639)),
    DEATH(Item(Items.DEATH_TALISMAN_STAFF_13640)),
    BLOOD(Item(Items.BLOOD_TALISMAN_STAFF_13641)),
    OMNI(Item(Items.OMNI_TALISMAN_STAFF_13642));

    val talisman: Talisman? get() = mapped<Talisman>()

    companion object {
        @JvmStatic
        fun forItem(item: Item?) = item?.let { i -> values().find { it.item.id == i.id } }
    }
}

/**
 * Represents a tiara item and its corresponding information.
 */
enum class Tiara(val item: Item, val experience: Double) {
    AIR(Item(Items.AIR_TIARA_5527), 25.0),
    MIND(Item(Items.MIND_TIARA_5529), 27.5),
    WATER(Item(Items.WATER_TIARA_5531), 30.0),
    EARTH(Item(Items.EARTH_TIARA_5535), 32.5),
    FIRE(Item(Items.FIRE_TIARA_5537), 35.0),
    BODY(Item(Items.BODY_TIARA_5533), 37.5),
    COSMIC(Item(Items.COSMIC_TIARA_5539), 40.0),
    CHAOS(Item(Items.CHAOS_TIARA_5543), 43.5),
    ASTRAL(Item(Items.ASTRAL_TIARA_9106), 43.5),
    NATURE(Item(Items.NATURE_TIARA_5541), 45.0),
    LAW(Item(Items.LAW_TIARA_5545), 47.5),
    DEATH(Item(Items.DEATH_TIARA_5547), 50.0),
    //SOUL(Item(Items.SOUL_TIARA_5551), 55.0),
    BLOOD(Item(Items.BLOOD_TIARA_5549), 52.5),
    OMNI(Item(Items.OMNI_TIARA_13655), 0.0);

    val talisman: Talisman? get() = mapped<Talisman>()

    companion object {
        @JvmStatic
        fun forItem(item: Item?) = item?.let { i -> values().find { it.item.id == i.id } }
    }
}

/**
 * Represents a talisman item.
 */
enum class Talisman(val item: Item) {
    AIR(Item(Items.AIR_TALISMAN_1438)),
    MIND(Item(Items.MIND_TALISMAN_1448)),
    WATER(Item(Items.WATER_TALISMAN_1444)),
    EARTH(Item(Items.EARTH_TALISMAN_1440)),
    FIRE(Item(Items.FIRE_TALISMAN_1442)),
    BODY(Item(Items.BODY_TALISMAN_1446)),
    COSMIC(Item(Items.COSMIC_TALISMAN_1454)),
    LAW(Item(Items.LAW_TALISMAN_1458)),
    NATURE(Item(Items.NATURE_TALISMAN_1462)),
    CHAOS(Item(Items.CHAOS_TALISMAN_1452)),
    DEATH(Item(Items.DEATH_TALISMAN_1456)),
    //SOUL(Item(Items.SOUL_TALISMAN_1460)),
    BLOOD(Item(Items.BLOOD_TALISMAN_1450)),
    ELEMENTAL(Item(Items.ELEMENTAL_TALISMAN_5516)),
    OMNI(Item(Items.OMNI_TALISMAN_13649));

    val ruin: MysteriousRuins? get() = mapped<MysteriousRuins>()
    val tiara: Tiara? get() = mapped<Tiara>()
    val staff: Staff? get() = mapped<Staff>()

    fun locate(player: Player) {
        val targetRuin = ruin
        if (this == ELEMENTAL || targetRuin == null) {
            sendMessage(player, "You cannot tell which direction the talisman is pulling...")
            return
        }

        val loc = targetRuin.base
        val pLoc = player.location

        val direction = when {
            pLoc.y > loc.y && pLoc.x - 1 > loc.x -> "south-west"
            pLoc.x < loc.x && pLoc.y > loc.y -> "south-east"
            pLoc.x > loc.x + 1 && pLoc.y < loc.y -> "north-west"
            pLoc.x < loc.x && pLoc.y < loc.y -> "north-east"
            pLoc.y < loc.y -> "north"
            pLoc.y > loc.y -> "south"
            pLoc.x < loc.x + 1 -> "east"
            pLoc.x > loc.x + 1 -> "west"
            else -> ""
        }

        if (direction.isNotEmpty()) {
            sendMessage(player, "The talisman pulls towards the $direction.")
        }
    }

    companion object {
        @JvmStatic
        fun forItem(item: Item?) = item?.let { i -> values().find { it.item.id == i.id } }

        @JvmStatic
        fun forName(name: String) = runCatching { valueOf(name) }.getOrNull()
    }
}

/**
 * Represents an overworld mysterious ruin.
 */
enum class MysteriousRuins(val objectIds: IntArray, val base: Location, val end: Location) {
    AIR(intArrayOf(AIR_VARBIT, Scenery.MYSTERIOUS_RUINS_7103, Scenery.MYSTERIOUS_RUINS_7104), Location.create(2983, 3292, 0), Location.create(2841, 4829, 0)),
    MIND(intArrayOf(MIND_VARBIT, Scenery.MYSTERIOUS_RUINS_7105, Scenery.MYSTERIOUS_RUINS_7106), Location.create(2980, 3514, 0), Location.create(2793, 4828, 0)),
    WATER(intArrayOf(WATER_VARBIT, Scenery.MYSTERIOUS_RUINS_7107, Scenery.MYSTERIOUS_RUINS_7108), Location.create(3183, 3163, 0), Location.create(3482, 4838, 0)),
    EARTH(intArrayOf(EARTH_VARBIT, Scenery.MYSTERIOUS_RUINS_7109, Scenery.MYSTERIOUS_RUINS_7110), Location.create(3304, 3475, 0), Location.create(2655, 4830, 0)),
    FIRE(intArrayOf(FIRE_VARBIT, Scenery.MYSTERIOUS_RUINS_7111, Scenery.MYSTERIOUS_RUINS_7112), Location.create(3312, 3253, 0), Location.create(2574, 4849, 0)),
    BODY(intArrayOf(BODY_VARBIT, Scenery.MYSTERIOUS_RUINS_7113, Scenery.MYSTERIOUS_RUINS_7114), Location.create(3051, 3443, 0), Location.create(2521, 4834, 0)),
    COSMIC(intArrayOf(COSMIC_VARBIT, Scenery.MYSTERIOUS_RUINS_7115, Scenery.MYSTERIOUS_RUINS_7116), Location.create(2406, 4375, 0), Location.create(2162, 4833, 0)),
    LAW(intArrayOf(LAW_VARBIT, Scenery.MYSTERIOUS_RUINS_7117, Scenery.MYSTERIOUS_RUINS_7118), Location.create(2857, 3379, 0), Location.create(2464, 4819, 0)),
    NATURE(intArrayOf(NATURE_VARBIT, Scenery.MYSTERIOUS_RUINS_7119, Scenery.MYSTERIOUS_RUINS_7120), Location.create(2869, 3021, 0), Location.create(2400, 4835, 0)),
    CHAOS(intArrayOf(CHAOS_VARBIT, Scenery.MYSTERIOUS_RUINS_7121, Scenery.MYSTERIOUS_RUINS_7122), Location.create(3058, 3589, 0), Location.create(2275, 4847, 3)),
    DEATH(intArrayOf(DEATH_VARBIT, Scenery.MYSTERIOUS_RUINS_7123, Scenery.MYSTERIOUS_RUINS_7124), Location.create(1862, 4639, 0), Location.create(2208, 4830, 0)),
    //SOUL(intArrayOf(SOUL_VARBIT, Scenery.MYSTERIOUS_RUINS_7125, Scenery.MYSTERIOUS_RUINS_7126), null, null),
    BLOOD(intArrayOf(BLOOD_VARBIT, Scenery.STRANGE_STONES_30529, Scenery.STRANGE_STONES_30530), Location.create(3561, 9779, 0), Location.create(2467, 4889, 1));

    val talisman: Talisman? get() = mapped<Talisman>()
    val tiara: Tiara? get() = mapped<Tiara>()
    val staff: Staff? get() = mapped<Staff>()

    companion object {
        @JvmStatic
        fun forObject(obj: core.game.node.scenery.Scenery) = values().find { obj.id in it.objectIds }
    }
}

/**
 * Represents the altar inside the rift.
 */
enum class Altar(val objectId: Int, val portal: Int, val riftId: Int, val rune: Rune?) {
    AIR(Scenery.ALTAR_2478, Scenery.PORTAL_2465, Scenery.AIR_RIFT_7139, Rune.AIR),
    MIND(Scenery.ALTAR_2479, Scenery.PORTAL_2466, Scenery.MIND_RIFT_7140, Rune.MIND),
    WATER(Scenery.ALTAR_2480, Scenery.PORTAL_2467, Scenery.WATER_RIFT_7137, Rune.WATER),
    EARTH(Scenery.ALTAR_2481, Scenery.PORTAL_2468, Scenery.EARTH_RIFT_7130, Rune.EARTH),
    FIRE(Scenery.ALTAR_2482, Scenery.PORTAL_2469, Scenery.FIRE_RIFT_7129, Rune.FIRE),
    BODY(Scenery.ALTAR_2483, Scenery.PORTAL_2470, Scenery.BODY_RIFT_7131, Rune.BODY),
    COSMIC(Scenery.ALTAR_2484, Scenery.PORTAL_2471, Scenery.COSMIC_RIFT_7132, Rune.COSMIC),
    LAW(Scenery.ALTAR_2485, Scenery.PORTAL_2472, Scenery.LAW_RIFT_7135, Rune.LAW),
    NATURE(Scenery.ALTAR_2486, Scenery.PORTAL_2473, Scenery.NATURE_RIFT_7133, Rune.NATURE),
    CHAOS(Scenery.ALTAR_2487, Scenery.PORTAL_2474, Scenery.CHAOS_RIFT_7134, Rune.CHAOS),
    ASTRAL(Scenery.ALTAR_17010, 0, 0, Rune.ASTRAL),
    DEATH(Scenery.ALTAR_2488, Scenery.PORTAL_2475, Scenery.DEATH_RIFT_7136, Rune.DEATH),
    //SOUL(Scenery.ALTAR_2489, Scenery.PORTAL_2476, Scenery.SOUL_RIFT_7138, Rune.SOUL),
    BLOOD(Scenery.ALTAR_30624, Scenery.PORTAL_2477, Scenery.BLOOD_RIFT_7141, Rune.BLOOD),
    OURANIA(Scenery.RUNECRAFTING_ALTAR_26847, 0, 0, null);

    val ruin: MysteriousRuins? get() = mapped<MysteriousRuins>()
    val talisman: Talisman? get() = mapped<Talisman>()
    val tiara: Tiara? get() = mapped<Tiara>()
    val staff: Staff? get() = mapped<Staff>()

    val isOurania: Boolean get() = rune == null

    fun enterRift(player: Player) {
        if (MysteriousRuinListener.checkReq(player, this)) {
            teleport(player, ruin?.end ?: player.location)
        }
    }

    companion object {
        @JvmStatic
        fun forObject(obj: core.game.node.scenery.Scenery) = values().find {
            it.objectId == obj.id || it.portal == obj.id || it.riftId == obj.id
        }
    }
}