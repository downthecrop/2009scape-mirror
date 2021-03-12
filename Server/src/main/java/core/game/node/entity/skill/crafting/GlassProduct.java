package core.game.node.entity.skill.crafting;

/**
 * Represents the glass product.
 * @author 'Vexia
 */
public enum GlassProduct {
	
	VIAL(38, 229, 1, 33, 35), ORB(39, 567, 1, 46, 52.5), BEER(40, 1919, 1, 1, 17.5), CANDLE(41, 4527, 1, 4, 19), OIL_LAMP(42, 4525, 1, 12, 25), FISHBOWL(44, 6667, 1, 42, 42.5), LANTERN_LENS(43, 4542, 1, 49, 55), LIGHT_ORB(45, 10973, 1, 87, 70);

	/**
	 * Represents the button pressed.
	 */
	private final int buttonId;

	/**
	 * Represents the product.
	 */
	private final int product;

	/**
	 * Represents the amount.
	 */
	private final int amount;

	/**
	 * Represents the level needed.
	 */
	private final int level;

	/**
	 * Represents the experience gained.
	 */
	private final double experience;

	/**
	 * Constructs a new {@code GlassDefinitions.java} {@Code Object}
	 * @param buttonId the button id.
	 * @param result the product.
	 * @param amount the amount.
	 * @param level the level.
	 * @param experience the exp.
	 */
	private GlassProduct(int buttonId, int result, int amount, int level, double experience) {
		this.buttonId = buttonId;
		this.product = result;
		this.amount = amount;
		this.level = level;
		this.experience = experience;
	}

	/**
	 * Gets the buttonId.
	 * @return The buttonId.
	 */
	public int getButtonId() {
		return buttonId;
	}

	/**
	 * Gets the product.
	 * @return The product.
	 */
	public int getProduct() {
		return product;
	}

	/**
	 * Gets the amount.
	 * @return The amount.
	 */
	public int getAmount() {
		return amount;
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
	 * Gets the glass product by the id.
	 * @param id the id.
	 * @return the product.
	 */
	public static GlassProduct forId(int id) {
		for (GlassProduct def : GlassProduct.values()) {
			if (def.getButtonId() == id) {
				return def;
			}
		}
		return null;
	}
}
