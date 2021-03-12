package core.game.node.entity.player.link;

/**
 * A type of iron man mode.
 * @author Vexia
 */
public enum IronmanMode {
	// HARDCORE_DEAD has to be before Ultimate so that it does not adopt it's restrictions (on the basis of >= in IronmanManager.java?)
	NONE(-1), STANDARD(5), HARDCORE(6), ULTIMATE(7);

	/**
	 * The icon id.
	 */
	private final int icon;

	/**
	 * Constructs a new {@code IronmanMode} {@code Object}
	 * @param icon the icon.
	 */
	private IronmanMode(int icon) {
		this.icon = icon;
	}

	/**
	 * Gets the icon.
	 * @return the icon
	 */
	public int getIcon() {
		return icon;
	}

}
