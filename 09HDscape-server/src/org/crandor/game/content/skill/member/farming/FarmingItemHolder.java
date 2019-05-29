package org.crandor.game.content.skill.member.farming;

import org.crandor.game.node.item.Item;

/**
 * Represents the a holder for a farming item.
 * @author 'Vexia
 * @version 1.0
 */
public enum FarmingItemHolder {
	CABBAGE(new Item(1965), new Item[] { new Item(5460), new Item(5462), new Item(5464), new Item(5466), new Item(5468), new Item(5470), new Item(5472), new Item(5474), new Item(5476), new Item(5478) }), ONION(new Item(1957), new Item[] { new Item(5440), new Item(5442), new Item(5444), new Item(5446), new Item(5448), new Item(5450), new Item(5452), new Item(5454), new Item(5456), new Item(5458) }), POTATO(new Item(1942), new Item[] { new Item(5420), new Item(5422), new Item(5424), new Item(5426), new Item(5428), new Item(5430), new Item(5432), new Item(5434), new Item(5436), new Item(5438) }), APPLE(new Item(1955), new Item[] { new Item(5378), new Item(5380), new Item(5382), new Item(5384), new Item(5386) }), BANANA(new Item(1963), new Item[] { new Item(5408), new Item(5410), new Item(5412), new Item(5414), new Item(5416) }), ORANGE(new Item(2108), new Item[] { new Item(5388), new Item(5390), new Item(5392), new Item(5394), new Item(5396) }), STRAWBERRY(new Item(5504), new Item[] { new Item(5398), new Item(5400), new Item(5402), new Item(5404), new Item(5406) }), TOMATO(new Item(1982), new Item[] { new Item(5960), new Item(5962), new Item(5964), new Item(5966), new Item(5968) });

	/**
	 * Represents the empty sack item.
	 */
	private static final Item SACK = new Item(5418);

	/**
	 * Represents the empty basket item.
	 */
	private static final Item BASKET = new Item(5376);

	/**
	 * Represents the item.
	 */
	private final Item item;

	/**
	 * Represents the item holders.
	 */
	private final Item[] holders;

	/**
	 * Constructs a new {@code ItemHolder} {@code Object}.
	 * @param item the item.
	 * @param holders the holders.
	 */
	private FarmingItemHolder(Item item, Item[] holders) {
		this.item = item;
		this.holders = holders;
	}

	/**
	 * Gets the item by the amount.
	 * @param amount the amount.
	 * @return the item.
	 */
	public Item getItem(int amount) {
		if (amount - 1 < 0) {
			return ordinal() < 3 ? SACK : BASKET;
		}
		return holders[amount - 1];
	}

	/**
	 * Gets the item index.
	 * @param item the item.
	 * @return the item index.
	 */
	public int getItemIndex(Item item) {
		for (int i = 0; i < holders.length; i++) {
			if (holders[i].getId() == item.getId()) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the amount of an item.
	 * @param item the item.
	 * @return the amount.
	 */
	public int getAmount(Item item) {
		return getItemIndex(item) + 1;
	}

	/**
	 * Gets the holder by the item.
	 * @param item the item.
	 * @return the holder.
	 */
	public static FarmingItemHolder forItem(final Item item) {
		for (FarmingItemHolder holder : values()) {
			if (holder.getItem().getId() == item.getId()) {
				return holder;
			}
		}
		return null;
	}

	/**
	 * Gets the item holder by the id.
	 * @param item the item.
	 * @return the holder.
	 */
	public static FarmingItemHolder forHolder(final Item item) {
		for (FarmingItemHolder holder : values()) {
			for (Item i : holder.getHolders()) {
				if (i.getId() == item.getId()) {
					return holder;
				}
			}
		}
		return null;
	}

	/**
	 * Checks if it is a basket.
	 * @return {@code True} if so.
	 */
	public boolean isBasket() {
		return ordinal() > 2;
	}

	/**
	 * Gets the item.
	 * @return The item.
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * Gets the holders.
	 * @return The holders.
	 */
	public Item[] getHolders() {
		return holders;
	}
}
