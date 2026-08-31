package content.global.skill.herblore

import content.data.Quests
import core.api.getQuestStage
import core.api.hasRequirement
import core.api.isQuestComplete
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs09.consts.Items

/**
 * Represents a finished potion.
 * @author Vexia, oftheshire
 */
enum class FinishedPotion(
    val unfinished: Item,
    val ingredient: Item,
    val level: Int,
    val experience: Double,
    val potion: Item,
    private val requirement: (Player) -> Boolean = { true }
) {
    // regular potions
    ATTACK_POTION(UnfinishedPotion.GUAM.potion, Item(Items.EYE_OF_NEWT_221), 3, 25.0, Item(Items.ATTACK_POTION3_121)),
    ANTIPOISON_POTION(UnfinishedPotion.MARRENTILL.potion, Item(Items.UNICORN_HORN_DUST_235), 5, 37.5, Item(Items.ANTIPOISON3_175)),
    STRENGTH_POTION(UnfinishedPotion.TARROMIN.potion, Item(Items.LIMPWURT_ROOT_225), 12, 50.0, Item(Items.STRENGTH_POTION3_115)),
    RESTORE_POTION(UnfinishedPotion.HARRALANDER.potion, Item(Items.RED_SPIDERS_EGGS_223), 22, 62.5, Item(Items.RESTORE_POTION3_127)),
    ENERGY_POTION(UnfinishedPotion.HARRALANDER.potion, Item(Items.CHOCOLATE_DUST_1975), 26, 67.5, Item(Items.ENERGY_POTION3_3010)),
    DEFENCE_POTION(UnfinishedPotion.RANARR.potion, Item(Items.WHITE_BERRIES_239), 30, 45.0, Item(Items.DEFENCE_POTION3_133)),
    AGILITY_POTION(UnfinishedPotion.TOADFLAX.potion, Item(Items.TOADS_LEGS_2152), 34, 80.0, Item(Items.AGILITY_POTION3_3034)),
    COMBAT_POTION(UnfinishedPotion.HARRALANDER.potion, Item(Items.GOAT_HORN_DUST_9736), 36, 84.0, Item(Items.COMBAT_POTION3_9741)),
    PRAYER_POTION(UnfinishedPotion.RANARR.potion, Item(Items.SNAPE_GRASS_231), 38, 87.5, Item(Items.PRAYER_POTION3_139)),
    SUMMONING_POTION(UnfinishedPotion.SPIRIT_WEED.potion, Item(Items.COCKATRICE_EGG_12109), 40, 92.0, Item(Items.SUMMONING_POTION3_12142)),
    SUPER_ATTACK(UnfinishedPotion.IRIT.potion, Item(Items.EYE_OF_NEWT_221), 45, 100.0, Item(Items.SUPER_ATTACK3_145)),
    SUPER_ANTIPOISON(UnfinishedPotion.IRIT.potion, Item(Items.UNICORN_HORN_DUST_235), 48, 106.3, Item(Items.SUPER_ANTIPOISON3_181)),
    FISHING_POTION(UnfinishedPotion.AVANTOE.potion, Item(Items.SNAPE_GRASS_231), 50, 112.5, Item(Items.FISHING_POTION3_151)),
    SUPER_ENERGY(UnfinishedPotion.AVANTOE.potion, Item(Items.MORT_MYRE_FUNGUS_2970), 52, 117.5, Item(Items.SUPER_ENERGY3_3018)),
    HUNTING_POTION(UnfinishedPotion.AVANTOE.potion, Item(Items.KEBBIT_TEETH_DUST_10111), 53, 120.0, Item(Items.HUNTER_POTION3_10000)),
    SUPER_STRENGTH(UnfinishedPotion.KWUARM.potion, Item(Items.LIMPWURT_ROOT_225), 55, 125.0, Item(Items.SUPER_STRENGTH3_157)),
    WEAPON_POISON(UnfinishedPotion.KWUARM.potion, Item(Items.DRAGON_SCALE_DUST_241), 60, 137.5, Item(Items.WEAPON_POISON_187)),
    SUPER_RESTORE(UnfinishedPotion.SNAPDRAGON.potion, Item(Items.RED_SPIDERS_EGGS_223), 63, 142.5, Item(Items.SUPER_RESTORE3_3026)),
    SUPER_DEFENCE(UnfinishedPotion.CADANTINE.potion, Item(Items.WHITE_BERRIES_239), 66, 160.0, Item(Items.SUPER_DEFENCE3_163)),
    ANTIFIRE(UnfinishedPotion.LANTADYME.potion, Item(Items.DRAGON_SCALE_DUST_241), 69, 157.5, Item(Items.ANTIFIRE_POTION3_2454)),
    RANGING_POTION(UnfinishedPotion.DWARF_WEED.potion, Item(Items.WINE_OF_ZAMORAK_245), 72, 162.5, Item(Items.RANGING_POTION3_169)),
    MAGIC(UnfinishedPotion.LANTADYME.potion, Item(Items.POTATO_CACTUS_3138), 76, 172.5, Item(Items.MAGIC_POTION3_3042)),
    ZAMORAK_BREW(UnfinishedPotion.TORSTOL.potion, Item(Items.JANGERBERRIES_247), 78, 175.0, Item(Items.ZAMORAK_BREW3_189)),
    SARADOMIN_BREW(UnfinishedPotion.TOADFLAX.potion, GrindingItem.BIRDS_NEST.product, 81, 180.0, Item(Items.SARADOMIN_BREW3_6687)),
    STRONG_WEAPON_POISON(UnfinishedPotion.STRONG_WEAPON_POISON.potion, Item(Items.RED_SPIDERS_EGGS_223), 73, 165.0, Item(Items.WEAPON_POISON_PLUS_5937)),
    SUPER_STRONG_WEAPON_POISON(UnfinishedPotion.SUPER_STRONG_WEAPON_POISON.potion, Item(Items.POISON_IVY_BERRIES_6018), 82, 190.0, Item(Items.WEAPON_POISON_PLUS_PLUS_5940)),
    STRONG_ANTIPOISON(UnfinishedPotion.STRONG_ANTIPOISON.potion, Item(Items.YEW_ROOTS_6049), 68, 155.0, Item(Items.ANTIPOISON_PLUS3_5945)),
    SUPER_STRONG_ANTIPOISON(UnfinishedPotion.SUPER_STRONG_ANTIPOISON.potion, Item(Items.MAGIC_ROOTS_6051), 79, 177.5, Item(Items.ANTIPOISON_PLUS_PLUS3_5954)),

    // potions with quest requirements
    RELICYMS_BALM(UnfinishedPotion.ROGUES_PURSE.potion, Item(Items.CLEAN_SNAKE_WEED_1526), 8, 40.0, Item(Items.RELICYMS_BALM3_4844),
        { player -> getQuestStage(player, Quests.ZOGRE_FLESH_EATERS) >= 7 }
    ),
    SERUM_207(UnfinishedPotion.TARROMIN.potion, Item(Items.ASHES_592), 15, 50.0, Item(Items.SERUM_207_3_3410),
        //{ player -> getQuestStage(player, Quests.SHADES_OF_MORTTON) >= 5 }
        { player -> hasRequirement(player, Quests.SHADES_OF_MORTTON, false) } // todo replace this with real requirements (above) once quest is implemented.
    ),
    BLAMISH_OIL(UnfinishedPotion.HARRALANDER.potion, Item(Items.BLAMISH_SNAIL_SLIME_1581), 25, 80.0, Item(Items.BLAMISH_OIL_1582),
        { player -> getQuestStage(player, Quests.HEROES_QUEST) > 0 }
    ),
    SUPER_FISHING_EXPLOSIVE(UnfinishedPotion.GUAM.potion, Item(Items.RUBIUM_12630), 31, 55.0, Item(Items.SUPER_FISHING_EXPLOSIVE_12633),
        { player -> hasRequirement(player, Quests.KENNITHS_CONCERNS, false) } // todo replace this with real requirements once quest is implemented.
    ),
    SHRINK_ME_QUICK(UnfinishedPotion.TARROMIN.potion, Item(Items.SHRUNK_OGLEROOT_11205), 52, 6.0, Item(Items.SHRINK_ME_QUICK_11204),
        { player -> hasRequirement(player, Quests.GRIM_TALES, false) } // todo replace this with real requirements once quest is implemented.
    ),
    MAGIC_ESSENCE(UnfinishedPotion.STARFLOWER.potion, Item(Items.GORAK_CLAW_POWDER_9018), 57, 130.0, Item(Items.MAGIC_ESSENCE3_9022),
        { player -> hasRequirement(player, Quests.FAIRYTALE_II_CURE_A_QUEEN, false) } // todo replace this with real requirements once quest is implemented.
    ),

    // sanfew serum: first, mix a super restore with unicorn horn
    SANFEW_SERUM_MIX1_1(Item(Items.SUPER_RESTORE1_3030), Item(Items.UNICORN_HORN_DUST_235), 65, 47.5, Item(Items.MIXTURE___STEP_11_10915),
        { player -> isQuestComplete(player, Quests.ZOGRE_FLESH_EATERS) }
    ),
    SANFEW_SERUM_MIX1_2(Item(Items.SUPER_RESTORE2_3028), Item(Items.UNICORN_HORN_DUST_235), 65, 47.5, Item(Items.MIXTURE___STEP_12_10913),
        { player -> isQuestComplete(player, Quests.ZOGRE_FLESH_EATERS) }
    ),
    SANFEW_SERUM_MIX1_3(Item(Items.SUPER_RESTORE3_3026), Item(Items.UNICORN_HORN_DUST_235), 65, 47.5, Item(Items.MIXTURE___STEP_13_10911),
        { player -> isQuestComplete(player, Quests.ZOGRE_FLESH_EATERS) }
    ),
    SANFEW_SERUM_MIX1_4(Item(Items.SUPER_RESTORE4_3024), Item(Items.UNICORN_HORN_DUST_235), 65, 47.5, Item(Items.MIXTURE___STEP_14_10909),
        { player -> isQuestComplete(player, Quests.ZOGRE_FLESH_EATERS) }
    ),

    // sanfew serum: next, mix the mixture step 1 with snake weed
    SANFEW_SERUM_MIX2_1(SANFEW_SERUM_MIX1_1.potion, Herbs.SNAKE_WEED.product, 65, 52.5, Item(Items.MIXTURE___STEP_21_10923),
        { player -> isQuestComplete(player, Quests.ZOGRE_FLESH_EATERS) }
    ),
    SANFEW_SERUM_MIX2_2(SANFEW_SERUM_MIX1_2.potion, Herbs.SNAKE_WEED.product, 65, 52.5, Item(Items.MIXTURE___STEP_22_10921),
        { player -> isQuestComplete(player, Quests.ZOGRE_FLESH_EATERS) }
    ),
    SANFEW_SERUM_MIX2_3(SANFEW_SERUM_MIX1_3.potion, Herbs.SNAKE_WEED.product, 65, 52.5, Item(Items.MIXTURE___STEP_23_10919),
        { player -> isQuestComplete(player, Quests.ZOGRE_FLESH_EATERS) }
    ),
    SANFEW_SERUM_MIX2_4(SANFEW_SERUM_MIX1_4.potion, Herbs.SNAKE_WEED.product, 65, 52.5, Item(Items.MIXTURE___STEP_24_10917),
        { player -> isQuestComplete(player, Quests.ZOGRE_FLESH_EATERS) }
    ),

    // sanfew serum: finally, mix the mixture step 2 with nail beast nails
    SANFEW_SERUM1(SANFEW_SERUM_MIX2_1.potion, Item(Items.NAIL_BEAST_NAILS_10937), 65, 60.0, Item(Items.SANFEW_SERUM1_10931),
        { player -> isQuestComplete(player, Quests.ZOGRE_FLESH_EATERS) }
    ),
    SANFEW_SERUM2(SANFEW_SERUM_MIX2_2.potion, Item(Items.NAIL_BEAST_NAILS_10937), 65, 60.0, Item(Items.SANFEW_SERUM2_10929),
        { player -> isQuestComplete(player, Quests.ZOGRE_FLESH_EATERS) }
    ),
    SANFEW_SERUM3(SANFEW_SERUM_MIX2_3.potion, Item(Items.NAIL_BEAST_NAILS_10937), 65, 60.0, Item(Items.SANFEW_SERUM3_10927),
        { player -> isQuestComplete(player, Quests.ZOGRE_FLESH_EATERS) }
    ),
    SANFEW_SERUM4(SANFEW_SERUM_MIX2_4.potion, Item(Items.NAIL_BEAST_NAILS_10937), 65, 60.0, Item(Items.SANFEW_SERUM4_10925),
        { player -> isQuestComplete(player, Quests.ZOGRE_FLESH_EATERS) }
    ),

    // guthix balance: first, mix a restore with garlic
    GUTHIX_BALANCE_UNF_1(Item(Items.RESTORE_POTION1_131), Item(Items.GARLIC_1550), 22, 25.0, Item(Items.GUTHIX_BALANCEUNF_7658),
        { player -> hasRequirement(player, Quests.IN_AID_OF_THE_MYREQUE, false) } // todo replace this with real requirements once quest is implemented.
    ),
    GUTHIX_BALANCE_UNF_2(Item(Items.RESTORE_POTION2_129), Item(Items.GARLIC_1550), 22, 25.0, Item(Items.GUTHIX_BALANCEUNF_7656),
        { player -> hasRequirement(player, Quests.IN_AID_OF_THE_MYREQUE, false) } // todo replace this with real requirements once quest is implemented.
    ),
    GUTHIX_BALANCE_UNF_3(Item(Items.RESTORE_POTION3_127), Item(Items.GARLIC_1550), 22, 25.0, Item(Items.GUTHIX_BALANCEUNF_7654),
        { player -> hasRequirement(player, Quests.IN_AID_OF_THE_MYREQUE, false) } // todo replace this with real requirements once quest is implemented.
    ),
    GUTHIX_BALANCE_UNF_4(Item(Items.RESTORE_POTION4_2430), Item(Items.GARLIC_1550), 22, 25.0, Item(Items.GUTHIX_BALANCEUNF_7652),
        { player -> hasRequirement(player, Quests.IN_AID_OF_THE_MYREQUE, false) } // todo replace this with real requirements once quest is implemented.
    ),

    // guthix balance: then, mix the unfinished potion with silver dust
    GUTHIX_BALANCE_1(GUTHIX_BALANCE_UNF_1.potion, Item(Items.SILVER_DUST_7650), 22, 25.0, Item(Items.GUTHIX_BALANCE1_7666),
        { player -> hasRequirement(player, Quests.IN_AID_OF_THE_MYREQUE, false) } // todo replace this with real requirements once quest is implemented.
    ),
    GUTHIX_BALANCE_2(GUTHIX_BALANCE_UNF_2.potion, Item(Items.SILVER_DUST_7650), 22, 25.0, Item(Items.GUTHIX_BALANCE2_7664),
        { player -> hasRequirement(player, Quests.IN_AID_OF_THE_MYREQUE, false) } // todo replace this with real requirements once quest is implemented.
    ),
    GUTHIX_BALANCE_3(GUTHIX_BALANCE_UNF_3.potion, Item(Items.SILVER_DUST_7650), 22, 25.0, Item(Items.GUTHIX_BALANCE3_7662),
        { player -> hasRequirement(player, Quests.IN_AID_OF_THE_MYREQUE, false) } // todo replace this with real requirements once quest is implemented.
    ),
    GUTHIX_BALANCE_4(GUTHIX_BALANCE_UNF_4.potion, Item(Items.SILVER_DUST_7650), 22, 25.0, Item(Items.GUTHIX_BALANCE4_7660),
        { player -> hasRequirement(player, Quests.IN_AID_OF_THE_MYREQUE, false) } // todo replace this with real requirements once quest is implemented.
    );

    fun canMake(player: Player): Boolean = requirement(player)

    companion object {
        /**
         * Gets the finished potion by the unfinished potion and the ingredient.
         * @param unf the unf-potion.
         * @param ingredient the ingredient.
         * @return the finished potion, or null if no match is found.
         */
        @JvmStatic
        fun getPotion(unf: Item, ingredient: Item): FinishedPotion? {
            for (pot in values()) {
                if (pot.unfinished.id == unf.id
                    && pot.ingredient.id == ingredient.id
                ) {
                    return pot
                }
            }
            return null
        }
    }
}