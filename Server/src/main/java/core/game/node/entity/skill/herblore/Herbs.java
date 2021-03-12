package core.game.node.entity.skill.herblore;

import core.game.node.item.Item;

/**
 * Represents the enumeration of herbs to clean.
 * @author 'Vexia
 */
public enum Herbs {
	GUAM(new Item(199), 2.5, 3, new Item(249)), MARRENTILL(new Item(201), 3.8, 5, new Item(251)), TARROMIN(new Item(203), 5, 11, new Item(253)), HARRALANDER(new Item(205), 6.3, 20, new Item(255)), RANARR(new Item(207), 7.5, 25, new Item(257)), TOADFLAX(new Item(3049), 8, 30, new Item(2998)), SPIRIT_WEED(new Item(12174), 7.8, 35, new Item(12172)), IRIT(new Item(209), 8.8, 40, new Item(259)), AVANTOE(new Item(211), 10, 48, new Item(261)), KWUARM(new Item(213), 11.3, 54, new Item(263)), SNAPDRAGON(new Item(3051), 11.8, 59, new Item(3000)), CADANTINE(new Item(215), 12.5, 65, new Item(265)), LANTADYME(new Item(2485), 13.1, 67, new Item(2481)), DWARF_WEED(new Item(217), 13.8, 70, new Item(267)), TORSTOL(new Item(219), 15, 75, new Item(269)), SNAKE_WEED(new Item(1525), 2.5, 3, new Item(1526)), ARDRIGAL(new Item(1527), 2.5, 3, new Item(1528)), SITO_FOIL(new Item(1529), 2.5, 3, new Item(1530)), VOLENCIA_MOSS(new Item(1531), 2.5, 3, new Item(1532)), ROGUES_PUSE(new Item(1533), 2.5, 3, new Item(1534));

	/**
	 * Represents the herb item.
	 */
	private final Item herb;

	/**
	 * Represents the level required to clean this herb.
	 */
	private final int level;

	/**
	 * Represents the experience.
	 */
	private final double experience;

	/**
	 * Represents the product recieved from cleaning the herb.
	 */
	private final Item product;

	/**
	 * Constructs a new {@code Herbs} {@code Object}.
	 * @param herb the herb.
	 * @param level the level.
	 * @param experience the experience.
	 * @param product the product.
	 */
	Herbs(final Item herb, final double experience, final int level, final Item product) {
		this.herb = herb;
		this.experience = experience;
		this.level = level;
		this.product = product;
	}

	/**
	 * Gets the herb.
	 * @return The herb.
	 */
	public Item getHerb() {
		return herb;
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
	 * Gets the product.
	 * @return The product.
	 */
	public Item getProduct() {
		return product;
	}

	/**
	 * Gets the herb from the item id.
	 * @param item the item.
	 * @return the herb.
	 */
	public static Herbs forItem(final Item item) {
		for (Herbs herb : Herbs.values()) {
			if (herb.getHerb().getId() == item.getId()) {
				return herb;
			}
		}
		return null;
	}
}
