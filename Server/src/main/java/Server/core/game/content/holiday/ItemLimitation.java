package core.game.content.holiday;

import core.cache.ServerStore;
import core.cache.def.impl.ItemDefinition;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles limitation of holiday item releases.
 * @author Emperor
 */
public final class ItemLimitation {

	/**
	 * The items.
	 */
	private static final Map<Integer, Integer> ITEMS = new HashMap<>();

	/**
	 * Constructs a new {@code ItemLimitation} {@code Object}
	 */
	public ItemLimitation() {
		/*
		 * empty.
		 */
	}
	
	/**
	 * Parses the item limitation.
	 * @return {@code True} if parsed.
	 */
	public boolean parse() {
		if (ServerStore.get("hir_limits") == null) {
			return true;
		}
		ByteBuffer buffer = ServerStore.getArchive("hir_limits");
		int length = buffer.get() & 0xFF;
		for (int i = 0; i < length; i++) {
			int itemId = buffer.getShort() & 0xFFFF;
			int amount = buffer.getShort() & 0xFFFF;
			ITEMS.put(itemId, amount);
		}
		return true;
	}

	/**
	 * Dumps the item limitation data.
	 */
	public static void dump() {
		ByteBuffer buffer = ByteBuffer.allocate(ITEMS.size() * 4 + 1);
		buffer.put((byte) ITEMS.size());
		for (int itemId : ITEMS.keySet()) {
			buffer.putShort((short) itemId);
			buffer.putShort((short) (int) ITEMS.get(itemId));
		}
		buffer.flip();
		ServerStore.setArchive("hir_limits", buffer);
	}

	/**
	 * Registers a new item release.
	 * @param itemId The item id.
	 * @param amount The amount.
	 */
	public static void register(int itemId, int amount) {
		ITEMS.put(itemId, amount);
		System.out.println("Registered item release [name=" + ItemDefinition.forId(itemId).getName() + ", id=" + itemId + ", limit=" + amount + "].");
		dump();
	}

	/**
	 * Checks if the item has been registered.
	 * @param itemId The item id.
	 * @return {@code True} if so.
	 */
	public static boolean isRegistered(int itemId) {
		return ITEMS.containsKey(itemId);
	}

	/**
	 * Gets the amount left to release.
	 * @param itemId The item id.
	 * @return The amount left.
	 */
	public static int getAmountLeft(int itemId) {
		Integer amount = ITEMS.get(itemId);
		if (amount == null) {
			return -1;
		}
		return amount;
	}

	/**
	 * Decreases the amount left.
	 * @param itemId The item id.
	 * @return {@code True} if the full amount has been fully released.
	 */
	public static boolean decreaseAmount(int itemId) {
		if (!ITEMS.containsKey(itemId)) {
			return false;
		}
		int amount = ITEMS.get(itemId);
		if (amount > 0) {
			ITEMS.put(itemId, amount - 1);
			dump();
		}
		return amount < 1;
	}

	/**
	 * Gets the item limits.
	 * @return The item limits.
	 */
	public static Map<Integer, Integer> getItems() {
		return ITEMS;
	}
}