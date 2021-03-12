package core.game.container;

import core.game.node.item.Item;

/**
 * Represents a container event.
 * @author Emperor
 */
public final class ContainerEvent {

	/**
	 * Represents a null item.
	 */
	public static final Item NULL_ITEM = new Item(0, 0);

	/**
	 * The array of changed items.
	 */
	private final Item[] items;

	/**
	 * Clears the container.
	 */
	private boolean clear;

	/**
	 * Constructs a new {@code ContainerEvent} {@code Object}.
	 * @param size The container size.
	 */
	public ContainerEvent(int size) {
		this.items = new Item[size];
	}

	/**
	 * Flags a null item on the given slot.
	 * @param slot The slot.
	 */
	public void flagNull(int slot) {
		items[slot] = NULL_ITEM;
	}

	/**
	 * Flags an item on the given slot.
	 * @param slot The slot.
	 * @param item The item.
	 */
	public void flag(int slot, Item item) {
		items[slot] = item;
	}

	/**
	 * Gets the amount of item slots changed.
	 * @return The amount of item slots that have changed.
	 */
	public int getChangeCount() {
		int count = 0;
		for (Item item : items) {
			if (item != null) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Gets the updated slots.
	 * @return The slots array.
	 */
	public int[] getSlots() {
		int size = 0;
		int[] slots = new int[items.length];
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				slots[size++] = i;
			}
		}
		int[] slot = new int[size];
		for (int i = 0; i < size; i++) {
			slot[i] = slots[i];
		}
		return slot;
	}

	/**
	 * Gets the items.
	 * @return The items.
	 */
	public Item[] getItems() {
		return items;
	}

	/**
	 * Flags an empty container.
	 */
	public void flagEmpty() {
		this.clear = true;
		for (int i = 0; i < items.length; i++) {
			items[i] = null;
		}
	}

	/**
	 * Gets the clear.
	 * @return The clear.
	 */
	public boolean isClear() {
		return clear;
	}

	/**
	 * Sets the clear flag.
	 * @param clear The container is cleared.
	 */
	public void setClear(boolean clear) {
		this.clear = clear;
	}
}