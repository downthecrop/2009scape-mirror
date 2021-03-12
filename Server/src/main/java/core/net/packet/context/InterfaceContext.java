package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The interface packet context.
 * @author Emperor
 */
public final class InterfaceContext implements Context {

	/**
	 * The player.
	 */
	private Player player;

	/**
	 * The window id.
	 */
	private final int windowId;

	/**
	 * The component id.
	 */
	private int componentId;

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * If the interface can be walked over.
	 */
	private final boolean walkable;

	/**
	 * Constructs a new {@code InterfaceContext} {@code Object}.
	 * @param player The player.
	 * @param windowId The window id.
	 * @param componentId The window component id.
	 * @param interfaceId The interface id.
	 * @param walkable If we can walk over the interface.
	 */
	public InterfaceContext(Player player, int windowId, int componentId, int interfaceId, boolean walkable) {
		this.player = player;
		this.windowId = windowId;
		this.componentId = componentId;
		this.interfaceId = interfaceId;
		this.walkable = walkable;
	}

	/**
	 * Transforms this context for the new player & id.
	 * @param player The player.
	 * @param id The new interface id.
	 * @return The interface context created.
	 */
	public InterfaceContext transform(Player player, int id) {
		return new InterfaceContext(player, windowId, componentId, id, walkable);
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
	 * Gets the windowId.
	 * @return The windowId.
	 */
	public int getWindowId() {
		return windowId;
	}

	/**
	 * Sets the component id.
	 * @param componentId The component id.
	 */
	public void setComponentId(int componentId) {
		this.componentId = componentId;
	}

	/**
	 * Gets the componentId.
	 * @return The componentId.
	 */
	public int getComponentId() {
		return componentId;
	}

	/**
	 * Gets the interfaceId.
	 * @return The interfaceId.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

	/**
	 * Gets the walkable.
	 * @return The walkable.
	 */
	public boolean isWalkable() {
		return walkable;
	}

}