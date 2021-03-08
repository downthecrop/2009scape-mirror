package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The interface config packet context.
 * @author Emperor
 */
public class InterfaceConfigContext implements Context {

	/**
	 * The player reference.
	 */
	private Player player;

	/**
	 * The interface id.
	 */
	private int interfaceId;

	/**
	 * The child id.
	 */
	private int childId;

	/**
	 * If the interface child should be hidden.
	 */
	private boolean hide;

	/**
	 * Construct a new {@code InterfaceConfigContext} {@code Object}.
	 * @param player The player reference.
	 * @param interfaceId The interface id.
	 * @param childId The child id.
	 * @param hide If the component should be hidden.
	 */
	public InterfaceConfigContext(Player player, int interfaceId, int childId, boolean hide) {
		this.player = player;
		this.interfaceId = interfaceId;
		this.childId = childId;
		this.hide = hide;
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

	/**
	 * If is set.
	 * @return If is set {@code true}.
	 */
	public boolean isHidden() {
		return hide;
	}

	@Override
	public Player getPlayer() {
		return player;
	}
}
