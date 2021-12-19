package core.game.node.entity.skill.summoning;

/**
 * Represents a summoning scroll.
 * @author 'Vexia
 */
public enum SummoningScroll {
	HOWL_SCROLL(0, 12425, 0.1, 1, 12047), DREADFOWL_STRIKE_SCROLL(1, 12445, 0.1, 4, 12043), EGG_SPAWN_SCROLL(2, 12428, 0.2, 10, 12059), SLIME_SPRAY_SCROLL(3, 12459, 0.2, 13, 12019), STONY_SHELL_SCROLL(4, 12533, 0.2, 16, 12009), PESTER_SCROLL(5, 12838, 0.5, 17, 12778), ELECTRIC_LASH_SCROLL(6, 12460, 0.4, 18, 12049), VENOM_SHOT_SCROLL(7, 12432, 0.9, 19, 12055), FIREBALL_ASSAULT_SCROLL(8, 12839, 1.1, 22, 12808), CHEESE_FEAST_SCROLL(9, 12430, 2.3, 23, 12067), SANDSTORM_SCROLL(10, 12446, 2.5, 25, 12063), GENERATE_COMPOST_SCROLL(11, 12440, 0.6, 28, 12091), EXPLODE_SCROLL(12, 12834, 2.9, 29, 12800), VAMPYRE_TOUCH_SCROLL(13, 12447, 1.5, 31, 12053), INSANE_FEROCITY_SCROLL(14, 12433, 1.6, 32, 12065), MULTICHOP_SCROLL(15, 12429, 0.7, 33, 12021), CALL_TO_ARMS_SCROLL1(16, 12443, 0.7, 34, 12818), CALL_TO_ARMS_SCROLL2(17, 12443, 0.7, 34, 12814), CALL_TO_ARMS_SCROLL3(18, 12443, 0.7, 34, 12780), CALL_TO_ARMS_SCROLL4(19, 12443, 0.7, 34, 12798), BRONZE_BULL_RUSH_SCROLL(64, 12461, 3.6, 36, 12073), UNBURDEN_SCROLL(20, 12431, 0.6, 40, 12087), HERBCALL_SCROLL(21, 12422, 0.8, 41, 12071), EVIL_FLAMES_SCROLL(22, 12448, 2.1, 42, 12051), PETRIFYING_GAZE_SCROLL1(23, 12458, 0.9, 43, 12095), PETRIFYING_GAZE_SCROLL2(24, 12458, 0.9, 43, 12097), PETRIFYING_GAZE_SCROLL3(25, 12458, 0.9, 43, 12099), PETRIFYING_GAZE_SCROLL4(26, 12458, 0.9, 43, 12101), PETRIFYING_GAZE_SCROLL5(27, 12458, 0.9, 43, 12103), PETRIFYING_GAZE_SCROLL6(28, 12458, 0.9, 43, 12105), PETRIFYING_GAZE_SCROLL7(29, 12458, 0.9, 43, 12107), IRON_BULL_RUSH_SCROLL(65, 12462, 4.6, 46, 12075), IMMENSE_HEAT_SCROLL(30, 12829, 2.3, 46, 12816), THIEVING_FINGERS_SCROLL(31, 12426, 47, 167, 12041), BLOOD_DRAIN_SCROLL(32, 12444, 2.4, 49, 12061), TIRELESS_RUN_SCROLL(33, 12441, 0.8, 52, 12007), ABYSSAL_DRAIN_SCROLL(34, 12454, 1.1, 54, 12035), DISSOLVE_SCROLL(35, 12453, 5.5, 55, 12027), STEEL_BULL_RUSH_SCROLL(66, 12463, 5.6, 56, 12077), FISH_RAIN_SCROLL(36, 12424, 1.1, 56, 12531), AMBUSH_SCROLL(37, 12836, 5.7, 57, 12812), RENDING_SCROLL(38, 12840, 5.7, 57, 12784), GOAD_SCROLL(39, 12835, 5.7, 57, 12810), DOOMSPHERE_SCROLL(40, 12455, 5.8, 58, -1), DUST_CLOUD_SCROLL(41, 12468, 3.0, 61, 12085), ABYSSAL_STEALTH_SCROLL(42, 12427, 1.9, 62, 12037), OPHIDIAN_INCUBATION_SCROLL(43, 12436, 3.1, 63, 12015), POISONOUS_BLAST_SCROLL(44, 12467, 3.2, 64, 12045), MITHRIL_BULL_RUSH_SCROLL(67, 12464, 6.6, 66, 12079), TOAD_BARK_SCROLL(45, 12452, 1.0, 66, 12123), TESTUDO_SCROLL(46, 12439, 0.7, 67, 12031), SWALLOW_WHOLE_SCROLL(47, 12438, 1.4, 68, 12029), FRUITFALL_SCROLL(48, 12423, 1.4, 69, 12033), FAMINE_SCROLL(49, 12830, 1.5, 70, 12820), ARCTIC_BLAST_SCROLL(50, 12451, 1.1, 71, 12057),
	// RISE_FROM_THE_ASHES_SCROLL(51, 14622, 8.0, 277, -1),
	VOLCANIC_STRENGTH_SCROLL(51, 12826, 7.3, 73, 12792), CRUSHING_CLAW_SCROLL(52, 12449, 3.7, 74, 12069), MANTIS_STRIKE_SCROLL(53, 12450, 3.7, 75, 12011), INFERNO_SCROLL(54, 12841, 1.5, 76, 12782), ADAMANT_BULL_RUSH_SCROLL(68, 12465, 7.6, 76, 12081), DEADLY_CLAW_SCROLL(55, 12831, 11.0, 77, 12162), ACORN_MISSILE_SCROLL(56, 12457, 1.6, 78, 12013), TITANS_CONSTITUTION_SCROLL1(57, 12824, 7.9, 79, 12802), TITANS_CONSTITUTION_SCROLL2(58, 12824, 7.9, 79, 12806), TITANS_CONSTITUTION_SCROLL3(59, 12824, 7.9, 79, 12804), REGROWTH_SCROLL(60, 12442, 1.6, 80, 12025), SPIKE_SHOT_SCROLL(61, 12456, 4.1, 83, 12017), EBON_THUNDER_SCROLL(62, 12837, 8.3, 83, 12788), SWAMP_PLAGUE_SCROLL(63, 12832, 4.1, 85, 12776), RUNE_BULL_RUSH_SCROLL(69, 12466, 8.6, 86, 12083), HEALING_AURA_SCROLL(70, 12434, 1.8, 88, 12039), BOIL_SCROLL(71, 12833, 8.9, 89, 12786), MAGIC_FOCUS_SCROLL(72, 12437, 4.6, 92, 12089), ESSENCE_SHIPMENT_SCROLL(73, 12827, 1.9, 93, 12796), IRON_WITHIN_SCROLL(74, 12828, 4.7, 95, 12822), WINTER_STORAGE_SCROLL(75, 12435, 4.8, 96, 12093), STEEL_OF_LEGENDS_SCROLL(76, 12825, 4.9, 99, 12790),
    CLAY_DEPOSIT_SCROLL_1(-1, 14421, 0, 1, 14422),
    CLAY_DEPOSIT_SCROLL_2(-1, 14421, 0, 20, 14424),
    CLAY_DEPOSIT_SCROLL_3(-1, 14421, 0, 40, 14426),
    CLAY_DEPOSIT_SCROLL_4(-1, 14421, 0, 60, 14428),
    CLAY_DEPOSIT_SCROLL_5(-1, 14421, 0, 80, 14430);

