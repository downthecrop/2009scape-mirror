package org.crandor.net.packet;

import org.crandor.game.node.entity.player.Player;

/**
 * Represents packet context.
 * @author Emperor
 */
public interface Context {

	/**
	 * Gets the node.
	 * @return The node.
	 */
	public Player getPlayer();

}