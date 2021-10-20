package core.game.content.ttrail;

import core.game.component.CloseEvent;
import core.game.component.Component;
import core.game.container.access.InterfaceContainer;
import core.game.node.entity.npc.drop.DropFrequency;
import core.game.node.entity.player.Player;
import core.game.node.item.ChanceItem;
import core.game.node.item.Item;
import core.game.node.item.WeightedChanceItem;
import org.rs09.consts.Items;
import rs09.game.world.GameWorld;
import core.tools.RandomFunction;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A clue scroll level.
 *
 * @author Vexia
 */
public enum ClueLevel {

	EASY(new Item(2714), 1 << 16 | 5,
			new ChanceItem(Items.BLUE_BERET_2633, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLACK_BERET_2635, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.WHITE_BERET_2637, 1, 1, DropFrequency.UNCOMMON),
			// black
			// t
			new ChanceItem(Items.BLACK_FULL_HELMT_2587, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLACK_PLATEBODY_T_2583, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLACK_PLATELEGS_T_2585, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLACK_PLATESKIRT_T_3472, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLACK_KITESHIELD_T_2589, 1, 1, DropFrequency.UNCOMMON),
			// black
			// g
			new ChanceItem(Items.BLACK_FULL_HELMG_2595, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLACK_PLATEBODY_G_2591, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLACK_PLATELEGS_G_2593, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLACK_PLATESKIRT_G_3473, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLACK_KITESHIELD_G_2597, 1, 1, DropFrequency.UNCOMMON),
			//black h helms
			new ChanceItem(Items.BLACK_HELM_H1_10306, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLACK_HELM_H2_10308, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLACK_HELM_H3_10310, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLACK_HELM_H4_10312, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLACK_HELM_H5_10314, 1, 1, DropFrequency.UNCOMMON),
			//black h shields
			new ChanceItem(Items.BLACK_SHIELDH1_7332, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLACK_SHIELDH2_7338, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLACK_SHIELDH3_7344, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLACK_SHIELDH4_7350, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLACK_SHIELDH5_7356, 1, 1, DropFrequency.UNCOMMON),
			// highway
			// man
			// mask
			new ChanceItem(Items.HIGHWAYMAN_MASK_2631, 1, 1, DropFrequency.UNCOMMON),
			// wizard t
			new ChanceItem(Items.WIZARD_ROBE_T_7392, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.WIZARD_HAT_T_7396, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLUE_SKIRT_T_7388, 1, 1, DropFrequency.UNCOMMON),
			// wizard
			// g
			new ChanceItem(Items.WIZARD_ROBE_G_7390, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLUE_SKIRT_G_7386, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.WIZARD_HAT_G_7394, 1, 1, DropFrequency.UNCOMMON),
			// studded
			// g
			new ChanceItem(Items.STUDDED_BODY_G_7362, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.STUDDED_CHAPS_G_7366, 1, 1, DropFrequency.UNCOMMON),
			// studded
			// t
			new ChanceItem(Items.STUDDED_BODY_T_7364, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.STUDDED_CHAPS_T_7368, 1, 1, DropFrequency.UNCOMMON),
			// red
			// ele
			// shirt
			new ChanceItem(Items.RED_ELE_SHIRT_10404, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.RED_ELE_LEGS_10406, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.RED_ELE_BLOUSE_10424, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.RED_ELE_SKIRT_10426, 1, 1, DropFrequency.UNCOMMON),
			// blue
			// ele
			// shirt
			new ChanceItem(Items.BLUE_ELE_SHIRT_10408, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLUE_ELE_LEGS_10410, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLUE_ELE_BLOUSE_10428, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLUE_ELE_SKIRT_10430, 1, 1, DropFrequency.UNCOMMON),
			// green
			// ele
			// shirt
			new ChanceItem(Items.GREEN_ELE_SHIRT_10412, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.GREEN_ELE_LEGS_10414, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.GREEN_ELE_BLOUSE_10432, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.GREEN_ELE_SKIRT_10434, 1, 1, DropFrequency.UNCOMMON),
			// bob
			// the
			// cat
			new ChanceItem(Items.BOB_SHIRT_10316, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.BOB_SHIRT_10318, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.BOB_SHIRT_10320, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.BOB_SHIRT_10322, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.BOB_SHIRT_10324, 1, 1, DropFrequency.RARE),
			// emote
			// enhancers
			new ChanceItem(Items.A_POWDERED_WIG_10392, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.FLARED_TROUSERS_10394, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.PANTALOONS_10396, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.SLEEPING_CAP_10398, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.AMULET_OF_MAGICT_10366, 1, 1, DropFrequency.UNCOMMON),
			// vestment
			// robes(sara,
			// guthix,
			// zammy)
			new ChanceItem(Items.SARADOMIN_ROBE_TOP_10458, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.SARADOMIN_ROBE_LEGS_10464, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.GUTHIX_ROBE_TOP_10462, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.GUTHIX_ROBE_LEGS_10466, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ZAMORAK_ROBE_TOP_10460, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ZAMORAK_ROBE_LEGS_10468, 1, 1, DropFrequency.UNCOMMON),
			// black
			// cane
			new ChanceItem(Items.BLACK_CANE_13095, 1, 1, DropFrequency.UNCOMMON),
			// spikey
			// helmet
			new ChanceItem(Items.SPIKED_HELMET_13105, 1, 1, DropFrequency.RARE),
			//comp bow
			new ChanceItem(Items.WILLOW_COMP_BOW_10280, 1, 1, DropFrequency.UNCOMMON),
			//other common crap
			new ChanceItem(Items.BLACK_PLATELEGS_1077, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.BLACK_PLATESKIRT_1089, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.BLACK_CHAINBODY_1107, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.BLACK_PLATEBODY_1125, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.BLACK_MED_HELM_1151, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.BLACK_FULL_HELM_1165, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.BLACK_SQ_SHIELD_1179, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.BLACK_KITESHIELD_1195, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.BLACK_DAGGER_1217, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.BLACK_SWORD_1283, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.BLACK_LONGSWORD_1297, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.BLACK_2H_SWORD_1313, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.BLACK_SCIMITAR_1327, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.BLACK_WARHAMMER_1341, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.BLACK_AXE_1361, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.BLACK_BATTLEAXE_1367, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.BLACK_MACE_1426, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.BLACK_CLAWS_3098, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.BLACK_NAILS_4821, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.OAK_PLANK_8779, 4, 38, DropFrequency.COMMON),
			new ChanceItem(Items.WILLOW_SHORTBOW_850, 1, 4, DropFrequency.COMMON),
			new ChanceItem(Items.TROUT_334, 4, 19, DropFrequency.COMMON),
			new ChanceItem(Items.COIF_1169, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.LEATHER_GLOVES_1059, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.LEATHER_BOOTS_1061, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.LEATHER_VAMBRACES_1063, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.LEATHER_CHAPS_1095, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.LEATHER_BODY_1129, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.LEATHER_COWL_1167, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.HARDLEATHER_BODY_1131, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.YEW_SHORTBOW_858, 1, 4, DropFrequency.COMMON),
			new ChanceItem(Items.SALMON_330, 3, 23, DropFrequency.COMMON),
			new ChanceItem(Items.EARTH_TALISMAN_1441, 1, 3, DropFrequency.COMMON),
			new ChanceItem(Items.FIRE_TALISMAN_1443, 1, 3, DropFrequency.COMMON),
			new ChanceItem(Items.STEEL_PICKAXE_1270, 1, 3, DropFrequency.COMMON),
			new ChanceItem(Items.STUDDED_CHAPS_1097, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.STUDDED_BODY_1133, 1, 1, DropFrequency.COMMON)),

