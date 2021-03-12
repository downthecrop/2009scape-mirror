package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The config packet context.
 * @author Emperor
 */
public class ConfigContext implements Context {

	/**
	 * The player reference.
	 */
	private Player player;

	/**
	 * The config id.
	 */
	private int id;

	/**
	 * The config value.
	 */
	private int value;

	/**
	 * If the config is a cs2-config.
	 */
	private boolean cs2;

	/**
	 * Construct a new {@code ConfigContext} {@code Object}.
	 * @param player The player reference.
	 * @param id The config id.
	 * @param value The config value.
	 */
	public ConfigContext(Player player, int id, int value) {
		this(player, id, value, false);
	}

	/**
	 * Construct a new {@code ConfigContext} {@code Object}.
	 * @param player The player reference.
	 * @param id The config id.
	 * @param value The config value.
	 */
	public ConfigContext(Player player, int id, int value, boolean cs2) {
		this.player = player;
		this.id = id;
		this.value = value;
		this.cs2 = cs2;
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	/**
	 * Sets the player.
	 * @param player The player.
	 * @return This context instance.
	 */
	public Context setPlayer(Player player) {
		this.player = player;
		return this;
	}

	/**
	 * Get the config id.
	 * @return The config id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Get the config value.
	 * @return The config value.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Gets the cs2.
	 * @return The cs2.
	 */
	public boolean isCs2() {
		return cs2;
	}
}
