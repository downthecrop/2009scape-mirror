package core.game.node.entity.skill.crafting.armour;

import core.game.node.item.Item;

/**
 * A snake skin.
 * @author Vexia
 */
public enum SnakeSkin {
	SNAKESKIN_BOOT(new Item(6328), 45, 30, 6), SNAKESKIN_VAMBRACES(new Item(6330), 47, 35, 8), SNAKESKIN_BANDANA(new Item(6326), 48, 45, 5), SNAKESKIN_CHAPS(new Item(6324), 51, 50, 12), SNAKESKIN_BODY(new Item(6322), 53, 55, 15);

	/**
	 * The item product.
	 */
	private final Item product;

	/**
	 * The level.
	 */
	private final int level;

	/**
	 * The experience.
	 */
	private final double experience;

	/**
	 * The required amount of snakeskins.
	 */
	private final int requiredAmount;

	/**
	 * Constructs a new {@Code SnakeSkin} {@Code Object}
	 * @param product the product.
	 * @param level the level.
	 * @param experience the exp.
	 * @param requiredAmount the required amount.
	 */
	private SnakeSkin(Item product, int level, double experience, int requiredAmount) {
		this.product = product;
		this.level = level;
		this.experience = experience;
		this.requiredAmount = requiredAmount;
	}

	/**
	 * Gets the product.
	 * @return the product
	 */
	public Item getProduct() {
		return product;
	}

	/**
	 * Gets the level.
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Gets the experience.
	 * @return the experience
	 */
	public double getExperience() {
		return experience;
	}

	/**
	 * Gets the requiredAmount.
	 * @return the requiredAmount
	 */
	public int getRequiredAmount() {
		return requiredAmount;
	}

}
