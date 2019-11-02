package org.crandor.game.content.global;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;

/**
 * Represents a light source.
 * @author Vexia
 * @author Emperor
 */
public enum LightSource {
	CANDLE(1, new Item(36, 1), new Item(33, 1), true, 98),
	BLACK_CANDLE(1, new Item(38, 1), new Item(32, 1), true, 98),
	TORCH(1, new Item(596, 1), new Item(594, 1), true, 98), 
	CANDLE_LANTERN(4, new Item(4527, 1), new Item(4531, 1), false, 98),
	OIL_LAMP(12, new Item(4522, 1), new Item(4524, 1), true, 97),
	OIL_LANTERN(26, new Item(4535, 1), new Item(4539, 1), false, 97), 
	BULLSEYE_LANTERN(49, new Item(4548, 1), new Item(4550, 1), false, -1),
	SAPPHIRE_LANTERN(49, new Item(4701, 1), new Item(4702, 1), false, -1),
	EMERALD_LANTERN(49, new Item(9064, 1), new Item(9065, 1), false, -1),
	MINING_HELMET(65, new Item(5014, 1), new Item(5013, 1), false, 97);

	/**
	 * Represents the level required.
	 */
	private int level;

	/**
	 * Represents the raw item.
	 */
	private Item raw;

	/**
	 * Represents the product.
	 */
	private Item product;

	/**
	 * If the light source is open (eg. candles, torches, ...).
	 */
	private final boolean open;

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * Constructs a new {@code LightSource} {@code Object}.
	 * @param level the level.
	 * @param raw the raw.
	 * @param product the product.
	 * @param open If it's an open light source.
	 * @param interfaceId The overlay interface id to display.
	 */
	LightSource(int level, Item raw, Item product, boolean open, int interfaceId) {
		this.level = level;
		this.raw = raw;
		this.product = product;
		this.open = open;
		this.interfaceId = interfaceId;
	}

	/**
	 * Checks if the player has a lit light source.
	 * @param player The player.
	 * @return {@code True} if so.
	 */
	public static boolean hasActiveLightSource(Player player) {
		if (SkillcapePerks.hasSkillcapePerk(player, SkillcapePerks.FIREMAKING)) {
			return true;
		}
		return getActiveLightSource(player) != null;
	}

	/**
	 * Gets the light source used by the player.
	 * @param player The player.
	 * @return The light source object, or null if the player didn't have any
	 * light source.
	 */
	public static LightSource getActiveLightSource(Player player) {
		LightSource source;
		for (Item item : player.getInventory().toArray()) {
			if (item != null && (source = forProductId(item.getId())) != null) {
				return source;
			}
		}
		for (Item item : player.getEquipment().toArray()) {
			if (item != null && (source = forProductId(item.getId())) != null) {
				return source;
			}
		}
		if (SkillcapePerks.hasSkillcapePerk(player, SkillcapePerks.FIREMAKING)) {
			return OIL_LANTERN;
		}
		return null;
	}

	/**
	 * Gets the light source by the id.
	 * @param id the id.
	 * @return the source.
	 */
	public static LightSource forId(int id) {
		for (LightSource light : LightSource.values()) {
			if (light.raw.getId() == id) {
				return light;
			}
		}
		return null;
	}

	/**
	 * Gets the light souce by the product id.
	 * @param id the id.
	 * @return the light source.
	 */
	public static LightSource forProductId(int id) {
		for (LightSource light : LightSource.values()) {
			if (light.product.getId() == id) {
				return light;
			}
		}
		return null;
	}

	/**
	 * Gets the strength of the light source (1=dim, 2=medium, 3=bright).
	 * @return The strength.
	 */
	public int getStrength() {
		switch (interfaceId) {
		case 97:
			return 1;
		case 98:
			return 2;
		case -1:
			return 3;
		}
		return 0;
	}

	/**
	 * Gets the name of the light source.
	 * @return The name.
	 */
	public String getName() {
		return super.name().toLowerCase().replaceAll("_", " ");
	}

	/**
	 * Gets the raw item.
	 * @return the raw.
	 */
	public Item getRaw() {
		return raw;
	}

	/**
	 * Gets the product.
	 * @return the product.
	 */
	public Item getProduct() {
		return product;
	}

	/**
	 * Gets the level.
	 * @return the level.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Gets the open.
	 * @return The open.
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * Gets the interfaceId.
	 * @return The interfaceId.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

	/**
	 * Gets the ids of the raw items.
	 * @return The raw item ids.
	 */
	public static int[] getRawIds() {
		int array[] = new int[LightSource.values().length];
		for (int i = 0; i < LightSource.values().length -1; i++) {
			array[i] = LightSource.values()[i].getRaw().getId();
		}
		return array;
	}

}