	/**
	 * The level required.
	 */
	private int level;

	/**
	 * The item id needed.
	 */
	private int itemId;

	/**
	 * The slot id.
	 */
	private int slotId;

	/**
	 * The xp gained.
	 */
	private double xp;

	/**
	 * The items required.
	 */
	private int[] items;

	/**
	 * Constructs a new {@code SummoningScroll} {@code Object}.
	 * @param level
	 * @param itemId
	 * @param xp
	 * @param slotId
	 * @param items
	 */
	SummoningScroll(int slotId, int itemId, double xp, int level, int... items) {
		this.level = level;
		this.itemId = itemId;
		this.xp = xp;
		this.slotId = slotId;
		this.items = items;
	}

	public int getPouch(){
		return items[0];
	}

	/**
	 * Gets the level.
	 * @return The level.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Gets the itemId.
	 * @return The itemId.
	 */
	public int getItemId() {
		return itemId;
	}

	/**
	 * Gets the slotId.
	 * @return The slotId.
	 */
	public int getSlotId() {
		return slotId;
	}

	/**
	 * Gets the xp.
	 * @return The xp.
	 */
	public double getExperience() {
		return xp;
	}

	/**
	 * Gets the items.
	 * @return The items.
	 */
	public int[] getItems() {
		return items;
	}

	/**
	 * Gets the summoning value for the id.
	 * @param id
	 * @return
	 */
	public static SummoningScroll forId(int id) {
		for (SummoningScroll scroll : SummoningScroll.values()) {
			if (scroll.slotId == id) {
				return scroll;
			}
		}
		return null;
	}

	public static SummoningScroll forItemId(int id) {
		for (SummoningScroll scroll : SummoningScroll.values()) {
			if (scroll.itemId == id) {
				return scroll;
			}
		}
		return null;
	}

	/**
	 * Gets the scroll for the given pouch item id.
	 * @param pouchId The pouch item id.
	 * @return The scroll.
	 */
	public static SummoningScroll forPouch(int pouchId) {
		for (SummoningScroll scroll : SummoningScroll.values()) {
			if (scroll.items[0] == pouchId) {
				return scroll;
			}
		}
		return null;
	}

}
