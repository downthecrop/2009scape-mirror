package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The animate interface context.
 * @author Emperor
 */
public class AnimateInterfaceContext implements Context {

	/**
	 * The player reference.
	 */
	private Player player;

	/**
	 * The animation id.
	 */
	private int animationId;

	/**
	 * The interface id.
	 */
	private int interfaceId;

	/**
	 * The child id.
	 */
	private int childId;

	/**
	 * Construct a new {@code AnimateInterfaceContext} {@code Object}.
	 * @param player The player reference.
	 * @param animationId The animation id.
	 * @param interfaceId The interface id.
	 * @param childId The child id.
	 */
	public AnimateInterfaceContext(Player player, int animationId, int interfaceId, int childId) {
		this.player = player;
		this.animationId = animationId;
		this.interfaceId = interfaceId;
		this.childId = childId;
	}

	/**
	 * Get the animation id.
	 * @return The animation id.
	 */
	public int getAnimationId() {
		return animationId;
	}

	/**
	 * Get the interface id.
	 * @return The interface id.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

	/**
	 * Get the child id.
	 * @return The child id.
	 */
	public int getChildId() {
		return childId;
	}

	@Override
	public Player getPlayer() {
		return player;
	}
}
