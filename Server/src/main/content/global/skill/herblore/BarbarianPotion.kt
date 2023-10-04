package content.global.skill.herblore;

/**
 * Represents the barbarian potion.
 * @author 'Vexia
 * @author treevar
 */
public enum class BarbarianPotion {
	ATTACK_POTION(123, 4, 11429, 8.0, true), ANTI_POISION_POTION(177, 6, 11433, 12.0, true), RELIC(4846, 9, 11437, 14.0, true), STRENGTH_POTION(117, 14, 11443, 17.0, true), RESTORE_POTION(129, 24, 11449, 21.0, true), ENERGY_POTION(3012, 29, 11453, 23.0, false), DEFENCE_POTION(135, 33, 11457, 25.0, false), AGILITY_POTION(3036, 37, 11461, 27.0, false), COMBAT_POTION(9743, 40, 11445, 28.0, false), PRAYER_POTION(141, 42, 11465, 29.0, false), SUPER_ATTACK_POTION(147, 47, 11469, 33.0, false), SUPER_ANTIPOISION_POTION(183, 51, 11473, 35.0, false), FISHING_POTION(153, 53, 11477, 38.0, false), SUPER_ENERGY_POTION(3020, 56, 11481, 42.0, false), HUNTER_POTION(10002, 58, 11517, 40.0, false), SUPER_STRENGTH_POTION(159, 59, 11485, 42.0, false), SUPER_RESTORE(3028, 67, 11493, 48.0, false), SUPER_DEFENCE_POTION(165, 71, 11497, 50.0, false), ANTIDOTE_PLUS(5947, 74, 11501, 52.0, false), ANTIFIRE_POTION(2456, 75, 11505, 53.0, false), RANGING_POTION(171, 80, 11509, 54.0, false), MAGIC_POTION(3044, 83, 11513, 57.0, false), ZAMORAK_BREW(191, 85, 11521, 58.0, false);

	/**
	 * Constructs a new {@code BarbarianPotion} {@Code Object}.
	 * @param item the input potion id.
	 * @param level the level requirement to make.
	 * @param product the product potion id.
	 * @param exp the exp rewarded.
	 * @param both if both can be added(roe, cavier).
	 */
	constructor(item: Int, level: Int, product: Int, exp: Double, both: Boolean) {
		this.item = item;
		this.level = level;
		this.product = product;
		this.exp = exp;
		this.both = both;
	}

	/**
	 * The item id.
	 */
	private val item: Int;

	/**
	 * The product item id.
	 */
	private val product: Int;

	/**
	 * The level required.
	 */
	private val level: Int;

	/**
	 * the exp gained.
	 */
	private val exp: Double;

	/**
	 * Represents if both <b>Roe</b> & <b>Cavier</b> can be added.
	 */
	private val both: Boolean;

	/**
	 * Gets the item.
	 * @return The item.
	 */
	fun getItem(): Int {
		return item;
	}

	/**
	 * Gets the product.
	 * @return The product.
	 */
	fun getProduct(): Int {
		return product;
	}

	/**
	 * Gets the level.
	 * @return The level.
	 */
	fun getLevel(): Int {
		return level;
	}

	/**
	 * Gets the exp.
	 * @return The exp.
	 */
	fun getExp(): Double {
		return exp;
	}

	/**
	 * Gets whether roe and caviar can be used.
	 * @return true if both, false if only caviar.
	 */
	fun isBoth(): Boolean {
		return both;
	}

	companion object {
		/**
		 * Gets the barbarian potion from the input potion id
		 * @param id the id of the input potion.
		 * @return the barbarian potion that takes the id as input.
		 */
		@JvmStatic
		fun forId(id: Int): BarbarianPotion? {
			for (pot in BarbarianPotion.values()) {
				if (pot.getItem() == id) {
					return pot;
				}
			}
			return null;
		}
	}
}
