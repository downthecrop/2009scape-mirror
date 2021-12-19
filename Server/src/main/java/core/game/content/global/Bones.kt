package core.game.content.global

import core.game.content.global.Bones
import core.game.node.item.Item
import java.util.ArrayList
import java.util.HashMap

/**
 * Represents the type of bones.
 * @author Apache Ah64
 */
enum class Bones
/**
 * Construct a new `Bones` `Object`.
 * @param itemId The item id.
 * @param experience The experience given by burying the bone.
 */(
        /**
         * The bone item id.
         */
        val itemId: Int,
        /**
         * The experience given by burying the bone.
         */
        val experience: Double) {
    BONES(2530, 4.5),
    BONES_2(526, 4.5),
    WOLF_BONES(2859, 4.5),
    BURNST_BONES(528, 4.5),
    MONKEY_BONES(3183, 5.0),
    MONKEY_BONES2(3179, 5.0),
    BAT_BONES(530, 5.3),
    BIG_BONES(532, 15.0),
    JOGRE_BONES(3125, 15.0),
    ZOGRE_BONES(4812, 12.5),
    SHAIKAHAN_BONES(3123, 25.0),
    BABY_DRAGON_BONES(534, 30.0),
    WYVERN_BONES(6812, 50.0),
    DRAGON_BONES(536, 72.0),
    FAYRG(4830, 84.0),
    RAURG_BONES(4832, 96.0),
    DAGANNOTH(6729, 125.0),
    OURG_BONES(4834, 140.0),
    LAVA_DRAGON_BONES(14693, 85.0);
    /**
     * Get the bone experience given when you bury the bone.
     * @return The experience.
     */

    /**
     * Gets the bone meal item.
     * @return the item.
     */
    val boneMeal: Item
        get() {
            return when(this){
                FAYRG -> Item(4852)
                RAURG_BONES -> Item(4853)
                OURG_BONES -> Item(4854)
                else -> Item(4255 + ordinal)
            }
        }

    /**
     * Gets the config value for the bone type.
     * @param hopper the hopper.
     * @return the value.
     */
    fun getConfigValue(hopper: Boolean): Int {
        return (if (this == BONES_2) ordinal else ordinal - 1) or (if (hopper) 4 else 8) shl 16
    }

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
                if (bone.boneMeal.id == itemId) {
                    return bone
                }
            }
            return null
        }

        /**
         * Gets the config value for the bone.
         * @param value the value.
         * @param hopper hopper.
         * @return `True` if so.
         */
        @JvmStatic
        fun forConfigValue(value: Int, hopper: Boolean): Bones? {
            for (bone in values()) {
                if (bone.getConfigValue(hopper) == value) {
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