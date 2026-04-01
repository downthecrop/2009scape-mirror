package content.global.skill.cooking.fermenting

import org.rs09.consts.Items


enum class Brewable (val displayName: String, val itemID: Int, val level: Int, val product: IntArray, val levelXP: Double, val vatVarBitOffset: Int,
                     val barrelVarBitOffset: Int, val ingredientAmount: Int = 4){
    DWARVEN_STOUT("Dwarven Stout", Items.HAMMERSTONE_HOPS_5994, 19, intArrayOf(Items.DWARVEN_STOUT_1913, Items.DWARVEN_STOUT4_5777, Items.DWARVEN_STOUTM_5747, Items.DWARVEN_STOUTM4_5857), 215.0, 4, 8),
    ASGARNIAN_ALE("Asgarnian Ale", Items.ASGARNIAN_HOPS_5996, 26, intArrayOf(Items.ASGARNIAN_ALE_1905, Items.ASGARNIAN_ALE4_5785, Items.ASGARNIAN_ALEM_5739, Items.ASGARNIAN_ALEM4_5865), 248.0, 10, 16),
    GREENMANS_ALE("Greenman's Ale", Items.CLEAN_HARRALANDER_255, 29, intArrayOf(Items.GREENMANS_ALE_1909, Items.GREENMANS_ALE4_5793, Items.GREENMANS_ALEM_5743, Items.GREENMANS_ALEM4_5873), 281.0, 16, 24),
    WIZARD_BOMB("Wizard's Mind Bomb", Items.YANILLIAN_HOPS_5998, 34, intArrayOf(Items.WIZARDS_MIND_BOMB_1907, Items.MIND_BOMB4_5801, Items.MATURE_WMB_5741, Items.MIND_BOMBM4_5881), 341.0, 22, 32),
    DRAGON_BITTER("Dragon Bitter", Items.KRANDORIAN_HOPS_6000, 39, intArrayOf(Items.DRAGON_BITTER_1911, Items.DRAGON_BITTER4_5809, Items.DRAGON_BITTERM_5745, Items.DRAGON_BITTERM4_5889), 347.0, 28, 40),
    MOONLIGHT_MEAD("Moonlight Mead", Items.MUSHROOM_6004, 44, intArrayOf(Items.MOONLIGHT_MEAD_2955, Items.MOONLIGHT_MEAD4_5817, Items.MOONLIGHT_MEADM_5749, Items.MLIGHT_MEADM4_5897), 380.0, 34, 48),
    AXEMANS_FOLLY("Axeman's Folly", Items.OAK_ROOTS_6043, 49, intArrayOf(Items.AXEMANS_FOLLY_5751, Items.AXEMANS_FOLLY4_5825, Items.AXEMANS_FOLLYM_5753, Items.AXEMANS_FOLLYM4_5905), 413.0, 40, 56, 1),
    CHEFS_DELIGHT("Chef's Delight", Items.CHOCOLATE_DUST_1975, 54, intArrayOf(Items.CHEFS_DELIGHT_5755, Items.CHEFS_DELIGHT4_5833, Items.CHEFS_DELIGHTM_5757, Items.CHEFS_DELIGHTM4_5913), 446.0, 46, 64),
    SLAYERS_RESPITE("Slayer's Respite", Items.WILDBLOOD_HOPS_6002, 59, intArrayOf(Items.SLAYERS_RESPITE_5759, Items.SLAYERS_RESPITE4_5841, Items.SLAYERS_RESPITEM_5761, Items.SLAYER_RESPITEM4_5921), 479.0, 52, 72),
    CIDER("cider", Items.APPLE_MUSH_5992, 14, intArrayOf(Items.CIDER_5763, Items.CIDER4_5849, Items.MATURE_CIDER_5765, Items.CIDERM4_5929), 182.0, 58, 80),
    KELDA_STOUT("Kelda Stout", Items.KELDA_HOPS_6113, 22, intArrayOf(Items.KELDA_STOUT_6118), 0.0, 68, 3, 1);


    companion object {
        fun getIngredients() : IntArray{
            return values().map { it.itemID }.toIntArray()
        }

        fun getBrewable(itemId: Int) : Brewable?{
            return values().find { it.itemID == itemId }
        }

    }

}