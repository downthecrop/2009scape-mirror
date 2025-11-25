package content.data

import core.game.node.item.Item

/**
 * Represents the repair item type.
 * @author Vexia
 * @author Damighty - Kotlin conversion
 */
enum class RepairItem(
    val item: Item,
    val product: Item,
    val cost: Int
) {
    BRONZE_HATCHET(Item(494, 1), Item(1351, 1), 0),
    BRONZE_PICKAXE(Item(468, 1), Item(1265, 1), 0),
    IRON_HATCHET(Item(496, 1), Item(1349, 1), 0),
    IRON_PICKAXE(Item(470, 1), Item(1267, 1), 0),
    STEEL_HATCHET(Item(498, 1), Item(1353, 1), 0),
    STEEL_PICKAXE(Item(472, 1), Item(1269, 1), 14),
    BLACK_HATCHET(Item(500, 1), Item(1361, 1), 10),
    MITHRIL_HATCHET(Item(502, 1), Item(1355, 1), 18),
    MITHRIL_PICKAXE(Item(474, 1), Item(1273, 1), 43),
    ADAMANT_HATCHET(Item(504, 1), Item(1357, 1), 43),
    ADAMANT_PICKAXE(Item(476, 1), Item(1271, 1), 107),
    RUNE_HATCHET(Item(506, 1), Item(1359, 1), 427),
    RUNE_PICKAXE(Item(478, 1), Item(1275, 1), 1100),
    DRAGON_HATCHET(Item(6741, 1), Item(6739, 1), 1800);

    companion object {
        /**
         * List of all repairable item IDs.
         */
        @JvmStatic
        val repairableItemIds: List<Int> = values().map { it.item.id }

        /**
         * Gets the repair item by the broken items ID.
         */
        @JvmStatic
        fun forId(id: Int): RepairItem? = values().firstOrNull { it.item.id == id }
    }
}
