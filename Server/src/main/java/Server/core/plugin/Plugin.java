package core.plugin;

import core.game.node.entity.player.Player;

/**
 * Represents a plugin.
 * @author Emperor
 * @param <T> The argument type.
 */
public interface Plugin<T> {

	/**
	 * Creates a new instance.
	 * @param arg The argument.
	 * @return The plugin instance created.
	 */
	public Plugin<T> newInstance(T arg) throws Throwable;

	/**
	 * Fires a plugin event.
	 * @param identifier The identifier.
	 * @param args The arguments.
	 * @return Specified by the plugin implementation.
	 */
	Object fireEvent(String identifier, Object... args);

	public default void handleSelectionCallback(int skill, Player player){};

}