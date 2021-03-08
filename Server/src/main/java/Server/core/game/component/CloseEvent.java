package core.game.component;

import core.game.node.entity.player.Player;

/**
 * An event called when the interface gets closed.
 * @author Emperor
 */
public interface CloseEvent {

	/**
	 * Called when the interface gets closed.
	 * @param player The player.
	 * @param c The component.
	 * @return {@code True} if successful, {@code false} if the component should
	 * remain open.
	 */
	boolean close(Player player, Component c);

}