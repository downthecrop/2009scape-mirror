package content.global.skill.herblore

import org.rs09.consts.Items

enum class BarbarianPotion(val item: Int, val level: Int, val product: Int, val xp: Double, val canUseRoe: Boolean) {
    ATTACK_POTION(Items.ATTACK_POTION2_123, 4, Items.ATTACK_MIX2_11429, 8.0, true),
    ANTIPOISON(Items.ANTIPOISON2_177, 6, Items.ANTIPOISON_MIX2_11433, 12.0, true),
    RELICYMS_BALM(Items.RELICYMS_BALM2_4846, 9, Items.RELICYMS_MIX2_11437, 14.0, true),
    STRENGTH_POTION(Items.STRENGTH_POTION2_117, 14, Items.STRENGTH_MIX2_11443, 17.0, true),
    RESTORE_POTION(Items.RESTORE_POTION2_129, 24, Items.RESTORE_MIX2_11449, 21.0, true),
    ENERGY_POTION(Items.ENERGY_POTION2_3012, 29, Items.ENERGY_MIX2_11453, 23.0, false),
    DEFENCE_POTION(Items.DEFENCE_POTION2_135, 33, Items.DEFENCE_MIX2_11457, 25.0, false),
    AGILITY_POTION(Items.AGILITY_POTION2_3036, 37, Items.AGILITY_MIX2_11461, 27.0, false),
    COMBAT_POTION(Items.COMBAT_POTION2_9743, 40, Items.COMBAT_MIX2_11445, 28.0, false),
    PRAYER_POTION(Items.PRAYER_POTION2_141, 42, Items.PRAYER_MIX2_11465, 29.0, false),
    SUPER_ATTACK(Items.SUPER_ATTACK2_147, 47, Items.SUPERATTACK_MIX2_11469, 33.0, false),
    SUPER_ANTIPOISON(Items.SUPER_ANTIPOISON2_183, 51, Items.ANTI_P_SUPERMIX2_11473, 35.0, false),
    FISHING_POTION(Items.FISHING_POTION2_153, 53, Items.FISHING_MIX2_11477, 38.0, false),
    SUPER_ENERGY(Items.SUPER_ENERGY2_3020, 56, Items.SUP_ENERGY_MIX2_11481, 42.0, false),
    HUNTER_POTION(Items.HUNTER_POTION2_10002, 58, Items.HUNTING_MIX2_11517, 40.0, false),
    SUPER_STRENGTH(Items.SUPER_STRENGTH2_159, 59, Items.SUP_STR_MIX2_11485, 42.0, false),
    MAGIC_ESSENCE_(Items.MAGIC_ESSENCE2_9023, 61, Items.MAGIC_ESS_MIX2_11489, 43.0, false),
    SUPER_RESTORE(Items.SUPER_RESTORE2_3028, 67, Items.SUP_RESTORE_MIX2_11493, 48.0, false),
    SUPER_DEFENCE(Items.SUPER_DEFENCE2_165, 71, Items.SUP_DEF_MIX2_11497, 50.0, false),
    ANTIPOISON_PLUS(Items.ANTIPOISON_PLUS2_5947, 74, Items.ANTIDOTE_PLUS_MIX2_11501, 52.0, false),
    ANTIFIRE_POTION(Items.ANTIFIRE_POTION2_2456, 75, Items.ANTIFIRE_MIX2_11505, 53.0, false),
    RANGING_POTION(Items.RANGING_POTION2_171, 80, Items.RANGING_MIX2_11509, 54.0, false),
    MAGIC_POTION(Items.MAGIC_POTION2_3044, 83, Items.MAGIC_MIX2_11513, 57.0, false),
    ZAMORAK_BREW(Items.ZAMORAK_BREW2_191, 85, Items.ZAMORAK_MIX2_11521, 58.0, false);

    companion object {
        /**
         * Gets the barbarian potion from the input potion id
         * @param id the id of the input potion.
         * @return the barbarian potion that takes the id as input.
         */
        @JvmStatic
        fun forId(id: Int): BarbarianPotion? {
            for (pot in BarbarianPotion.values()) {
                if (pot.item == id) {
                    return pot
                }
            }
            return null
        }
    }
}
