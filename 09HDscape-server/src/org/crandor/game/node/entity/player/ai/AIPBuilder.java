package org.crandor.game.node.entity.player.ai;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.map.Location;

/**
 * Used for "building" artificial intelligent players.
 * @author Emperor
 */
public final class AIPBuilder {

	/**
	 * Creates a new artificial intelligent player.
	 * @param name The name.
	 * @return The AIPlayer object.
	 */
	public static AIPlayer create(String name, Location l) {
		return new AIPlayer(name, l);
	}

	/**
	 * Makes an artificial intelligent copy of the player.
	 * @param player The player.
	 * @return The artificial intelligent player with the same name, stats,
	 * items, etc.
	 */
	public static AIPlayer copy(Player player) {
		return copy(player, player.getName(), player.getLocation());
	}

	/**
	 * Makes an artificial intelligent copy of the player.
	 * @param player The player.
	 * @param l The location the AIP should spawn on.
	 * @return The artificial intelligent player with the same name, stats,
	 * items, etc.
	 */
	public static AIPlayer copy(Player player, Location l) {
		return copy(player, player.getName(), l);
	}

	/**
	 * Makes an artificial intelligent copy of the player.
	 * @param player The player.
	 * @param name The AIP's name.
	 * @param l The location the AIP should spawn on.
	 * @return The artificial intelligent player with the same name, stats,
	 * items, etc.
	 */
	public static AIPlayer copy(Player player, String name, Location l) {
		AIPlayer p = new AIPlayer(name, l);
		p.getSkills().copy(player.getSkills());
		p.getInventory().copy(player.getInventory());
		p.getEquipment().copy(player.getEquipment());
		p.getBank().copy(player.getBank());
		p.getAppearance().copy(player.getAppearance());
		p.setControler(player);
		return p;
	}
}