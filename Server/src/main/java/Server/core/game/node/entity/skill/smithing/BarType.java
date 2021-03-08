package core.game.node.entity.skill.smithing;

/**
 * @author Emperor
 */
public enum BarType {

	/**
	 * Bronze Interface
	 */
	BRONZE(2349, 12.5, "Bronze Smithing"),

	/**
	 * Blurite
	 */
	BLURITE(9467, 16, "Blurite Smithing"),

	/**
	 * Iron
	 */
	IRON(2351, 25, "Iron Smithing"),

	/**
	 * Steel
	 */
	STEEL(2353, 37.5, "Steel Smithing"),

	/**
	 * Mithril
	 */
	MITHRIL(2359, 50, "Mithril Smithing"),

	/**
	 * Adamant
	 */
	ADAMANT(2361, 62.5, "Adamant Smithing"),

	/**
	 * Runite
	 */
	RUNITE(2363, 75, "Runite Smithing");

	/**
	 * The bar type id.
	 */
	private int bar;

	/**
	 * The bar's name.
	 */
	private String string;

	/**
	 * The amount of experience gained
	 */
	private double experience;

	/**
	 * Constructs a new {@code BarType} {@code Object}.
	 * @param bar the bar.
	 * @param experience the exp.
	 * @param string the string.
	 */
	private BarType(int bar, double experience, String string) {
		this.bar = bar;
		this.string = string;
		this.experience = experience;
	}

	/**
	 * Retreive's the bar's type.
	 * @return bar type id;
	 */
	public int getBarType() {
		return bar;
	}

	/**
	 * Retreive's the bar's name.
	 * @return the bar's name.
	 */
	public String getBarName() {
		return string;
	}

	/**
	 * Gets the exp.
	 * @return the exp.
	 */
	public double getExperience() {
		return experience;
	}

	public static BarType getBarTypeForId(int itemId) {
		switch (itemId) {
		case 2349:
			return BarType.BRONZE;
		case 2351:
			return BarType.IRON;
		case 2353:
			return BarType.STEEL;
		case 2359:
			return BarType.MITHRIL;
		case 2361:
			return BarType.ADAMANT;
		case 2363:
			return BarType.RUNITE;
		case 9467:
			return BarType.BLURITE;
		}
		return null;
	}
}
