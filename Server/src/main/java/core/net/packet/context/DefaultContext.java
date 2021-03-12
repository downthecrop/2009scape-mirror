package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * Represents a default context of a packet.
 * @author 'Vexia
 */
public class DefaultContext implements Context {
	/**
	 * Represents the {@link Player} instance.
	 */
	private Player player;
	/**
	 * Represents the array of objects casted.
	 */
	private Object[] objects;

	/**
	 * Constructs a new {@code DefaultContext.java} {@code Object}.
	 * @param player the player.
	 * @param objects the objects.
	 */
	public DefaultContext(Player player, Object... objects) {
		this.player = player;
		this.objects = objects;
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return the objects.
	 */
	public Object[] getObjects() {
		return objects;
	}

	/**
	 * @param player the player to set.
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @param objects the objects to set.
	 */
	public void setObjects(Object[] objects) {
		this.objects = objects;
	}

}
