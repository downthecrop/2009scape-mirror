package org.crandor.net.packet.context;

import org.crandor.game.node.entity.player.Player;
import org.crandor.net.packet.Context;

/**
 * The default packet context.
 * @author Emperor
 */
public final class PlayerContext implements Context {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Constructs a new {@code PlayerContext} {@code Object}.
	 * @param player The player.
	 */
	public PlayerContext(Player player) {
		this.player = player;
	}

	@Override
	public Player getPlayer() {
		return player;
	}

}