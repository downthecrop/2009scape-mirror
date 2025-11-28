package content.global.skill.fletching.stringing

import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items

/**
 * Provides information pertaining to crafting different strung items
 * @property isCrossbow is this enum for a crossbow?
 * @property unstrungItemId the unstrung item Id
 * @property strungItemId the resulting strung item Id
 * @property level the level required to string the item
 * @property experience experience gained from stringing
 * @property animation the stringing animation to play
 */
enum class StringableCraftInfo(
    val isCrossbow: Boolean,
    val unstrungItemId: Int,
    val strungItemId: Int,
    val level: Int,
    val experience: Double,
    val animation: Animation
) {
    SHORT_BOW(false, Items.SHORTBOW_U_50, Items.SHORTBOW_841, 5, 5.0, Animation(6678)),
    LONG_BOW(false, Items.LONGBOW_U_48, Items.LONGBOW_839, 10, 10.0, Animation(6684)),
    OAK_SHORTBOW(false, Items.OAK_SHORTBOW_U_54, Items.OAK_SHORTBOW_843, 20, 16.5, Animation(6679)),
    OAK_LONGBOW(false, Items.OAK_LONGBOW_U_56, Items.OAK_LONGBOW_845, 25, 25.0, Animation(6685)),
    OGRE_COMP_BOW(false, Items.UNSTRUNG_COMP_BOW_4825, Items.COMP_OGRE_BOW_4827, 30, 45.0, Animation(-1)),
    WILLOW_SHORTBOW(false, Items.WILLOW_SHORTBOW_U_60, Items.WILLOW_SHORTBOW_849, 35, 33.3, Animation(6680)),
    WILLOW_LONGBOW(false, Items.WILLOW_LONGBOW_U_58, Items.WILLOW_LONGBOW_847, 40, 41.5, Animation(6686)),
    MAPLE_SHORTBOW(false, Items.MAPLE_SHORTBOW_U_64, Items.MAPLE_SHORTBOW_853, 50, 50.0, Animation(6681)),
    MAPLE_LONGBOW(false, Items.MAPLE_LONGBOW_U_62, Items.MAPLE_LONGBOW_851, 55, 58.3, Animation(6687)),
    YEW_SHORTBOW(false, Items.YEW_SHORTBOW_U_68, Items.YEW_SHORTBOW_857, 65, 67.5, Animation(6682)),
    YEW_LONGBOW(false, Items.YEW_LONGBOW_U_66, Items.YEW_LONGBOW_855, 70, 75.0, Animation(6688)),
    MAGIC_SHORTBOW(false, Items.MAGIC_SHORTBOW_U_72, Items.MAGIC_SHORTBOW_861, 80, 83.3, Animation(6683)),
    MAGIC_LONGBOW(false, Items.MAGIC_LONGBOW_U_70, Items.MAGIC_LONGBOW_859, 85, 91.5, Animation(6689)),

    BRONZE_CBOW(true, Items.BRONZE_CBOW_U_9454, Items.BRONZE_CROSSBOW_9174, 9, 6.0, Animation(6671)),
    BLURITE_CBOW(true, Items.BLURITE_CBOW_U_9456, Items.BLURITE_CROSSBOW_9176, 24, 16.0, Animation(6672)),
    IRON_CBOW(true, Items.IRON_CBOW_U_9457, Items.IRON_CROSSBOW_9177, 39, 22.0, Animation(6673)),
    STEEL_CBOW(true, Items.STEEL_CBOW_U_9459, Items.STEEL_CROSSBOW_9179, 46, 27.0, Animation(6674)),
    MITHIRIL_CBOW(true, Items.MITHRIL_CBOW_U_9461, Items.MITH_CROSSBOW_9181, 54, 32.0, Animation(6675)),
    ADAMANT_CBOW(true, Items.ADAMANT_CBOW_U_9463, Items.ADAMANT_CROSSBOW_9183, 61, 41.0, Animation(6676)),
    RUNITE_CBOW(true, Items.RUNITE_CBOW_U_9465, Items.RUNE_CROSSBOW_9185, 69, 50.0, Animation(6677));

    companion object {
        val unstrungBowIds: IntArray = values().map { stringableCraftInfo: StringableCraftInfo -> stringableCraftInfo.unstrungItemId }.toIntArray()
        val stringItemIds: IntArray = intArrayOf(Items.BOW_STRING_1777,Items.CROSSBOW_STRING_9438)

        private val stringableCraftInfoByUnstrungItemId = values().associateBy { it.unstrungItemId }

        fun forUnstrungBowItemId(unstrungItemId: Int): StringableCraftInfo? {
            return stringableCraftInfoByUnstrungItemId[unstrungItemId]
        }

        /**
         * Returns the item id that should be used for stringing this enums unstrung bow
         */
        val StringableCraftInfo.applicableStringId: Int
            get() {
                return when {
                    this.isCrossbow -> Items.CROSSBOW_STRING_9438
                    else -> Items.BOW_STRING_1777
                }
            }
    }
}