	MEDIUM(new Item(2717), 1 << 16 | 6,
			// Trimmed
			// addy
			// shit
			new ChanceItem(Items.ADAM_FULL_HELMT_2605, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ADAM_PLATEBODY_T_2599, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ADAM_PLATELEGS_T_2601, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ADAM_KITESHIELD_T_2603, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ADAM_PLATESKIRT_T_3474, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ADAM_FULL_HELMG_2613, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ADAM_PLATEBODY_G_2607, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ADAM_PLATELEGS_G_2609, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ADAM_PLATESKIRT_G_3475, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ADAM_KITESHIELD_G_2611, 1, 1, DropFrequency.UNCOMMON),
			// Headbands
			new ChanceItem(Items.BLACK_HEADBAND_2647, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.RED_HEADBAND_2645, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BROWN_HEADBAND_2649, 1, 1, DropFrequency.UNCOMMON),
			// Boots
			new ChanceItem(Items.RANGER_BOOTS_2577, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.WIZARD_BOOTS_2579, 1, 1, DropFrequency.UNCOMMON),
			// Boaters
			new ChanceItem(Items.RED_BOATER_7319, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ORANGE_BOATER_7321, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.GREEN_BOATER_7323, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLUE_BOATER_7325, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLACK_BOATER_7327, 1, 1, DropFrequency.UNCOMMON),
			// Trimmed
			// Dragonhide
			// shit
			new ChanceItem(Items.DHIDE_BODY_T_7372, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.DHIDE_CHAPS_T_7380, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.DHIDE_BODYG_7370, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.DHIDE_CHAPS_G_7378, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.PITH_HELMET_13103, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ADAMANT_CANE_13097, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.YEW_COMP_BOW_10282, 1, 1, DropFrequency.UNCOMMON),
			// animal
			// masks
			new ChanceItem(Items.PENGUIN_MASK_13109, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.SHEEP_MASK_13107, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BAT_MASK_13111, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.CAT_MASK_13113, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.WOLF_MASK_13115, 1, 1, DropFrequency.UNCOMMON),
			// ammy
			// of
			// strength
			// (t)
			new ChanceItem(Items.STRENGTH_AMULETT_10364, 1, 1, DropFrequency.UNCOMMON),
			// white ele
			new ChanceItem(Items.WHITE_ELE_BLOUSE_10420, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.WHITE_ELE_SKIRT_10422, 1, 1, DropFrequency.UNCOMMON),
			// black
			// ele
			new ChanceItem(Items.BLACK_ELE_SHIRT_10400, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.BLACK_ELE_LEGS_10402, 1, 1, DropFrequency.RARE),
			// purple
			// ele
			new ChanceItem(Items.PURPLE_ELE_SHIRT_10416, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.PURPLE_ELE_LEGS_10418, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.PURPLE_ELE_BLOUSE_10436, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.PURPLE_ELE_SKIRT_10438, 1, 1, DropFrequency.UNCOMMON),
			// addy
			// heraldic
			// helms
			new ChanceItem(Items.ADAMANT_HELM_H1_10296, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ADAMANT_HELM_H2_10298, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ADAMANT_HELM_H3_10300, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ADAMANT_HELM_H4_10302, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ADAMANT_HELM_H5_10304, 1, 1, DropFrequency.UNCOMMON),
			// addy
			// heraldic
			// shield
			new ChanceItem(Items.ADAMANT_SHIELDH1_10666, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ADAMANT_SHIELDH2_10669, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ADAMANT_SHIELDH3_10672, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ADAMANT_SHIELDH4_10675, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ADAMANT_SHIELDH5_10678, 1, 1, DropFrequency.UNCOMMON),
			// cloaks
			new ChanceItem(Items.SARADOMIN_CLOAK_10446, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.GUTHIX_CLOAK_10448, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ZAMORAK_CLOAK_10450, 1, 1, DropFrequency.UNCOMMON),
			//mitres
			new ChanceItem(Items.SARADOMIN_MITRE_10452, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.GUTHIX_MITRE_10454, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ZAMORAK_MITRE_10456, 1, 1, DropFrequency.UNCOMMON),

			new ChanceItem(Items.ADAMANT_FULL_HELM_1161, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.ADAMANT_PLATEBODY_1123, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.ADAMANT_PLATELEGS_1073, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.ADAMANT_PLATESKIRT_1091, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.ADAMANT_KITESHIELD_1199, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.ADAMANT_SQ_SHIELD_1183, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.ADAMANT_CHAINBODY_1111, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.ADAMANT_DAGGER_1211, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.ADAMANT_PICKAXE_1271, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.ADAMANT_SWORD_1287, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.ADAMANT_LONGSWORD_1301, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.ADAMANT_2H_SWORD_1317, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.ADAMANT_AXE_1357, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.ADAMANT_BATTLEAXE_1371, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.ADAMANT_MACE_1430, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.ADAMANTITE_NAILS_4823, 10, 40, DropFrequency.COMMON),
			new ChanceItem(Items.ADAMANT_CROSSBOW_9183, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.FIRE_BATTLESTAFF_1393, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.GREEN_DHIDE_CHAPS_1099, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.GREEN_DHIDE_BODY_1135, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.YEW_SHORTBOW_857, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.TEAK_PLANK_8781, 10, 43, DropFrequency.COMMON),
			new ChanceItem(Items.SWORDFISH_374, 6, 23, DropFrequency.COMMON),
			new ChanceItem(Items.LOBSTER_380, 15, 43, DropFrequency.COMMON)),

