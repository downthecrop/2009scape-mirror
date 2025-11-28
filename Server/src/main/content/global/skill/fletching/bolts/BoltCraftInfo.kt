package content.global.skill.fletching.bolts

import org.rs09.consts.Items

/**
 * Provides information pertaining to crafting different bolts
 * @property unfinishedBoltItemId the unfinished bolt (unf) Item id used to craft the dart.
 * @property finishedItemId the resulting finished bolt Item id.
 * @property level the level required to craft.
 * @property experience the experience gained by crafting.
 */
enum class BoltCraftInfo(
    val unfinishedBoltItemId: Int,
    val finishedItemId: Int,
    val level: Int,
    val experience: Double
) {
    BRONZE_BOLT(Items.BRONZE_BOLTS_UNF_9375, Items.BRONZE_BOLTS_877, 9, 0.5),
    BLURITE_BOLT(Items.BLURITE_BOLTS_UNF_9376, Items.BLURITE_BOLTS_9139, 24, 1.0),
    IRON_BOLT(Items.IRON_BOLTS_UNF_9377, Items.IRON_BOLTS_9140, 39, 1.5),
    SILVER_BOLT(Items.SILVER_BOLTS_UNF_9382, Items.SILVER_BOLTS_9145, 43, 2.5),
    STEEL_BOLT(Items.STEEL_BOLTS_UNF_9378, Items.STEEL_BOLTS_9141, 46, 3.5),
    MITHRIL_BOLT(Items.MITHRIL_BOLTS_UNF_9379, Items.MITHRIL_BOLTS_9142, 54, 5.0),
    ADAMANTITE_BOLT(Items.ADAMANT_BOLTSUNF_9380, Items.ADAMANT_BOLTS_9143, 61, 7.0),
    RUNITE_BOLT(Items.RUNITE_BOLTS_UNF_9381, Items.RUNE_BOLTS_9144, 69, 10.0),
    BROAD_BOLT(Items.UNFINISHED_BROAD_BOLTS_13279, Items.BROAD_TIPPED_BOLTS_13280, 55, 3.0);

    companion object {
        private val boltCraftInfoByUnfinishedBoltIds = values().associateBy { it.unfinishedBoltItemId }

        val unfinishedBoltIds: IntArray = BoltCraftInfo.values()
            .map { unfinishedBoltToBoltMapping: BoltCraftInfo -> unfinishedBoltToBoltMapping.unfinishedBoltItemId }
            .toIntArray()

        fun fromUnfinishedBoltId(unfinishedBoltId: Int): BoltCraftInfo? {
            return boltCraftInfoByUnfinishedBoltIds[unfinishedBoltId]
        }
    }
}