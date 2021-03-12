package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The <b>Integer</b> context.
 * @author 'Vexia
 */
public class IntegerContext implements Context {

	/**
	 * The player instance.
	 */
	private Player player;

	/**
	 * The integer value.
	 */
	private int integer;

	/**
	 * Constructs a new {@code IntegerContext.java} {@code Object}.
	 * @param player the player.
	 * @param integer the integer.
	 */
	public IntegerContext(Player player, int integer) {
		this.player = player;
		this.setInteger(integer);
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return the integer.
	 */
	public int getInteger() {
		return integer;
	}

	/**
	 * @param integer the integer to set.
	 */
	public void setInteger(int integer) {
		this.integer = integer;
	}

}