	HARD(new Item(2720), 1 << 16 | 8,
			new ChanceItem(Items.MAGIC_COMP_BOW_10284, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.TOP_HAT_13101, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.RUNE_CANE_13099, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.SARADOMIN_STOLE_10470, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.GUTHIX_STOLE_10472, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ZAMORAK_STOLE_10474, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.SARADOMIN_CROZIER_10440, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.GUTHIX_CROZIER_10442, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ZAMORAK_CROZIER_10444, 1, 1, DropFrequency.UNCOMMON),
			// Trimmed
			// rune
			// shit
			new ChanceItem(Items.RUNE_FULL_HELM_T_2627, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.RUNE_PLATEBODY_T_2623, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.RUNE_PLATELEGS_T_2625, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.RUNE_PLATESKIRT_T_3477, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.RUNE_KITESHIELD_T_2629, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.RUNE_FULL_HELMG_2619, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.RUNE_PLATEBODY_G_2615, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.RUNE_PLATELEGS_G_2617, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.RUNE_PLATESKIRT_G_3476, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.RUNE_KITESHIELD_G_2621, 1, 1, DropFrequency.UNCOMMON),
			// God
			// armour
			// shit
			new ChanceItem(Items.GUTHIX_FULL_HELM_2673, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.GUTHIX_PLATEBODY_2669, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.GUTHIX_PLATELEGS_2671, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.GUTHIX_PLATESKIRT_3480, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.GUTHIX_KITESHIELD_2675, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.ZAMORAK_FULL_HELM_2657, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.ZAMORAK_PLATEBODY_2653, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.ZAMORAK_PLATELEGS_2655, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.ZAMORAK_PLATESKIRT_3478, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.ZAMORAK_KITESHIELD_2659, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.SARADOMIN_FULL_HELM_2665, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.SARADOMIN_PLATEBODY_2661, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.SARADOMIN_PLATELEGS_2663, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.SARADOMIN_PLATESKIRT_3479, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.SARADOMIN_KITESHIELD_2667, 1, 1, DropFrequency.RARE),
			// heraldic
			// shit
			new ChanceItem(Items.RUNE_HELM_H1_10286, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.RUNE_SHIELDH1_10667, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.RUNE_HELM_H2_10288, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.RUNE_SHIELDH2_10670, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.RUNE_HELM_H3_10290, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.RUNE_SHIELDH3_10673, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.RUNE_HELM_H4_10292, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.RUNE_SHIELDH4_10676, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.RUNE_HELM_H5_10294, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.RUNE_SHIELDH5_10679, 1, 1, DropFrequency.UNCOMMON),
			// Blue
			// dragonhide
			// trimmed
			new ChanceItem(Items.DHIDE_BODY_T_7376, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.DHIDE_CHAPS_T_7384, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.DHIDE_BODY_G_7374, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.DHIDE_CHAPS_G_7382, 1, 1, DropFrequency.COMMON),
			// God
			// dhide
			new ChanceItem(Items.ZAMORAK_COIF_10374, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.ZAMORAK_BRACERS_10368, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.ZAMORAK_DHIDE_10370, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.ZAMORAK_CHAPS_10372, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.GUTHIX_COIF_10382, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.GUTHIX_BRACERS_10376, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.GUTHIX_DRAGONHIDE_10378, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.GUTHIX_CHAPS_10380, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.SARADOMIN_COIF_10390, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.SARADOMIN_BRACERS_10384, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.SARADOMIN_DHIDE_10386, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.SARADOMIN_CHAPS_10388, 1, 1, DropFrequency.RARE),
			// Hats
			new ChanceItem(Items.PIRATES_HAT_2651, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ROBIN_HOOD_HAT_2581, 1, 1, DropFrequency.UNCOMMON),
			// Cavaliers
			new ChanceItem(Items.TAN_CAVALIER_2639, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.DARK_CAVALIER_2641, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.BLACK_CAVALIER_2643, 1, 1, DropFrequency.COMMON),
			//rune crap
			new ChanceItem(Items.RUNE_FULL_HELM_1163, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.RUNE_PLATEBODY_1127, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.RUNE_PLATELEGS_1079, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.RUNE_PLATESKIRT_1093, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.RUNE_KITESHIELD_1201, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.RUNE_SQ_SHIELD_1185, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.RUNE_DAGGER_1213, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.RUNE_PICKAXE_1275, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.RUNE_SWORD_1289, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.RUNE_LONGSWORD_1303, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.RUNE_2H_SWORD_1319, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.RUNE_AXE_1359, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.RUNE_BATTLEAXE_1373, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.RUNE_MACE_1432, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.RUNE_NAILS_4824, 10, 43, DropFrequency.COMMON),
			new ChanceItem(Items.RUNE_BOLTS_9144, 6, 23, DropFrequency.COMMON),
			new ChanceItem(Items.RUNE_CROSSBOW_9185, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.BLACK_DHIDE_CHAPS_2497, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.BLACK_DHIDE_BODY_2503, 1, 1, DropFrequency.COMMON),
			new ChanceItem(Items.MAGIC_SHORTBOW_861, 1, 2, DropFrequency.COMMON),
			new ChanceItem(Items.MAGIC_LONGBOW_859, 1, 2, DropFrequency.COMMON),
			//planks
			new ChanceItem(Items.MAHOGANY_PLANK_8783, 6, 27, DropFrequency.COMMON),
			//food
			new ChanceItem(Items.LOBSTER_380, 16, 38, DropFrequency.COMMON),
			new ChanceItem(Items.SHARK_386, 7, 32, DropFrequency.COMMON),
			//enchanted robes
			new ChanceItem(Items.ENCHANTED_ROBE_7398, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ENCHANTED_TOP_7399, 1, 1, DropFrequency.UNCOMMON),
			new ChanceItem(Items.ENCHANTED_HAT_7400, 1, 1, DropFrequency.UNCOMMON)),

