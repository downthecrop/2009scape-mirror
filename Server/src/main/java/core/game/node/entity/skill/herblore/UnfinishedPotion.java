package core.game.node.entity.skill.herblore;

import core.game.node.item.Item;

/**
 * Represents an unfinished potion to make.
 * @author 'Vexia
 */
public enum UnfinishedPotion {
	GUAM(Herbs.GUAM.getProduct(), 3, new Item(91)),
	MARRENTILL(Herbs.MARRENTILL.getProduct(), 5, new Item(93)),
	TARROMIN(Herbs.TARROMIN.getProduct(), 12, new Item(95)),
	HARRALANDER(Herbs.HARRALANDER.getProduct(), 22, new Item(97)),
	RANARR(Herbs.RANARR.getProduct(), 30, new Item(99)),
	TOADFLAX(Herbs.TOADFLAX.getProduct(), 34, new Item(3002)),
	SPIRIT_WEED(Herbs.SPIRIT_WEED.getProduct(), 40, new Item(12181)),
	IRIT(Herbs.IRIT.getProduct(), 45, new Item(101)),
	AVANTOE(Herbs.AVANTOE.getProduct(), 50, new Item(103)),
	KWUARM(Herbs.KWUARM.getProduct(), 55, new Item(105)),
	SNAPDRAGON(Herbs.SNAPDRAGON.getProduct(), 63, new Item(3004)),
	CADANTINE(Herbs.CADANTINE.getProduct(), 66, new Item(107)),
	LANTADYME(Herbs.LANTADYME.getProduct(), 69, new Item(2483)),
	DWARF_WEED(Herbs.DWARF_WEED.getProduct(), 72, new Item(109)),
	TORSTOL(Herbs.TORSTOL.getProduct(), 75, new Item(111)),
	STRONG_WEAPON_POISON(HerblorePulse.COCONUT_MILK, new Item(6016), 73, new Item(5936)),
	SUPER_STRONG_WEAPON_POISON(HerblorePulse.COCONUT_MILK, new Item(2398), 82, new Item(5939)),
	STRONG_ANTIPOISON(HerblorePulse.COCONUT_MILK, Herbs.TOADFLAX.getProduct(), 68, new Item(5942)),
	SUPER_STRONG_ANTIPOISON(HerblorePulse.COCONUT_MILK, Herbs.IRIT.getProduct(), 79, new Item(5951));

	/**
	 * Represents the base item.
	 */
	private final Item base;

	/**
	 * Represents the ingredient needed to make this unf-potion.
	 */
	private final Item ingredient;

	/**
	 * Represents the level required to make this unf-potion.
	 */
	private final int level;

	/**
	 * Represents the potion product.
	 */
	private final Item potion;

	/**
	 * Constructs a new {@code UnfinishedPotion} {@code Object}.
	 * @param ingredient the ingredient.
	 * @param level the level.
	 * @param potion the potion.
	 */
	UnfinishedPotion(final Item base, final Item ingredient, final int level, final Item potion) {
		this.base = base;
		this.ingredient = ingredient;
		this.level = level;
		this.potion = potion;
	}

	/**
	 * Constructs a new {@code UnfinishedPotion} {@code Object}.
	 * @param ingredient the ingredient.
	 * @param level the level.
	 * @param potion the potion.
	 */
	UnfinishedPotion(final Item ingredient, final int level, final Item potion) {
		this(HerblorePulse.VIAL_OF_WATER, ingredient, level, potion);
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
	 * Gets the potion.
	 * @return The potion.
	 */
	public Item getPotion() {
		return potion;
	}

	/**
	 * Gets the base.
	 * @return The base.
	 */
	public Item getBase() {
		return base;
	}

	/**
	 * Gets the unf-potion.
	 * @param item the item.
	 * @paramt the base item.
	 * @return the unf-potion.
	 */
	public static UnfinishedPotion forItem(final Item item, final Item base) {
		for (UnfinishedPotion potion : values()) {
			if ((potion.getIngredient().getId() == item.getId() || potion.getIngredient().getId() == base.getId()) && (item.getId() == potion.getBase().getId() || base.getId() == potion.getBase().getId())) {
				return potion;
			}
		}
		return null;
	}

}
