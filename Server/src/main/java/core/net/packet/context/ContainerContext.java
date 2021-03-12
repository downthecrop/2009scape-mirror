package core.net.packet.context;

import core.game.container.Container;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.net.packet.Context;

/**
 * Represents the context for the container packet.
 * @author Emperor
 */
public final class ContainerContext implements Context {

	/**
	 * The player.
	 */
	private Player player;

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * The child id.
	 */
	private final int childId;

	/**
	 * The container type.
	 */
	private final int type;

	/**
	 * The items.
	 */
	private final Item[] items;

	/**
	 * The length of the array to send.
	 */
	private final int length;

	/**
	 * If the container should be split up.
	 */
	private final boolean split;

	/**
	 * The slots we're changing.
	 */
	private final int[] slots;

	/**
	 * If the container should be cleared.
	 */
	private boolean clear;

	/**
	 * Constructs a new {@code ContainerContext} {@code Object}.
	 * @param player The player.
	 * @param interfaceId The interface id.
	 * @param childId The child id.
	 * @param clear If the container should be cleared.
	 */
	public ContainerContext(Player player, int interfaceId, int childId, boolean clear) {
		this(player, interfaceId, childId, 0, null, 1, false);
		this.clear = clear;
	}

	/**
	 * Constructs a new {@code ContainerContext} {@code Object}.
	 * @param player The player.
	 * @param interfaceId The interface id.
	 * @param childId The child id.
	 * @param type The container type.
	 * @param container The container.
	 * @param split If the container should be split.
	 */
	public ContainerContext(Player player, int interfaceId, int childId, int type, Container container, boolean split) {
		this(player, interfaceId, childId, type, container.toArray(), container.toArray().length, split);
	}

	/**
	 * Constructs a new {@code ContainerContext} {@code Object}.
	 * @param player The player.
	 * @param interfaceId The interface id.
	 * @param childId The child id.
	 * @param type The container type.
	 * @param items The items.
	 * @param split If the container should be split.
	 */
	public ContainerContext(Player player, int interfaceId, int childId, int type, Item[] items, boolean split) {
		this(player, interfaceId, childId, type, items, items.length, split);
	}

	/**
	 * Constructs a new {@code ContainerContext} {@code Object}.
	 * @param player The player.
	 * @param interfaceId The interface id.
	 * @param childId The child id.
	 * @param type The container type.
	 * @param items The items.
	 * @param length The length.
	 * @param split If the container should be split.
	 */
	public ContainerContext(Player player, int interfaceId, int childId, int type, Item[] items, int length, boolean split) {
		this.player = player;
		this.interfaceId = interfaceId;
		this.childId = childId;
		this.type = type;
		this.items = items;
		this.length = length;
		this.split = split;
		this.slots = null;
	}

	/**
	 * Constructs a new {@code ContainerContext} {@code Object}.
	 * @param player The player.
	 * @param interfaceId The interface id.
	 * @param childId The child id.
	 * @param type The container type.
	 * @param items The items.
	 * @param split If the container should be split.
	 * @param slots The slots to update.
	 */
	public ContainerContext(Player player, int interfaceId, int childId, int type, Item[] items, boolean split, int... slots) {
		this.player = player;
		this.interfaceId = interfaceId;
		this.childId = childId;
		this.type = type;
		this.items = items;
		this.length = items.length;
		this.split = split;
		this.slots = slots;
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the interfaceId.
	 * @return The interfaceId.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

	/**
	 * Gets the type.
	 * @return The type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * Gets the items.
	 * @return The items.
	 */
	public Item[] getItems() {
		return items;
	}

	/**
	 * Gets the length.
	 * @return The length.
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Gets the split.
	 * @return The split.
	 */
	public boolean isSplit() {
		return split;
	}

	/**
	 * Gets the slots.
	 * @return The slots.
	 */
	public int[] getSlots() {
		return slots;
	}

	/**
	 * Gets the childId.
	 * @return The childId.
	 */
	public int getChildId() {
		return childId;
	}

	/**
	 * Gets the clear.
	 * @return The clear.
	 */
	public boolean isClear() {
		return clear;
	}

	/**
	 * Sets the clear.
	 * @param clear The clear to set.
	 */
	public void setClear(boolean clear) {
		this.clear = clear;
	}

}