	;

	/**
	 * The default rewards.
	 */
	private static final ChanceItem[] DEFAULT_REWARDS = new ChanceItem[] {
			new ChanceItem(Items.COINS_995, 500, 7000, DropFrequency.COMMON),
			new ChanceItem(Items.PURPLE_FIRELIGHTER_10326, 15, 40, DropFrequency.UNCOMMON),
			new ChanceItem(Items.RED_FIRELIGHTER_7329, 15, 40, DropFrequency.UNCOMMON),
			new ChanceItem(Items.BLUE_FIRELIGHTER_7331, 15, 40, DropFrequency.UNCOMMON),
			new ChanceItem(Items.GREEN_FIRELIGHTER_7330, 15, 40, DropFrequency.UNCOMMON),
			new ChanceItem(Items.WHITE_FIRELIGHTER_10327, 15, 40, DropFrequency.UNCOMMON),
			new ChanceItem(Items.PURPLE_SWEETS_10476, 1, 27, DropFrequency.UNCOMMON),
			new ChanceItem(Items.AIR_RUNE_556, 1, 228, DropFrequency.COMMON),
			new ChanceItem(Items.FIRE_RUNE_554, 1, 228, DropFrequency.COMMON),
			new ChanceItem(Items.EARTH_RUNE_557, 1, 228, DropFrequency.COMMON),
			new ChanceItem(Items.WATER_RUNE_555, 1, 228, DropFrequency.COMMON),
			new ChanceItem(Items.SARADOMIN_PAGE_1_3827, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.SARADOMIN_PAGE_2_3828, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.SARADOMIN_PAGE_3_3829, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.SARADOMIN_PAGE_4_3830, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.ZAMORAK_PAGE_1_3831, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.ZAMORAK_PAGE_2_3832, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.ZAMORAK_PAGE_3_3833, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.ZAMORAK_PAGE_4_3834, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.GUTHIX_PAGE_1_3835, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.GUTHIX_PAGE_2_3836, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.GUTHIX_PAGE_3_3837, 1, 1, DropFrequency.RARE),
			new ChanceItem(Items.GUTHIX_PAGE_4_3838, 1, 1, DropFrequency.RARE)
	};

