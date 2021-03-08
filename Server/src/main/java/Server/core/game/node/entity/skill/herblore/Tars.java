package core.game.node.entity.skill.herblore;

import core.game.node.item.Item;

/**
 * Represents a tar to create.
 * @author 'Vexia
 */
public enum Tars {
	GUAM_TAR(Herbs.GUAM.getProduct(), 19, 30, new Item(10142)), MARRENTILL_TAR(Herbs.MARRENTILL.getProduct(), 31, 42.5, new Item(10143)), TARROMIN_TAR(Herbs.TARROMIN.getProduct(), 39, 55, new Item(10144)), HARRALANDER_TAR(Herbs.HARRALANDER.getProduct(), 44, 72.5, new Item(10145));

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
	 * Represents the tar item.
	 */
	private final Item tar;

	/**
	 * Constructs a new {@code Tars} {@code Object}.
	 * @param ingredient the ingredient.
	 * @param level the level.
	 * @param experience the experience.
	 * @param tar the tar.
	 */
	Tars(Item ingredient, int level, double experience, Item tar) {
		this.ingredient = ingredient;
		this.level = level;
		this.experience = experience;
		this.tar = tar;
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
	 * Gets the tar.
	 * @return The tar.
	 */
	public Item getTar() {
		return tar;
	}

	/**
	 * Gets the tar.
	 * @param item the item.
	 * @return the tar.
	 */
	public static Tars forItem(final Item item) {
		for (Tars tar : Tars.values()) {
			if (tar.getIngredient().getId() == item.getId()) {
				return tar;
			}
		}
		return null;
	}
}
