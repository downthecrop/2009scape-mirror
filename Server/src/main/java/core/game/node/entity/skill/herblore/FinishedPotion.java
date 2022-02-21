package core.game.node.entity.skill.herblore;

import core.game.node.item.Item;
import org.rs09.consts.Items;

/**
 * Represents a finished potion.
 * @author 'Vexia
 */
public enum FinishedPotion {
	ATTACK_POTION(UnfinishedPotion.GUAM, new Item(221), 3, 25, new Item(121)),
	ANTIPOISON_POTION(UnfinishedPotion.MARRENTILL, new Item(235), 5, 37.5, new Item(175)),
	STRENGTH_POTION(UnfinishedPotion.TARROMIN, new Item(225), 12, 50, new Item(115)),
	RESTORE_POTION(UnfinishedPotion.HARRALANDER, new Item(223), 22, 62.5, new Item(127)),
	ENERGY_POTION(UnfinishedPotion.HARRALANDER, new Item(1975), 26, 67.5, new Item(3010)),
	DEFENCE_POTION(UnfinishedPotion.RANARR, new Item(239), 30, 45, new Item(133)),
	AGILITY_POTION(UnfinishedPotion.TOADFLAX, new Item(2152), 34, 80, new Item(3034)),
	COMBAT_POTION(UnfinishedPotion.HARRALANDER, new Item(9736), 36, 84, new Item(9741)),
	PRAYER_POTION(UnfinishedPotion.RANARR, new Item(231), 38, 87.5, new Item(139)),
	SUMMONING_POTION(UnfinishedPotion.SPIRIT_WEED, new Item(12109), 40, 92, new Item(12142)),
	SUPER_ATTACK(UnfinishedPotion.IRIT, new Item(221), 45, 100, new Item(145)),
	SUPER_ANTIPOISON(UnfinishedPotion.IRIT, new Item(235), 48, 106.3, new Item(181)),
	FISHING_POTION(UnfinishedPotion.AVANTOE, new Item(231), 50, 112.5, new Item(151)),
	SUPER_ENERGY(UnfinishedPotion.AVANTOE, new Item(2970), 52, 117.5, new Item(3018)),
	HUNTING_POTION(UnfinishedPotion.AVANTOE, new Item(Items.KEBBIT_TEETH_DUST_10111), 53, 120, new Item(10000)),
	SUPER_STRENGTH(UnfinishedPotion.KWUARM, new Item(225), 55, 125, new Item(157)),
	WEAPON_POISON(UnfinishedPotion.KWUARM, new Item(241), 60, 137.5, new Item(187)),
	SUPER_RESTORE(UnfinishedPotion.SNAPDRAGON, new Item(223), 63, 142.5, new Item(3026)),
	SUPER_DEFENCE(UnfinishedPotion.CADANTINE, new Item(239), 66, 160, new Item(163)),
	ANTIFIRE(UnfinishedPotion.LANTADYME, new Item(241), 69, 157.5, new Item(2454)),
	SUPER_RANGING_POTION(UnfinishedPotion.DWARF_WEED, new Item(245), 72, 162.5, new Item(169)),
	SUPER_MAGIC(UnfinishedPotion.LANTADYME, new Item(3138), 76, 172.5, new Item(3042)),
	ZAMORAK_BREW(UnfinishedPotion.TORSTOL, new Item(247), 78, 175, new Item(189)),
	SARADOMIN_BREW(UnfinishedPotion.TOADFLAX, GrindingItem.BIRDS_NEST.getProduct(), 81, 180, new Item(6687)),
	STRONG_WEAPON_POISON(UnfinishedPotion.STRONG_WEAPON_POISON, new Item(223), 73, 165, new Item(5937)),
	SUPER_STRONG_WEAPON_POISON(UnfinishedPotion.SUPER_STRONG_WEAPON_POISON, new Item(6018), 82, 190, new Item(5940)),
	STRONG_ANTIPOISON(UnfinishedPotion.STRONG_ANTIPOISON, new Item(6049), 68, 155, new Item(5945)),
	SUPER_STRONG_ANTIPOISON(UnfinishedPotion.SUPER_STRONG_ANTIPOISON, new Item(6051), 79, 177.5, new Item(5954)),
	BLAMISH_OIL(UnfinishedPotion.HARRALANDER, new Item(1581), 25, 80, new Item(1582));

	/**
	 * Represents the unfinished potion.
	 */
	private final UnfinishedPotion unfinished;

	/**
	 * Represents the ingredient required.
	 */
	private final Item ingredient;

	/**
	 * Represents the level required.
	 */
	private final int level;

	/**
	 * Represents the experience gained.
	 */
	private final double experience;

	/**
	 * Represents the potion item.
	 */
	private final Item potion;

	/**
	 * Constructs a new {@code FinishedPotion} {@code Object}.
	 * @param unfinished the unfinished potion base.
	 * @param ingredient the ingredient.
	 * @param level the level.
	 * @param experience the experience.
	 * @param potion the potion.
	 */
	FinishedPotion(final UnfinishedPotion unfinished, final Item ingredient, final int level, final double experience, final Item potion) {
		this.unfinished = unfinished;
		this.ingredient = ingredient;
		this.level = level;
		this.experience = experience;
		this.potion = potion;
	}
	
	/**
	 * Gets the unfinished.
	 * @return The unfinished.
	 */
	public UnfinishedPotion getUnfinished() {
		return unfinished;
	}

	/**
	 * Gets the ingredient.
	 * @return The ingredient.
	 */
	public Item getIngredient() {
		return ingredient;
	}

	/**
	 * Gets the level.
	 * @return The level.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Gets the experience.
	 * @return The experience.
	 */
	public double getExperience() {
		return experience;
	}

	/**
	 * Gets the potion.
	 * @return The potion.
	 */
	public Item getPotion() {
		return potion;
	}

	/**
	 * Gets the finished potion by the unfinished potion and the ingredient.
	 * @param unf the unf-potion.
	 * @param ingredient the ingredient.
	 * @return the finished potion.
	 */
	public static FinishedPotion getPotion(final Item unf, final Item ingredient) {
		for (FinishedPotion pot : values()) {
			if (pot.getUnfinished().getPotion().getId() == unf.getId() && pot.getIngredient().getId() == ingredient.getId()) {
				return pot;
			}
		}
		return null;
	}
}
