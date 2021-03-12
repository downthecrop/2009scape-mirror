package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The run script packet context.
 * @author Emperor
 */
public class RunScriptContext implements Context {

	/**
	 * The player reference.
	 */
	private Player player;

	/**
	 * The run script id.
	 */
	private int id;

	/**
	 * The parameters.
	 */
	private Object[] objects;

	/**
	 * The string.
	 */
	private String string;

	/**
	 * Construct a new {@code RunScriptContext} {@code Object}.
	 * @param player
	 * @param id
	 * @param string
	 * @param objects
	 */
	public RunScriptContext(Player player, int id, String string, Object... objects) {
		this.player = player;
		this.id = id;
		this.objects = objects;
		this.string = string;
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
	 * Get the run scripts id.
	 * @return The run scripts id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Get the objects.
	 * @return The objects.
	 */
	public Object[] getObjects() {
		return objects;
	}

	/**
	 * Get the string.
	 * @return The string.
	 */
	public String getString() {
		return string;
	}
}
