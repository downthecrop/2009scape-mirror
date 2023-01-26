package core.game.node.entity.player.link;

import core.game.node.entity.player.Player;

/**
 * Represents a run script.
 * @author Vexia
 */
public abstract class RunScript {

	/**
	 * The player instance.
	 */
	protected Player player;

	/**
	 * The value entered.
	 */
	protected Object value;

	/**
	 * Constructs a new {@code RunScript} {@code Object}.
	 */
	public RunScript() {
		/**
		 * empty.
		 */
	}

	/**
	 * Handles the run script.
	 * @return the return.
	 */
	public abstract boolean handle();

	/**
	 * @return the value.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value the value to set;.
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Sets the player.
	 * @param player the player.
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
}
