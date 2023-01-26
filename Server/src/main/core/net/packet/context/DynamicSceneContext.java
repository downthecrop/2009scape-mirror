package core.net.packet.context;

import core.game.node.entity.player.Player;

/**
 * Represents the packet context for the build dynamic scene graph packet.
 * @author Emperor
 */
public final class DynamicSceneContext extends SceneGraphContext {

	/**
	 * Constructs a new {@code DynamicSceneContext} {@code Object}.
	 * @param player The player.
	 * @param login If the player is logging in.
	 */
	public DynamicSceneContext(Player player, boolean login) {
		super(player, login);
	}

}