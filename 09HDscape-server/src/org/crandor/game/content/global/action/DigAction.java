package org.crandor.game.content.global.action;

import org.crandor.game.node.entity.player.Player;

/**
 * Handles a digging reward.
 * @author Emperor
 */
public interface DigAction {

	/**
	 * Runs the digging reward.
	 * @param player The player.
	 */
	void run(Player player);

}