	/**
	 * The super rare items.
	 */
	private static final WeightedChanceItem[] SUPER_RARE = new WeightedChanceItem[] {
			//1 Trimmed Glory = 50 = to hit 15%
			new WeightedChanceItem(Items.AMULET_OF_GLORYT_10362, 1, 1, 50),
			//5 Gilded Items = 150 = to hit 9%
			new WeightedChanceItem(Items.GILDED_FULL_HELM_3486, 1, 1, 30),
			new WeightedChanceItem(Items.GILDED_PLATEBODY_3481, 1, 1, 30),
			new WeightedChanceItem(Items.GILDED_PLATELEGS_3483, 1, 1, 30),
			new WeightedChanceItem(Items.GILDED_PLATESKIRT_3485, 1, 1, 30),
			new WeightedChanceItem(Items.GILDED_KITESHIELD_3488, 1, 1, 30),
			//12 3rd Age = 120 = to hit 3%
			new WeightedChanceItem(Items.THIRD_AGE_RANGE_TOP_10330, 1, 1, 10),
			new WeightedChanceItem(Items.THIRD_AGE_RANGE_LEGS_10332, 1, 1, 10),
			new WeightedChanceItem(Items.THIRD_AGE_RANGE_COIF_10334, 1, 1, 10),
			new WeightedChanceItem(Items.THIRD_AGE_VAMBRACES_10336, 1, 1, 10),
			new WeightedChanceItem(Items.THIRD_AGE_ROBE_TOP_10338, 1, 1, 10),
			new WeightedChanceItem(Items.THIRD_AGE_ROBE_10340, 1, 1, 10),
			new WeightedChanceItem(Items.THIRD_AGE_MAGE_HAT_10342, 1, 1, 10),
			new WeightedChanceItem(Items.THIRD_AGE_AMULET_10344, 1, 1, 10),
			new WeightedChanceItem(Items.THIRD_AGE_PLATELEGS_10346, 1, 1, 10),
			new WeightedChanceItem(Items.THIRD_AGE_PLATEBODY_10348, 1, 1, 10),
			new WeightedChanceItem(Items.THIRD_AGE_FULL_HELMET_10350, 1, 1, 10),
			new WeightedChanceItem(Items.THIRD_AGE_KITESHIELD_10352, 1, 1, 10) };

