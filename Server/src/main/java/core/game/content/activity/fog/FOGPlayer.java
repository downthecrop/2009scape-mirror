package core.game.content.activity.fog;

import core.game.node.entity.player.Player;

/**
 * Represents a fist of guthix player.
 * @author Vexia
 */
public class FOGPlayer {

	/**
	 * The player instance.
	 */
	private final Player player;

	/**
	 * The target FOG player.
	 */
	private final FOGPlayer target;

	/**
	 * If the player is hunted or a hunter.
	 */
	private boolean hunted;

	/**
	 * The amount of fist of guthix charges.
	 */
	private int charges;

	/**
	 * Constructs a new {@code FOGPlayer} {@code Object}
	 * @param player the player.
	 * @param oponent the other player.
	 */
	public FOGPlayer(Player player, FOGPlayer oponent) {
		this.player = player;
		this.target = oponent;
	}

	/**
	 * Switches the roles of the player.
	 */
	public void switchRoles() {
		hunted = !hunted;
	}

	/**
	 * Increments the energy charges.
	 * @param increment the number to increment.
	 */
	public void incrementCharges(int increment) {
		charges += increment;
	}

	/**
	 * Gets the hunted.
	 * @return the hunted
	 */
	public boolean isHunted() {
		return hunted;
	}

	/**
	 * Sets the hunted.
	 * @param hunted the hunted to set.
	 */
	public void setHunted(boolean hunted) {
		this.hunted = hunted;
	}

	/**
	 * Gets the charges.
	 * @return the charges
	 */
	public int getCharges() {
		return charges;
	}

	/**
	 * Sets the charges.
	 * @param charges the charges to set.
	 */
	public void setCharges(int charges) {
		this.charges = charges;
	}

	/**
	 * Gets the player.
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the target.
	 * @return the target
	 */
	public FOGPlayer getTarget() {
		return target;
	}

}
