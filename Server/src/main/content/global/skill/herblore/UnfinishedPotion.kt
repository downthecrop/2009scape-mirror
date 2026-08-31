package content.global.skill.herblore

import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs09.consts.Items

/**
 * Represents an unfinished potion to make.
 * @author Vexia, oftheshire
 */
enum class UnfinishedPotion(
    val base: Item,
    val ingredient: Item,
    val level: Int,
    val potion: Item,
) {
    // regular potions
    GUAM(Item(Items.VIAL_OF_WATER_227), Herbs.GUAM.product, 3, Item(Items.GUAM_POTIONUNF_91)),
    MARRENTILL(Item(Items.VIAL_OF_WATER_227), Herbs.MARRENTILL.product, 5, Item(Items.MARRENTILL_POTIONUNF_93)),
    ROGUES_PURSE(Item(Items.VIAL_OF_WATER_227), Herbs.ROGUES_PURSE.product, 5, Item(Items.ROGUES_PURSE_POTIONUNF_4840)),
    TARROMIN(Item(Items.VIAL_OF_WATER_227), Herbs.TARROMIN.product, 12, Item(Items.TARROMIN_POTIONUNF_95)),
    HARRALANDER(Item(Items.VIAL_OF_WATER_227), Herbs.HARRALANDER.product, 22, Item(Items.HARRALANDER_POTIONUNF_97)),
    RANARR(Item(Items.VIAL_OF_WATER_227), Herbs.RANARR.product, 30, Item(Items.RANARR_POTIONUNF_99)),
    TOADFLAX(Item(Items.VIAL_OF_WATER_227), Herbs.TOADFLAX.product, 34, Item(Items.TOADFLAX_POTIONUNF_3002)),
    SPIRIT_WEED(Item(Items.VIAL_OF_WATER_227), Herbs.SPIRIT_WEED.product, 40, Item(Items.SPIRIT_WEED_POTIONUNF_12181)),
    IRIT(Item(Items.VIAL_OF_WATER_227), Herbs.IRIT.product, 45, Item(Items.IRIT_POTIONUNF_101)),
    AVANTOE(Item(Items.VIAL_OF_WATER_227), Herbs.AVANTOE.product, 50, Item(Items.AVANTOE_POTIONUNF_103)),
    KWUARM(Item(Items.VIAL_OF_WATER_227), Herbs.KWUARM.product, 55, Item(Items.KWUARM_POTIONUNF_105)),
    STARFLOWER(Item(Items.VIAL_OF_WATER_227), Item(Items.STAR_FLOWER_9017), 57, Item(Items.MAGIC_ESSENCEUNF_9019)),
    SNAPDRAGON(Item(Items.VIAL_OF_WATER_227), Herbs.SNAPDRAGON.product, 63, Item(Items.SNAPDRAGON_POTIONUNF_3004)),
    CADANTINE(Item(Items.VIAL_OF_WATER_227), Herbs.CADANTINE.product, 66, Item(Items.CADANTINE_POTIONUNF_107)),
    LANTADYME(Item(Items.VIAL_OF_WATER_227), Herbs.LANTADYME.product, 69, Item(Items.LANTADYME_POTIONUNF_2483)),
    DWARF_WEED(Item(Items.VIAL_OF_WATER_227), Herbs.DWARF_WEED.product, 72, Item(Items.DWARF_WEED_POTIONUNF_109)),
    TORSTOL(Item(Items.VIAL_OF_WATER_227), Herbs.TORSTOL.product, 75, Item(Items.TORSTOL_POTIONUNF_111)),
    STRONG_WEAPON_POISON(Item(Items.COCONUT_MILK_5935), Item(Items.CACTUS_SPINE_6016), 73, Item(Items.WEAPON_POISON_PLUSUNF_5936)),
    SUPER_STRONG_WEAPON_POISON(Item(Items.COCONUT_MILK_5935), Item(Items.CAVE_NIGHTSHADE_2398), 82, Item(Items.WEAPON_POISON_PLUS_PLUSUNF_5939)),
    STRONG_ANTIPOISON(Item(Items.COCONUT_MILK_5935), Herbs.TOADFLAX.product, 68, Item(Items.ANTIPOISON_PLUSUNF_5942)),
    SUPER_STRONG_ANTIPOISON(Item(Items.COCONUT_MILK_5935), Herbs.IRIT.product, 79, Item(Items.ANTIPOISON_PLUS_PLUSUNF_5951));

    companion object {
        /**
         * Gets the unf-potion.
         * @param item the item.
         * @param base the base item.
         * @return the unf-potion, or null if no match is found.
         */
        fun forItem(item: Item, base: Item): UnfinishedPotion? {
            for (potion in values()) {
                if ((potion.ingredient.id == item.id || potion.ingredient.id == base.id) 
                    && (item.id == potion.base.id || base.id == potion.base.id)
                ) {
                    return potion
                }
            }
            return null
        }
    }
}