package content.global.skill.cooking;

import org.rs09.consts.Items;
import core.game.node.item.Item;

import java.util.HashMap;

public enum CookableItems {
	// meats
	CHICKEN(2140, 2138, 2144, 1, 30, 128, 512, 128, 512),
	UGTHANKI(2140, 2138, 2144, 1, 40, 40, 252, 30, 253),
	RABBIT(3228, 3226, 7222, 1, 30, 128, 512, 128, 512),
	CRAB(7521, 7518, 7520, 21, 100, 57, 377, 57, 377),
	CHOMPY(2878, 2876, 2880, 30, 100, 200, 255, 200, 255),
	JUBBLY(7568, 7566, 7570, 41, 160, 195, 250, 195, 250),   // Can burn at 99 cooking. Ballpark low/high.
	
	// fish
	CRAYFISH(13433, 13435, 13437, 1, 30, 128, 512, 128, 512),
	SHRIMP(315, 317, 7954, 1, 30, 128, 512, 128, 512),
	KARAMBWANJI(3151, 3150, 592, 1, 10, 200, 400, 200, 400),
	SARDINE(325, 327, 369, 1, 40, 118, 492, 118, 492),
	ANCHOVIES(319, 321, 323, 1, 30, 128, 512, 128, 512),
	HERRING(347, 345, 357, 5, 50, 108, 472, 108, 472),
	MACKEREL(355, 353, 357, 10, 60, 98, 452, 98, 452),
	TROUT(333, 335, 343, 15, 70, 88, 432, 88, 432),
	COD(339, 341, 343, 18, 75, 83, 422, 88, 432),
	PIKE(351, 349, 343, 20, 80, 78, 412, 78, 412),
	SALMON(329, 331, 343, 25, 90, 68, 392, 68, 392),
	SLIMY_EEL(3381, 3379, 3383, 28, 95, 63, 382, 63, 382),
	TUNA(361, 359, 367, 30, 100, 58, 372, 58, 372),
	RAINBOW_FISH(10136, 10138, 10140, 35, 110, 56, 370, 56, 370),
	CAVE_EEL(5003, 5001, 5002, 38, 115, 38, 332, 38, 332),
	LOBSTER(379, 377, 381, 40, 120, 38, 332, 38, 332),
	BASS(365, 363, 367, 43, 130, 33, 312, 33, 312),
	SWORDFISH(373, 371, 375, 45, 140, 18, 292, 30, 310),
	LAVA_EEL(2149, 2148, 3383, 53, 30, 256, 256, 256, 256), // never burn
	MONKFISH(7946, 7944, 7948, 62, 150, 11, 275, 13, 280),
	SHARK(385, 383, 387, 80, 210, 1, 202, 1, 232),
	SEA_TURTLE(397, 395, 399, 82, 212, 1, 202, 1, 222),
	MANTA_RAY(391, 389, 393, 91, 216, 1, 202, 1, 222),
	KARAMBWAN(3144, 3142, 3146, 30, 190, 70, 255, 70, 255),
	
	// snails
	THIN_SNAIL(3369, 3363, 3375, 12, 70, 93, 444, 93, 444),
	LEAN_SNAIL(3371, 3365, 3375, 17, 80, 85, 428, 93, 444),
	FAT_SNAIL(3373, 3367, 3375, 22, 95, 73, 402, 73, 402),
	
	// bread
	BREAD(2309, 2307, 2311, 1, 40, 118, 492, 118, 492),
	PITTA_BREAD(1865, 1863, 1867, 58, 40,118, 492, 118, 492),
	
	// cake
	CAKE(1891, 1889, 1903, 40, 180, 0, 0, 38, 332),
	
	// beef(s) (rat, bear, cow, yak)
	BEEF(2142, 2132, 2146, 1, 30, 128, 512, 128, 512),
	RAT_MEAT(2142, 2134, 2146, 1, 30, 128, 512, 128, 512),
	BEAR_MEAT(2142, 2136, 2146, 1, 30, 128, 512, 128, 512),
	YAK_MEAT(2142, 10816, 2146, 1, 30, 128, 512, 128, 512),
	
	// skewered foods
	SKEWER_CHOMPY(2878, 7230, 2880, 30, 100, 200, 255, 200, 255),
	SKEWER_ROAST_RABBIT(7223, 7224, 7222, 16, 72, 160, 255, 160, 255),
	SKEWER_ROAST_BIRD(9980, 9984, 9982, 11, 62, 155, 255, 155, 255),
	SKEWER_ROAST_BEAST(9988, 9992, 9990, 21, 82.5, 180, 255, 180, 255),
	
	// pies
	REDBERRY_PIE(2325, 2321, 2329, 10, 78, 0, 0, 98, 452),
	MEAT_PIE(2327, 2319, 2329, 20, 110, 0, 0, 78, 412),
	MUD_PIE(7170, 7168, 2329, 29, 128, 0, 0, 58, 372),
	APPLE_PIE(2323, 2317, 2329, 30, 130, 0, 0, 58, 372),
	GARDEN_PIE(7178, 7176, 2329, 34, 138, 0, 0, 48, 352),
	FISH_PIE(7188, 7186, 2329, 47, 164, 0, 0, 38, 332),
	ADMIRAL_PIE(7198, 7196, 2329, 70, 210, 0, 0, 15, 270),
	WILD_PIE(7208, 7206, 2329, 85, 240, 0, 0, 1, 222),
	SUMMER_PIE(7218, 7216, 2329, 95, 260, 0, 0, 1, 212),
	
	// pizzas
	PIZZA_PLAIN(2289, 2287, 2305, 35, 143, 0, 0, 48, 352),
	
