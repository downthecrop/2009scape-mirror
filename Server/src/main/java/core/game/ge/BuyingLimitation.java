package core.game.ge;

import core.cache.def.impl.ItemDefinition;
import rs09.game.system.config.ItemConfigParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles the buying limitations.
 * @author Emperor
 */
public final class BuyingLimitation {

	/**
	 * Mapping holding buying amounts per player for every item id.
	 */
	private static final Map<Integer, Map<Integer, Integer>> CACHE = new HashMap<>();

	/**
	 * Constructs a new {@code BuyingLimitation} {@code Object}.
	 */
	private BuyingLimitation() {
		/*
		 * empty.
		 */
	}

	/**
	 * Gets the maximum amount the player can buy of this item.
	 * @param itemId The item id.
	 * @param playerUID The player UID.
	 * @return The maximum amount to buy.
	 */
	public static int getMaximumBuy(int itemId, int playerUID) {
		Map<Integer, Integer> data = CACHE.get(itemId);
		Integer current = 0;
		if (data != null) {
			current = data.get(playerUID);
		}
		if (current == null) {
			current = 0;
		}
		return ItemDefinition.forId(itemId).getConfiguration(ItemConfigParser.GE_LIMIT, 25000) - current;
	}

	/**
	 * Updates the currently bought amount.
	 * @param itemId The item id.
	 * @param playerUID The player UID.
	 * @param amount The amount.
	 */
	public static void updateBoughtAmount(int itemId, int playerUID, int amount) {
		Map<Integer, Integer> data = CACHE.get(itemId);
		if (data == null) {
			CACHE.put(itemId, data = new HashMap<>());
		}
		Integer current = data.get(playerUID);
		if (current == null) {
			current = 0;
		}
		current += amount;
		data.put(playerUID, current);
	}

	/**
	 * Checks if the offer is limited.
	 * @return {@code True} if so.
	 */
	public static boolean isLimited(int itemId, int playerUID) {
		Map<Integer, Integer> data = CACHE.get(itemId);
		if (data == null) {
			return false;
		}
		Integer current = data.get(playerUID);
		if (current == null) {
			return false;
		}
		int max = ItemDefinition.forId(itemId).getConfiguration(ItemConfigParser.GE_LIMIT, 25000);
		return current >= max;
	}

	/**
	 * Clears the cached data.
	 */
	public static void clear() {
		CACHE.clear();
	}
}