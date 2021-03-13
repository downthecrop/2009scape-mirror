package core.game.system.command;

import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.Rights;
import rs09.game.system.command.CommandPlugin;
import rs09.game.world.GameWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a command set.
 * @author Vexia
 */
public enum CommandSet {
	PLAYER(), 
	MODERATOR() {
		@Override
		public boolean validate(Player player) {
			return player.getDetails().getRights().ordinal() > 0;
		}
	},
	ADMINISTRATOR() {
		@Override
		public boolean validate(Player player) {
			return player.getDetails().getRights().equals(Rights.ADMINISTRATOR);
		}
	},
	DEVELOPER() {
		@Override
		public boolean validate(Player player) {
			return player.getDetails().getRights().equals(Rights.ADMINISTRATOR);
		}
	},
	BETA() {
		@Override
		public boolean validate(Player player) {
			return GameWorld.getSettings().isBeta() || ADMINISTRATOR.validate(player) || GameWorld.getSettings().isDevMode();
		}
	};

	/**
	 * Represents the list of linked plugins with this command set.
	 */
	private final List<CommandPlugin> plugins = new ArrayList<>(20);

	/**
	 * Constructs a new {@code CommandSet} {@code Object}.
	 */
	private CommandSet() {
		/*
		 * empty.
		 */
	}

	/**
	 * Checks if the player can use this set.
	 * @param player the player.
	 */
	public boolean validate(final Player player) {
		return true;
	}

	/**
	 * Interprets and incoming command by dispatching it to it's plugins.
	 * @param player the player.
	 * @param name the name.
	 * @param arguments the arguments.
	 * @return <code>True</code> if the command was interpreted.
	 */
	public boolean interpret(final Player player, final String name, final String... arguments) {
		if (player == null) {
			return false;
		}
		if (!validate(player)) {
			return false;
		}
		if (player.getZoneMonitor().parseCommand(player, name, arguments)) {
			return true;
		}
		for (int i = 0; i < plugins.size(); i++) {
			CommandPlugin plugin = plugins.get(i);
			if (!plugin.validate(player)) {
				continue;
			}
			if (plugin.parse(player, name, arguments)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the list of plugins of this command set.
	 * @return the plugins of this set.
	 */
	public List<CommandPlugin> getPlugins() {
		return plugins;
	}
}