	/**
	 * The casket.
	 */
	private final Item casket;

	/**
	 * The length hash of the level.
	 */
	private final int lengthHash;

	/**
	 * The reward items.
	 */
	private final ChanceItem[] rewards;

	/**
	 * Constructs a new {@Code ClueLevel} {@Code Object}
	 *
	 * @param casket
	 *            the casket.
	 * @param lengthHash
	 *            the hash.
	 * @param rewards
	 *            the rewards.
	 */
	private ClueLevel(Item casket, int lengthHash, ChanceItem... rewards) {
		this.casket = casket;
		this.lengthHash = lengthHash;
		this.rewards = rewards;
	}

	/**
	 * Opens a cakset for the player.
	 *
	 * @param player
	 *            the player.
	 * @param casket
	 *            the casket.
	 */
	public void open(Player player, Item casket) {
		if (casket != null) {
			if (!player.getInventory().containsItem(casket)) {
				return;
			}
		}
		if (player.getTreasureTrailManager().isCompleted() || GameWorld.getSettings().isDevMode()) {
			final List<Item> rewards = getLoot(player);
			player.getInterfaceManager().open(new Component(364).setCloseEvent(new CloseEvent() {
				private boolean given;

				@Override
				public boolean close(Player player, Component c) {
					if (!given) {
						for (Item item : rewards) {
							player.getInventory().add(item, player);
						}
						given = true;
					}
					return true;
				}
			}));
			if (casket != null) {
				player.getInventory().remove(casket);
			}
			player.getTreasureTrailManager().incrementClues(this);
			player.getTreasureTrailManager().clearTrail();
			player.sendMessage("Well done, you've completed the Treasure Trail!");
			player.sendMessage(getChatColor(this) + "You have completed " + player.getTreasureTrailManager().getCompletedClues(this) + " " + this.getName().toLowerCase() + " Treasure Trails.</col>");
			long value = 0;
			for (Item item : rewards) {
				value += item.getValue();
			}
			player.sendMessage("<col=990000>Your clue is worth approximately " + NumberFormat.getInstance().format(value) + " coins!</col>");
			player.getPacketDispatch().sendAccessMask(1278, 4, 364, 0, 6);
			InterfaceContainer.generateItems(player, rewards.toArray(new Item[] {}), new String[] { "" }, 364, 4, 3, 3);
			return;
		}
		Item clue = ClueScrollPlugin.getClue(this);
		if (clue == null) {
			player.sendMessage("Error! Clue not found! Report to admin.");
			return;
		}
		if (casket != null) {
			player.getInventory().replace(clue, casket.getSlot());
		} else {
			player.getInventory().add(clue);
		}
		player.getTreasureTrailManager().setClueId(clue.getId());
		player.getDialogueInterpreter().sendItemMessage(clue, "You've found another clue!");
	}

