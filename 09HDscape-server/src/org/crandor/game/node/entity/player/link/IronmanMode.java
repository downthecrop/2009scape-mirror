package org.crandor.game.node.entity.player.link;

/**
 * A type of iron man mode.
 * @author Vexia
 */
public enum IronmanMode {
	NONE(-1), STANDARD(14), ULTIMATE(15);

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
