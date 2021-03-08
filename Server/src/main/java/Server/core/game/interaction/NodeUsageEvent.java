package core.game.interaction;

import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;

/**
 * Represents a node use-with (other node) option.
 * @author Emperor
 */
public final class NodeUsageEvent {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The component id.
	 */
	private final int componentId;

	/**
	 * The used node.
	 */
	private final Node used;

	/**
	 * The node we used the other node on.
	 */
	private final Node with;

	/**
	 * Constructs a new {@code NodeUsageEvent} {@code Object}.
	 * @param player The player.
	 * @param componentId The component id.
	 * @param used The used node.
	 * @param with The node the other node is used on.
	 */
	public NodeUsageEvent(Player player, int componentId, Node used, Node with) {
		this.player = player;
		this.componentId = componentId;
		this.used = used;
		this.with = with;
	}

	/**
	 * Gets the base item.
	 * @return The base item.
	 */
	public Item getBaseItem() {
		return with instanceof Item ? (Item) with : null;
	}

	/**
	 * Gets the used item.
	 * @return The used item.
	 */
	public Item getUsedItem() {
		return used instanceof Item ? (Item) used : null;
	}

	/**
	 * Gets the player.
	 * @return The player.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the componentId.
	 * @return The componentId.
	 */
	public int getComponentId() {
		return componentId;
	}

	/**
	 * Gets the used.
	 * @return The used.
	 */
	public Node getUsed() {
		return used;
	}

	/**
	 * The node the other node is used on.
	 * @return The node.
	 */
	public Node getUsedWith() {
		return with;
	}

}