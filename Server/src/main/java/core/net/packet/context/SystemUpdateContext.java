package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * Represents a system update.
 * @author 'Vexia
 */
public class SystemUpdateContext implements Context {

	/**
	 * The <b>Player</b> instance.
	 */
	private Player player;

	/**
	 * The time.
	 */
	private int time;

	/**
	 * Constructs a new {@code SystemUpdateContext.java} {@code Object}.
	 * @param player the <b>Player</b>.
	 * @param time the time.
	 */
	public SystemUpdateContext(Player player, int time) {
		this.player = player;
		this.setTime(time);
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return the time
	 */
	public int getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(int time) {
		this.time = time;
	}

}
