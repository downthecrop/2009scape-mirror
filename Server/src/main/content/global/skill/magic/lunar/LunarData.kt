package content.global.skill.magic.lunar

import core.api.addItemOrDrop
import core.api.freeSlots
import core.api.removeItem
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs09.consts.Items

private val HunterKitContents = intArrayOf(
    Items.NOOSE_WAND_10150,
    Items.BUTTERFLY_NET_10010,
    Items.BIRD_SNARE_10006,
    Items.RABBIT_SNARE_10031,
    Items.TEASING_STICK_10029,
    Items.UNLIT_TORCH_596,
    Items.BOX_TRAP_10008
)

class HunterKitInteraction : InteractionListener {
    override fun defineListeners() {
        on(Items.HUNTER_KIT_11159, IntType.ITEM, "open") { player, _ ->
            if(freeSlots(player) < 6) sendMessage(player, "You don't have enough inventory space.").also { return@on false }
            if(removeItem(player, Items.HUNTER_KIT_11159)) {
                for(item in HunterKitContents) {
                    addItemOrDrop(player, item)
                }
            }
            return@on true
        }
    }
}

enum class StringJewelleryItems(val unstrung: Int, val strung: Int) {
    GOLD(Items.GOLD_AMULET_1673, Items.GOLD_AMULET_1692),
    SAPPHIRE(Items.SAPPHIRE_AMULET_1675, Items.SAPPHIRE_AMULET_1694),
    EMERALD(Items.EMERALD_AMULET_1677, Items.EMERALD_AMULET_1696),
    RUBY(Items.RUBY_AMULET_1679, Items.RUBY_AMULET_1698),
    DIAMOND(Items.DIAMOND_AMULET_1681, Items.DIAMOND_AMULET_1700),
    DRAGONSTONE(Items.DRAGONSTONE_AMMY_1683, Items.DRAGONSTONE_AMMY_1702),
    ONYX(Items.ONYX_AMULET_6579, Items.ONYX_AMULET_6581),
    SALVE(Items.SALVE_SHARD_4082, Items.SALVE_AMULET_4081),
    HOLY(Items.UNSTRUNG_SYMBOL_1714, Items.UNBLESSED_SYMBOL_1716),
    UNHOLY(Items.UNSTRUNG_EMBLEM_1720, Items.UNPOWERED_SYMBOL_1722);
    companion object {
        private val productOfString = values().associate { it.unstrung to it.strung }
        fun forId(id: Int): Int {
            return productOfString[id]!!
        }

        fun unstrungContains(id: Int): Boolean {
            return productOfString.contains(id)
        }
    }
}

enum class HumidifyItems(val empty: Int, val full: Int) {
    VIAL(Items.VIAL_229, Items.VIAL_OF_WATER_227),
    WATERSKIN0(Items.WATERSKIN0_1831, Items.WATERSKIN4_1823),
    WATERSKIN1(Items.WATERSKIN1_1829, Items.WATERSKIN4_1823),
    WATERSKIN2(Items.WATERSKIN2_1827, Items.WATERSKIN4_1823),
    WATERSKIN3(Items.WATERSKIN3_1825, Items.WATERSKIN4_1823),
    BUCKET(Items.BUCKET_1925, Items.BUCKET_OF_WATER_1929),
    BOWL(Items.BOWL_1923, Items.BOWL_OF_WATER_1921),
    JUG(Items.JUG_1935, Items.JUG_OF_WATER_1937),
    WATERING_CAN0(Items.WATERING_CAN_5331, Items.WATERING_CAN8_5340),
    WATERING_CAN1(Items.WATERING_CAN1_5333, Items.WATERING_CAN8_5340),
    WATERING_CAN2(Items.WATERING_CAN2_5334, Items.WATERING_CAN8_5340),
    WATERING_CAN3(Items.WATERING_CAN3_5335, Items.WATERING_CAN8_5340),
    WATERING_CAN4(Items.WATERING_CAN4_5336, Items.WATERING_CAN8_5340),
    WATERING_CAN5(Items.WATERING_CAN5_5337, Items.WATERING_CAN8_5340),
    WATERING_CAN6(Items.WATERING_CAN6_5338, Items.WATERING_CAN8_5340),
    WATERING_CAN7(Items.WATERING_CAN7_5339, Items.WATERING_CAN8_5340),
    FISHBOWL(Items.FISHBOWL_6667, Items.FISHBOWL_6668),
    KETTLE(Items.KETTLE_7688, Items.FULL_KETTLE_7690),
    ENCHANTED_VIAL(Items.ENCHANTED_VIAL_731, Items.HOLY_WATER_732),
    CUP(Items.EMPTY_CUP_1980, Items.CUP_OF_WATER_4458);
    companion object {
        private val productOfFill = values().associate { it.empty to it.full }
        fun forId(id: Int): Int {
            return productOfFill[id]!!
        }

        fun emptyContains(id: Int): Boolean {
            return productOfFill.contains(id)
        }
    }
}

enum class PlankType (val log: Item, val plank: Item, val price: Int) {
    WOOD(Item(1511), Item(960), 70),
    OAK(Item(1521), Item(8778), 175),
    TEAK(Item(6333), Item(8780), 350),
    MAHOGANY(Item(6332), Item(8782), 1050);
    companion object {
        fun getForLog(item: Item): PlankType? {
            for (plankType in values()) {
                if (plankType.log.id == item.id) {
                    return plankType
                }
            }
            return null
        }
    }
}

val statSpySkills = arrayOf(
    intArrayOf(Skills.ATTACK, 1, 2),
    intArrayOf(Skills.HITPOINTS, 5, 6),
    intArrayOf(Skills.MINING, 9, 10),
    intArrayOf(Skills.STRENGTH, 13, 14),
    intArrayOf(Skills.AGILITY, 17, 18),
    intArrayOf(Skills.SMITHING, 21, 22),
    intArrayOf(Skills.DEFENCE, 25, 26),
    intArrayOf(Skills.HERBLORE, 29, 30),
    intArrayOf(Skills.FISHING, 33, 34),
    intArrayOf(Skills.RANGE, 37, 38),
    intArrayOf(Skills.THIEVING, 41, 42),
    intArrayOf(Skills.COOKING, 45, 46),
    intArrayOf(Skills.PRAYER, 49, 50),
    intArrayOf(Skills.CRAFTING, 53, 54),
    intArrayOf(Skills.FIREMAKING, 57, 58),
    intArrayOf(Skills.MAGIC, 61, 62),
    intArrayOf(Skills.FLETCHING, 65, 66),
    intArrayOf(Skills.WOODCUTTING, 69, 70),
    intArrayOf(Skills.RUNECRAFTING, 73, 74),
    intArrayOf(Skills.SLAYER, 77, 78),
    intArrayOf(Skills.FARMING, 81, 82),
    intArrayOf(Skills.CONSTRUCTION, 85, 86),
    intArrayOf(Skills.HUNTER, 89, 90),
    intArrayOf(Skills.SUMMONING, 93, 94)
)