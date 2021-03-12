package core.game.node.entity.skill.cooking.dairy;

import core.game.node.item.Item;

/**
 * Represents an enumeration of dairy products.
 * @author 'Vexia
 */
public enum DairyProduct {
	POT_OF_CREAM(21, 18, new Item(2130, 1)), PAT_OF_BUTTER(38, 40.5, new Item(6697, 1)), CHEESE(48, 64, new Item(1985, 1));

	/**
	 * The prodct <code>Item</code>.
	 */
	private Item product;

	/**
	 * The level required.
	 */
	private int level;

	/**
	 * /** The experience gained.
	 */
	private double experience;

	/**
	 * Constructs a new {@code DairyProduct.java} {@code Object}.
	 * @param level
	 * @param experience
	 * @param product
	 */
	DairyProduct(int level, double experience, Item product) {
		this.level = level;
		this.experience = experience;
		this.product = product;
	}

	/**
	 * @return the product.
	 */
	public Item getProduct() {
		return product;
	}

	/**
	 * @return the level.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return the experience.
	 */
	public double getExperience() {
		return experience;
	}
}
