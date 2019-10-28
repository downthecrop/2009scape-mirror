package org.crandor.game.system.command;

import org.crandor.game.node.entity.player.Player;

/**
 * Represents a managing system used to dispatch incoming commands.
 * @author Vexia
 */
public final class CommandSystem {

	/**
	 * Represents the singleton instance of this class.
	 */
	private static final CommandSystem SINGLETON = new CommandSystem();

	/**
	 * Method used to parse an incomming command packet.
	 * @param player the player.
	 * @param message the command message.
	 */
	public final boolean parse(final Player player, final String message) {
		final String[] arguments = message.split(" ");
		for (CommandSet set : CommandSet.values()) {
			if (set.interpret(player, arguments[0], arguments)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the command system instance.
	 * @return the instance.
	 */
	public static final CommandSystem getCommandSystem() {
		return SINGLETON;
	}

}
