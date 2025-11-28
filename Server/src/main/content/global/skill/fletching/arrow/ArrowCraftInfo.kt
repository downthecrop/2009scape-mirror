package content.global.skill.fletching.arrow

import org.rs09.consts.Items

enum class ArrowCraftInfo(val tipItemId: Int, val arrowItemId: Int, val level: Int, val experience: Double) {
    BRONZE_ARROW(Items.BRONZE_ARROWTIPS_39, Items.BRONZE_ARROW_882, 1, 1.3),
    IRON_ARROW(Items.IRON_ARROWTIPS_40, Items.IRON_ARROW_884, 15, 2.5),
    STEEL_ARROW(Items.STEEL_ARROWTIPS_41, Items.STEEL_ARROW_886, 30, 5.0),
    MITHRIL_ARROW(Items.MITHRIL_ARROWTIPS_42, Items.MITHRIL_ARROW_888, 45, 7.5),
    ADAMANT_ARROW(Items.ADAMANT_ARROWTIPS_43, Items.ADAMANT_ARROW_890, 60, 10.0),
    RUNE_ARROW(Items.RUNE_ARROWTIPS_44, Items.RUNE_ARROW_892, 75, 12.5),
    DRAGON_ARROW(Items.DRAGON_ARROWTIPS_11237,Items.DRAGON_ARROW_11212, 90, 15.0),
    BROAD_ARROW(Items.BROAD_ARROW_HEADS_13278, Items.BROAD_ARROW_4160, 52, 15.0);

    companion object {
        private val arrowCraftInfoByTipId = values().associateBy { it.tipItemId }

        val arrowTipIds: IntArray = values().map { arrowCraftInfo: ArrowCraftInfo -> arrowCraftInfo.tipItemId  }.toIntArray()

        fun fromTipId(tipId: Int) : ArrowCraftInfo? {
            return arrowCraftInfoByTipId[tipId]
        }
    }
}