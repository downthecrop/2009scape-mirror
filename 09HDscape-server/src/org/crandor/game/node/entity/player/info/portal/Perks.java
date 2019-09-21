package org.crandor.game.node.entity.player.info.portal;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.tools.RandomFunction;
import org.crandor.tools.StringUtils;

/**
 * Represents a perk.
 * @author Vexia
 *
 */
public enum Perks {
	STAMINA_BOOST(2, 0.40),
	GREEN_THUMB(4, 0.25),
	BIRD_MAN(5, .25),
	STONER(6, .25),
	UNBREAKABLE_FORGE(11),
	OUT_OF_GRAVE_DANGER(12),
	SLEIGHT_OF_HAND(13),
	MASTER_CHEF(14),
	DIVINE_INTERVENTION(16),
	FAMILIAR_WHISPERER(17),
	BARROWS_BEFRIENDER(18),
	ABYSS_BEFRIENDER(19),
	CHARGE_BEFRIENDER(21),
	GOLDEN_NEEDLE(22),
	SLAYER_BETRAYER(24),
	THIRST_QUENCHER(26),
	DOUBLE_TROUBLE(27),
	GWD_BEFRIENDER(29, 30),
	PRAYER_BETRAYER(30, 50),
	SPELL_SWAP(31, 3),
	DWARF_BEFRIENDER(32),
	POWERPOINT(33),
	CHARM_COLLECTOR(35),
	REGULAR_DONATOR(1000),
	EXTREME_DONATOR(1001),
	DETECTIVE(36),
	OVERCHARGE(40),
	UNBREAKABLE_CRYSTAL(41),
	CRUSADER(42),
	PET_BEFRIENDER(43),
	BONECRUSHER(60),
	RUNESTONE_KNOWLEDGE(70),
	COIN_MACHINE(71),
	FIGHT_CAVE_FANATIC(72),
	DECANTER(73),

	;

	/**
	 * The default rand value.
	 */
	private static final int DEFAULT_RAND = 100;

	/**
	 * The product id.
	 */
	private final int productId;

	/**
	 * Any data for the perk.
	 */
	private final Object[] data;

	/**
	 * The info for the perk.
	 */
	private final String info;

	/**
	 * Constructs a new {@code Perks} {@code Object}
	 * @param productId the product id.
	 * @param info the info.
	 * @param type the type.
	 * @param data data.
	 */
	private Perks(final int productId, String info, final Object... data) {
		this.productId = productId;
		this.data = data;
		this.info = info;
	}

	/**
	 * Constructs a new {@code Perks} {@code Object}.
	 * @param productId the product id.
	 * @param data the data.
	 */
	private Perks(final int productId, final Object... data) {
		this(productId, null, data);
	}

	/**
	 * Gets the double of an item if they have the perk.
	 * @param player the player.
	 * @param original the original.
	 * @param ground if we should add it to the ground.
	 * @param maxRand the maximum rand value.
	 * @return the item.
	 */
	public static boolean addDouble(Player player, Item item, boolean ground, int maxRand) {
		// 10 % chance to give an extra item
		if (!player.hasPerk(DOUBLE_TROUBLE) || RandomFunction.random(maxRand) > 10) {
			return false;
		}
		// Give the player an extra item!
		if (ground) {
			player.getInventory().add(item, player);
		} else {
			player.getInventory().add(item);
		}
		player.sendMessage("<col=3498db>You receive an extra " + item.getName().toLowerCase() + " from the Double Trouble perk!");
		return true;
	}

	/**
	 * Adds the double of an item.
	 * @param player the player.
	 * @param original the original.
	 */
	public static boolean addDouble(Player player, Item original, boolean ground) {
		return addDouble(player, original, ground, DEFAULT_RAND);
	}

	/**
	 * Adds the double of an item.
	 * @param player the player.
	 * @param original the original.
	 */
	public static boolean addDouble(Player player, Item original) {
		return addDouble(player, original, false, DEFAULT_RAND);
	}

	/**
	 * Adds the double of an item.
	 * @param player the player.
	 * @param original the original.
	 * @param maxRand the maximum rand value.
	 */
	public static boolean addDouble(Player player, Item original, int maxRand) {
		return addDouble(player, original, false, maxRand);
	}

	/**
	 * Gets the perk for the item.
	 * @param item the item.
	 * @return the perk.
	 */
	public static Perks forItem(Item item) {
		for (Perks perk : values()) {
			if (perk.getData() != null && perk.getData().length > 0) {
				for (Object o : perk.getData()) {
					if (o != null && o instanceof Item && ((Item) o).getId() == item.getId()) {
						return perk;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Gets a perk by the product id.
	 * @param productId the product id.
	 * @return the perk.
	 */
	public static Perks forId(int productId) {
		for (Perks perk : values()) {
			if (perk.getProductId() == productId) {
				return perk;
			}
		}
		return null;
	}

	/**
	 * Gets the info name.
	 * @return the name.
	 */
	public String getInfoName() {
		String name = getName();
		if (info == null || info.length() == 0) {
			return name;
		}
		return name + " " + info;
	}

	/**
	 * Gets the name.
	 * @return the name.
	 */
	public String getName() {
		return StringUtils.formatDisplayName(name().toLowerCase().replace("_", " "));
	}

	/**
	 * Gets the productId.
	 * @return The productId.
	 */
	public int getProductId() {
		return productId;
	}

	/**
	 * Gets the data.
	 * @return The data.
	 */
	public Object[] getData() {
		return data;
	}

	/**
	 * Gets the info.
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

}