	// bowl foods
	BOWL_STEW(2003, 2001, 2005, 25, 117, 68, 392, 68, 392),
	BOWL_CURRY(2011, 2009, 2013, 60, 280, 38, 332, 38, 332),
	BOWL_NETTLE(4239, 4237, 4239, 20, 52, 78, 412, 78, 412),
	BOWL_EGG(7078, 7076, 7090, 13, 50, 0, 0, 90, 438),
	BOWL_ONION(7084, 1871, 7092, 43, 60, 36, 322, 36, 322),
	BOWL_MUSHROOM(7082, 7080, 7094, 46, 60, 16, 282, 16, 282),
	
	// vegetables
	BAKED_POTATO(6701, 1942, 6699, 7, 15, 0, 0, 108, 472),
	SWEETCORN(5988, 5986, 5990, 28, 104, 78, 412, 78, 412),
	
	// miscellaneous
	RAW_OOMLIE(Items.RAW_OOMLIE_2337, 0, Items.BURNT_OOMLIE_2426, 50, 0, 0, 0, 0, 0), // always burns
	OOMLIE_WRAP(Items.COOKED_OOMLIE_WRAP_2343, Items.WRAPPED_OOMLIE_2341, Items.BURNT_OOMLIE_WRAP_2345, 50, 30, 106, 450, 112, 476),
	SEAWEED(Items.SEAWEED_401, 0, Items.SODA_ASH_1781, 0, 0, 0, 0, 0, 0),
	
	/**
	 * Sinew gets overridden by BEEF in this enum, due to values being looked up by the items RAW id.
	 * This gets corrected in {@link SinewCookingPulse}
	 */
	SINEW(Items.SINEW_9436, Items.RAW_BEEF_2132, Items.SINEW_9436, 0, 3, 0, 0, 0, 0);
	
	public final static HashMap<Integer, CookableItems> cookingMap = new HashMap<>();
	public final static HashMap<Integer, CookableItems> intentionalBurnMap = new HashMap<>();
	public final static HashMap<Integer, int[]> gauntletValues = new HashMap<>();
	public final static HashMap<Integer, int[]> lumbridgeRangeValues = new HashMap<>();
	public final int cooked, raw, burnt, level, low, high, lowRange, highRange;
	public final double experience;
	
	CookableItems(int cooked, int raw, int burnt, int level, double experience, int low, int high, int lowRange, int highRange) {
		this.cooked = cooked;
		this.raw = raw;
		this.burnt = burnt;
		this.level = level;
		this.experience = experience;
		this.low = low;
		this.high = high;
		this.lowRange = lowRange;
		this.highRange = highRange;
	}
	
	static {
		for (CookableItems item : values()) {
			cookingMap.putIfAbsent(item.raw, item);
			intentionalBurnMap.putIfAbsent(item.cooked, item);
		}
		
		gauntletValues.put(Items.RAW_LOBSTER_377, new int[]{55, 368});
		gauntletValues.put(Items.RAW_SWORDFISH_371, new int[]{30, 310});
		gauntletValues.put(Items.RAW_MONKFISH_7944, new int[]{24, 290});
		gauntletValues.put(Items.RAW_SHARK_383, new int[]{15, 270});
		
		lumbridgeRangeValues.put(Items.BREAD_DOUGH_2307, new int[]{128, 512});
		lumbridgeRangeValues.put(Items.RAW_BEEF_2132, new int[]{138, 532});
		lumbridgeRangeValues.put(Items.RAW_RAT_MEAT_2134, new int[]{138, 532});
		lumbridgeRangeValues.put(Items.RAW_BEAR_MEAT_2136, new int[]{138, 532});
		lumbridgeRangeValues.put(Items.RAW_YAK_MEAT_10816, new int[]{138, 532});
		lumbridgeRangeValues.put(Items.RAW_CHICKEN_2138, new int[]{138, 532});
		lumbridgeRangeValues.put(Items.RAW_SHRIMPS_317, new int[]{138, 532});
		lumbridgeRangeValues.put(Items.RAW_ANCHOVIES_321, new int[]{138, 532});
		lumbridgeRangeValues.put(Items.RAW_SARDINE_327, new int[]{128, 512});
		lumbridgeRangeValues.put(Items.RAW_HERRING_345, new int[]{118, 492});
		lumbridgeRangeValues.put(Items.RAW_MACKEREL_353, new int[]{108, 472});
		lumbridgeRangeValues.put(Items.UNCOOKED_BERRY_PIE_2321, new int[]{108, 462});
		lumbridgeRangeValues.put(Items.THIN_SNAIL_3363, new int[]{103, 464});
		lumbridgeRangeValues.put(Items.RAW_TROUT_335, new int[]{98, 452});
		lumbridgeRangeValues.put(Items.LEAN_SNAIL_3365, new int[]{95, 448});
		lumbridgeRangeValues.put(Items.RAW_COD_341, new int[]{93, 442});
		lumbridgeRangeValues.put(Items.RAW_PIKE_349, new int[]{88, 432});
		lumbridgeRangeValues.put(Items.UNCOOKED_MEAT_PIE_2319, new int[]{88, 432});
		lumbridgeRangeValues.put(Items.FAT_SNAIL_3367, new int[]{83, 422});
		lumbridgeRangeValues.put(Items.UNCOOKED_STEW_2001, new int[]{78, 412});
		lumbridgeRangeValues.put(Items.RAW_SALMON_331, new int[]{78, 402});
	}
	
	public static CookableItems forId(int id) {
		return cookingMap.get(id);
	}
	
	public static Item getBurnt(int id) {
		return new Item(cookingMap.get(id).burnt);
	}
	
	public static boolean intentionalBurn(int id) {
		return (intentionalBurnMap.get(id) != null);
	}
	
	public static Item getIntentionalBurn(int id) {
		return new Item(intentionalBurnMap.get(id).burnt);
	}
}
