package content.global.skill.prayer

import org.rs09.consts.Items

/**
 * Represents the type of bones.
 * @author Apache Ah64
 */
enum class Bones(
    /**
     * Construct a new `Bones` `Object`.
     * @param itemId The item id.
     * @param experience The experience given by burying the bone.
     */

    /**
     * The bone item id.
     */
    val itemId: Int,

    /**
     * The experience given by burying the bone.
     */
    val experience: Double,

    /**
     * The bones' bonemeal item id if applicable.
     */
    val bonemealId: Int?,
) {
    BONES(Items.BONES_526, 4.5, Items.BONEMEAL_4255),
    BONES_2(Items.BONES_2530, 4.5, Items.BONEMEAL_4255),
    BONES_3(Items.BONES_3187, 4.5, Items.BONEMEAL_4255),
    WOLF_BONES(Items.WOLF_BONES_2859, 4.5, Items.BONEMEAL_4257),
    BURNT_BONES(Items.BURNT_BONES_528, 4.5, Items.BONEMEAL_4258),
    SMALL_NINJA_MONKEY_BONES(Items.MONKEY_BONES_3179, 16.0, Items.BONEMEAL_4256),
    MEDIUM_NINJA_MONKEY_BONES(Items.MONKEY_BONES_3180, 18.0, Items.BONEMEAL_4270),
    GORILLA_BONES(Items.MONKEY_BONES_3181, 18.0, Items.BONEMEAL_4855),
    BEARDED_GORILLA_BONES(Items.MONKEY_BONES_3182, 18.0, Items.BONEMEAL_5615),
    KARAMJA_MONKEY_BONES(Items.MONKEY_BONES_3183, 5.0, Items.BONEMEAL_4260),
    SMALL_ZOMBIE_MONKEY_BONES(Items.MONKEY_BONES_3185, 5.0, Items.BONEMEAL_6728),
    LARGE_ZOMBIE_MONKEY_BONES(Items.MONKEY_BONES_3186, 5.0, Items.BONEMEAL_6810),
    BAT_BONES(Items.BAT_BONES_530, 5.3, Items.BONEMEAL_4261),
    BIG_BONES(Items.BIG_BONES_532, 15.0, Items.BONEMEAL_4262),
    JOGRE_BONES(Items.JOGRE_BONES_3125, 15.0, Items.BONEMEAL_4263),
    ZOGRE_BONES(Items.ZOGRE_BONES_4812, 22.5, Items.BONEMEAL_4264),
    SHAIKAHAN_BONES(Items.SHAIKAHAN_BONES_3123, 25.0, Items.BONEMEAL_4265),
    BABY_DRAGON_BONES(Items.BABYDRAGON_BONES_534, 30.0, Items.BONEMEAL_4266),
    WYVERN_BONES(Items.WYVERN_BONES_6812, 50.0, Items.BONEMEAL_4267), //The bonemeal id should be 6810
    DRAGON_BONES(Items.DRAGON_BONES_536, 72.0, Items.BONEMEAL_4268),
    FAYRG(Items.FAYRG_BONES_4830, 84.0, Items.BONEMEAL_4852),
    RAURG_BONES(Items.RAURG_BONES_4832, 96.0, Items.BONEMEAL_4853),
    DAGANNOTH(Items.DAGANNOTH_BONES_6729, 125.0, Items.BONEMEAL_4271), // The bonemeal id should be 6728
    OURG_BONES(Items.OURG_BONES_4834, 140.0, Items.BONEMEAL_4854),
    BURNT_JOGRE_BONES(Items.BURNT_JOGRE_BONES_3127, 16.0, Items.BONEMEAL_4259),
    BURNT_RAW_PASTY_JOGRE_BONES(Items.PASTY_JOGRE_BONES_3128, 17.0, null),
    BURNT_COOKED_PASTY_JOGRE_BONES(Items.PASTY_JOGRE_BONES_3129, 17.0, null),
    MARINATED_JOGRE_BONES(Items.MARINATED_J_BONES_3130, 18.0, null),
    RAW_PASTY_JOGRE_BONES(Items.PASTY_JOGRE_BONES_3131, 17.0, null),
    COOKED_PASTY_JOGRE_BONES(Items.PASTY_JOGRE_BONES_3132, 17.0, null),
    MARINATED_JOGRE_BONES_BAD(Items.MARINATED_J_BONES_3133, 18.0, null);

    companion object {
        /**
         * Holds all bones.
         */
        private val bones = HashMap<Int, Bones>()

        /**
         * Gets the bones for the bone meal.
         * @param itemId the item.
         * @return the bones.
         */
        @JvmStatic
        fun forBoneMeal(itemId: Int): Bones? {
            for (bone in values()) {
                if (bone.bonemealId == itemId) {
                    return bone
                }
            }
            return null
        }

        /**
         * Gets the bone ids.
         * @return the ids.
         */
        val array: IntArray
            @JvmStatic get() {
                val list: MutableList<Int> = ArrayList(20)
                for (i in bones.keys) {
                    list.add(i)
                }
                return list.toIntArray()
            }

        /**
         * Get the bone.
         * @param itemId The item id.
         * @return The bone.
         */
        @JvmStatic
        fun forId(itemId: Int): Bones? {
            return bones[itemId]
        }

        /**
         * Construct the bones.
         */
        init {
            for (bone in values()) {
                bones[bone.itemId] = bone
            }
        }
    }
}