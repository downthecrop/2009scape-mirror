package core.game.node.entity.player.link.request;

import core.game.node.entity.player.Player;

/**
 * Represents the module used for all request types.
 * @author 'Vexia
 */
public interface RequestModule {

	/**
	 * Method invoked when the targeting player accepts a request.
	 * @param player the player.
	 * @param target the target.
	 */
	public void open(final Player player, final Player target);

}