	/**
	 * Gets the rewards.ds.
	 *
	 * @return the rewar
	 */
	public List<Item> getLoot(Player player) {
		List<ChanceItem> items = new ArrayList<>(20);
		List<Integer> ids = new ArrayList<>(20);
		items.addAll(Arrays.asList(DEFAULT_REWARDS));
		items.addAll(Arrays.asList(this.getRewards()));
		List<Item> rewards = new ArrayList<>(20);
		int size = RandomFunction.random(1, 6);
		if (this == HARD) {
			size = RandomFunction.random(4, 6);
		}
		Item item = null;
		for (int i = 0; i < size; i++) {
			rewards.addAll(RandomFunction.rollChanceTable(false,items));
		}
		/*
		int rand = 1400;// 2000
		if (this == HARD && rand == 11)
		 */
		//Super Rare Table
		if (this == HARD && RandomFunction.random(100) == 50) {
			rewards.remove(0);
			rewards.add(RandomFunction.rollWeightedChanceTable(SUPER_RARE).asItem());
		}
		return rewards;
	}

	/**
	 * Gets a reward item.
	 *
	 * @return the item.
	 */
	public Item getReward(List<ChanceItem> items) {
		Collections.shuffle(items);
		return RandomFunction.getChanceItem(items.toArray(new ChanceItem[] {})).getRandomItem();
	}

	/**
	 * Gets the clue level for the casket.
	 *
	 * @param node
	 *            the node.
	 * @return the level.
	 */
	public static ClueLevel forCasket(Item node) {
		for (ClueLevel level : values()) {
			if (node.getId() == level.getCasket().getId()) {
				return level;
			}
		}
		return null;
	}

	/**
	 * Gets the Chat color to send on completed clues.
	 * @param level The clue level.
	 * @return the chat color.
	 */
	public static String getChatColor(ClueLevel level) {
		if (level == ClueLevel.HARD) {
			return "<col=ff1a1a>";
		}
		if (level == ClueLevel.MEDIUM) {
			return "<col=b38f00>";
		}
		return "<col=00e673>";
	}
	/**
	 * Gets the maximum length.
	 *
	 * @return the length.
	 */
	public int getMaximumLength() {
		return lengthHash & 0xFFFF;
	}

	/**
	 * Gets the minimum length.
	 *
	 * @return the length.
	 */
	public int getMinimumLength() {
		return lengthHash >> 16 & 0xFFFF;
	}

	/**
	 * Gets the brewards.
	 *
	 * @return the rewards
	 */
	public ChanceItem[] getRewards() {
		return rewards;
	}

	/**
	 * Gets the bcasket.
	 *
	 * @return the casket
	 */
	public Item getCasket() {
		return casket;
	}

	/**
	 * Gets the blengthHash.
	 *
	 * @return the lengthHash
	 */
	public int getLengthHash() {
		return lengthHash;
	}

	/**
	 * Gets the name of the clue level.
	 * @return the name.
	 */
	public String getName() {
		return toString();
	}
}
