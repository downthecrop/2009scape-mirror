package core.game.node.entity.skill.crafting.spinning;

/**
 * Represents an item to be spinned.
 * @author 'Vexia
 */
public enum SpinningItem {
	WOOL(19, 1737, 1759, 1, 2.5), FLAX(17, 1779, 1777, 10, 15), ROOT(23, 6051, 6038, 19, 30), ROOT1(23, 6043, 6038, 19, 30), ROOT2(23, 6045, 6038, 19, 30), ROOT3(23, 6047, 6038, 19, 30), ROOT4(23, 6049, 6038, 19, 30), ROOT5(23, 6053, 6038, 19, 30), SINEW(27, 9436, 9438, 10, 15), TREE_ROOTS(31, 6043, 9438, 10, 15), YACK(35, 10814, 954, 30, 25);

	/**
	 * The button id.
	 */
	private final int button;

	/**
	 * The needed item.
	 */
	private final int need;

	/**
	 * The product.
	 */
	private final int product;

	/**
	 * The level required.
	 */
	private final int level;

	/**
	 * The exp gained.
	 */
	private final double exp;

	/**
	 * Constructs a new {@code SpinningItem} {@code Object}.
	 * @param button the button.
	 * @param need the needed item id.
	 * @param product the product.
	 * @param level the level.
	 * @param exp the exp.
	 */
	SpinningItem(int button, int need, int product, int level, double exp) {
		this.button = button;
		this.need = need;
		this.product = product;
		this.level = level;
		this.exp = exp;
	}

	/**
	 * Gets the spinning item.
	 * @param id the id.
	 * @return the item.
	 */
	public static SpinningItem forId(int id) {
		for (SpinningItem spin : SpinningItem.values()) {
			if (spin.button == id) {
				return spin;
			}
		}
		return null;
	}

	/**
	 * Gets the button.
	 * @return The button.
	 */
	public int getButton() {
		return button;
	}

	/**
	 * Gets the need.
	 * @return The need.
	 */
	public int getNeed() {
		return need;
	}

	/**
	 * Gets the product.
	 * @return The product.
	 */
	public int getProduct() {
		return product;
	}

	/**
	 * Gets the level.
	 * @return The level.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Gets the exp.
	 * @return The exp.
	 */
	public double getExp() {
		return exp;
	}

}
