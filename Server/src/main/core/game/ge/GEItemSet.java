package core.game.ge;

import core.game.node.item.Item;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds the Grand Exchange item sets.
 * @author Emperor
 */
public enum GEItemSet {

	BRONZE_L(11814, 1155, 1117, 1075, 1189), BRONZE_SK(11816, 1155, 1117, 1087, 1189), IRON_L(11818, 1153, 1115, 1067, 1191), IRON_SK(11820, 1153, 1115, 1081, 1191), STEEL_L(11822, 1157, 1119, 1069, 1193), STEEL_SK(11824, 1157, 1119, 1083, 1193), BLACK_L(11826, 1165, 1125, 1077, 1195), BLACK_SK(11828, 1165, 1125, 1089, 1195), MITHRIL_L(11830, 1159, 1121, 1071, 1197), MITHRIL_SK(11832, 1159, 1121, 1085, 1197), ADAMANT_L(11834, 1161, 1123, 1073, 1199), ADAMANT_SK(11836, 1161, 1123, 1091, 1199), RUNE_L(11838, 1163, 1127, 1079, 1201), RUNE_SK(11840, 1163, 1127, 1093, 1201), DRAGON_L(11842, 1149, 3140, 4087), DRAGON_SK(11844, 1149, 3140, 4585),
	// NULL_1(-1), //This would create the spaces between the sets (uncomment
	// when needed)
	AHRIMS(11846, 4708, 4712, 4714, 4710), DHAROKS(11848, 4716, 4720, 4722, 4718), GUTHANS(11850, 4724, 4728, 4730, 4726), KARILS(11852, 4732, 4736, 4738, 4734), TORAGS(11854, 4745, 4749, 4751, 4747), VERACS(11856, 4753, 4757, 4759, 4755), THIRD_AGE_MELEE(11858, 10350, 10348, 10346, 10352), THIRD_AGE_RANGE(11860, 10334, 10330, 10332, 10336), THIRD_AGE_MAGE(11862, 10342, 10338, 10340, 10344), GREEN_DHIDE(11864, 1135, 1099, 1065), BLUE_DHIDE(11866, 2499, 2493, 2487), RED_DHIDE(11868, 2501, 2495, 2489), BLACK_DHIDE(11870, 2503, 2497, 2491), MYSTIC(11872, 4089, 4091, 4093, 4095, 4097), LIGHT_MYSTIC(11960, 4109, 4111, 4113, 4115, 4117), DARK_MYSTIC(11962, 4099, 4101, 4103, 4105, 4107), INFINITY(11874, 6918, 6916, 6924, 6922, 6920), SPLITBARK(11876, 3385, 3387, 3389, 3391, 3393), BLACK_TRIMMED_L(11878, 2587, 2583, 2585, 2589), BLACK_TRIMMED_SK(11880, 2587, 2583, 3472, 2589), BLACK_GOLD_TRIMMED_L(11882, 2595, 2591, 2593, 2597), BLACK_GOLD_TRIMMED_SK(11884, 2595, 2591, 3473, 2597), ADAMANT_TRIMMED_L(11886, 2605, 2599, 2601, 2603), ADAMANT_TRIMMED_SK(11888, 2605, 2599, 3474), ADAMANT_GOLD_TRIMMED_L(11890, 2613, 2607, 2609, 2611), ADAMANT_GOLD_TRIMMED_SK(11892, 2613, 3475, 2611), RUNE_TRIMMED_L(11894, 2627, 2623, 2625, 2629), RUNE_TRIMMED_SK(11896, 2627, 2623, 3477, 2629), RUNE_GOLD_TRIMMED_L(11898, 2619, 2615, 2617, 2621), RUNE_GOLD_TRIMMED_SK(11900, 2619, 2615, 3476, 2621), ENCHANTED(11902, 7400, 7399, 7398), TRIMMED_BLUE_WIZARD(11904, 7396, 7392, 7388), GOLD_TRIMMED_BLUE_WIZARD(11906, 7394, 7390, 7386), TRIMMED_LEATHER(11908, 7364, 7368), GOLD_TRIMMED_LEATHER(11910, 7362, 7366), GREEN_DHIDE_T(11912, 7372, 7380), GREEN_DHIDE_G(11914, 7370, 7378), BLUE_DHIDE_T(11916, 7376, 7384), BLUE_DHIDE_G(11918, 7374, 7382), GREEN_DHIDE_BLESSED(11920, 10382, 10378, 10380, 10376), BLUE_DHIDE_BLESSED(11922, 10390, 10386, 10388, 10384), RED_DHIDE_BLESSED(11924, 10374, 10370, 10372, 10368), GUTHIX_L(11926, 2673, 2669, 2671, 2675), SARADOMIN_L(11928, 2665, 2661, 2663, 2667), ZAMORAK_L(11930, 2657, 2653, 2655, 2659), GUTHIX_SK(11932, 2673, 2669, 3480, 2675), SARADOMIN_SK(11934, 2665, 2661, 3479, 2667), ZAMORAK_SK(11936, 2657, 2653, 3478, 2659), GILDED_L(11938, 3486, 3481, 3483, 3488), GILDED_SK(11940, 3486, 3481, 3485, 3488), ROCKSHELL(11942, 6128, 6129, 6130, 6151, 6145), SPINED(11944, 6131, 6133, 6135, 6149, 6143), SKELETAL(11946, 6137, 6139, 6141, 6153, 6147), CANNON(11967, 6, 8, 10, 12);

	/**
	 * The item sets mapping.
	 */
	private static final Map<Integer, GEItemSet> ITEM_SETS = new HashMap<>();

	/**
	 * The items array.
	 */
	private static Item[] itemArray;

	/**
	 * Populate the mapping.
	 */
	static {
		itemArray = new Item[values().length];
		for (int i = 0; i < itemArray.length; i++) {
			GEItemSet set = values()[i];
			itemArray[i] = set.itemId == -1 ? new Item() : new Item(set.itemId);
			ITEM_SETS.put(set.itemId, set);
		}
	}

	/**
	 * Gets the item set for the given id.
	 * @param setId The set item id.
	 * @return The item set object.
	 */
	public static GEItemSet forId(int setId) {
		return ITEM_SETS.get(setId);
	}

	/**
	 * The item id.
	 */
	private int itemId;

	/**
	 * The components.
	 */
	private int[] components;

	/**
	 * Constructs a new {@code GEItemSet} {@code Object}.
	 * @param itemId The item id.
	 * @param components The components.
	 */
	private GEItemSet(int itemId, int... components) {
		this.itemId = itemId;
		this.components = components;
	}

	/**
	 * Gets the itemId.
	 * @return The itemId.
	 */
	public int getItemId() {
		return itemId;
	}

	/**
	 * Gets the components.
	 * @return The components.
	 */
	public int[] getComponents() {
		return Arrays.copyOf(components, components.length);
	}

	/**
	 * Gets the item array.
	 * @return The item array.
	 */
	public static Item[] getItemArray() {
		return Arrays.copyOf(itemArray, itemArray.length);
	}
}