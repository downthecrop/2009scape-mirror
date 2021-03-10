package core.game.node.entity.player.link;

import core.game.node.entity.player.Player;


import java.nio.ByteBuffer;

/**
 * Manages the iron man mode of an account.
 * @author Vexia
 * 
 */
public class IronmanManager {

	/**
	 * The player instance.
	 */
	private final Player player;

	/**
	 * The iron man mode.
	 */
	private IronmanMode mode = IronmanMode.NONE;

	/**
	 * Constructs a new {@code IronmanManager} {@code Object}
	 * @param player the player.
	 */
	public IronmanManager(Player player) {
		this.player = player;
	}

	/**
	 * Checks the restriction.
	 * @return {@code True} if so.
	 */
	public boolean checkRestriction() {
		return checkRestriction(IronmanMode.STANDARD);
	}

	/**
	 * Checks the restriction.
	 * @return {@code True} if so.
	 */
	public boolean checkRestriction(IronmanMode mode) {
		if (isIronman() && this.mode.ordinal() >= mode.ordinal()) {
			player.sendMessage("You can't do that as an Ironman.");
			return true;
		}
		return false;
	}

	/**
	 * Checks if the player is an ironman.
	 * @return {@code True} if one.
	 */
	public boolean isIronman() {
		return mode != IronmanMode.NONE;
	}

	/**
	 * Gets the player.
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the mode.
	 * @return the mode
	 */
	public IronmanMode getMode() {
		return mode;
	}

	/**
	 * Sets the mode.
	 * @param mode the mode to set.
	 */
	public void setMode(IronmanMode mode) {
		this.mode = mode;
	}

}
