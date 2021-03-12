package core.game.node.entity.skill.herblore;

import core.game.node.item.Item;

/**
 * Represents a generic potion which is used to transform incoming data to
 * represent a potion(finished or unfinished).
 * @author 'Vexia
 */
public final class GenericPotion {

	/**
	 * Represents the base of the potion.
	 */
	private final Item base;

	/**
	 * Represents the ingredient.
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
	 * Represents the product item.
	 */
	private final Item product;

	/**
	 * Constructs a new {@code GenericPotion} {@code Object}.
	 * @param base the base.
	 * @param ingredient the ingredient.
	 * @param level the level.
	 * @param experience the experience.
	 * @param product the product.
	 */
	public GenericPotion(final Item base, final Item ingredient, final int level, final double experience, final Item product) {
		this.base = base;
		this.ingredient = ingredient;
		this.level = level;
		this.experience = experience;
		this.product = product;
	}

	/**
	 * Method used to transform an unfinished potion into a generic potion.
	 * @param potion the potion to transform.
	 * @return the transformed potion.
	 */
	public static GenericPotion transform(final UnfinishedPotion potion) {
		return new GenericPotion(potion.getBase(), potion.getIngredient(), potion.getLevel(), 0, potion.getPotion());
	}

	/**
	 * Method used to transform a finished potion into a generic potion.
	 * @param potion the potion to transform.
	 * @return the transformed potion.
	 */
	public static GenericPotion transform(final FinishedPotion potion) {
		return new GenericPotion(potion.getUnfinished().getPotion(), potion.getIngredient(), potion.getLevel(), potion.getExperience(), potion.getPotion());
	}

	/**
	 * Gets the base.
	 * @return The base.
	 */
	public Item getBase() {
		return base;
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
	 * Gets the ingredient.
	 * @return The ingredient.
	 */
	public Item getIngredient() {
		return ingredient;
	}
}
