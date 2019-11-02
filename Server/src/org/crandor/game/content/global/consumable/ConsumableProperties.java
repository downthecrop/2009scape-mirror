package org.crandor.game.content.global.consumable;

import org.crandor.game.node.item.Item;

/**
 * Represents properties of a consumable. A consumable property lists the
 * healing amount, the new item when consumed, etc...
 * @author 'Vexia
 * @date 22/12/2013
 */
public class ConsumableProperties {

	/**
	 * Represents the amount the food can heal.
	 */
	private final int healing;

	/**
	 * Represents the new item when consumed (if any).
	 */
	private final Item newItem;

	/**
	 * Constructs a new {@code ConsumableProperties} {@code Object}.
	 * @param healing the healing amount.
	 * @param newItem the new item created (if any).
	 */
	public ConsumableProperties(int healing, Item newItem) {
		this.healing = healing;
		this.newItem = newItem;
	}

	/**
	 * Constructs a new {@code ConsumableProperties} {@code Object}.
	 * @param healing the healing amount.
	 * @param newItem the new item.
	 */
	public ConsumableProperties(int healing, int newItem) {
		this(healing, new Item(newItem));
	}

	/**
	 * Constructs a new {@code ConsumableProperties} {@code Object}.
	 * @param healing the healing power.
	 */
	public ConsumableProperties(int healing) {
		this(healing, null);
	}

	/**
	 * Gets the healing.
	 * @return The healing.
	 */
	public int getHealing() {
		return healing;
	}

	/**
	 * Gets the newItem.
	 * @return The newItem.
	 */
	public Item getNewItem() {
		return newItem;
	}

	/**
	 * Gets the value if the new item is not null.
	 * @return <code>True</code> if there is a new item.
	 */
	public boolean hasNewItem() {
		return getNewItem() != null && getNewItem().getId() > 0;
	}

}
