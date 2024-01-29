package content.global.skill.herblore;

import org.rs09.consts.Items;
import core.game.node.item.Item;

/**
 * Represents an item that will can be ground by pestle and mortar.
 * @author Vexia
 */
public enum GrindingItem {
	UNICORN_HORN(new Item[] { new Item(Items.UNICORN_HORN_237) }, new Item(Items.UNICORN_HORN_DUST_235)),
	KEBBIT_TEETH(new Item[] { new Item(Items.KEBBIT_TEETH_10109) }, new Item(Items.KEBBIT_TEETH_DUST_10111)),
	BIRDS_NEST(new Item[] { new Item(Items.BIRDS_NEST_5075) }, new Item(Items.CRUSHED_NEST_6693)),
	GOAT_HORN(new Item[] { new Item(Items.DESERT_GOAT_HORN_9735) }, new Item(Items.GOAT_HORN_DUST_9736)),
	MUD_RUNE(new Item[] { new Item(Items.MUD_RUNE_4698) }, new Item(Items.GROUND_MUD_RUNES_9594)),
	ASHES(new Item[] { new Item(Items.ASHES_592) }, new Item(Items.GROUND_ASHES_8865)),
	POISON_KARAMBWAN(new Item[] { new Item(Items.POISON_KARAMBWAN_3146) }, new Item(Items.KARAMBWAN_PASTE_3152)),
	FISHING_BAIT(new Item[] { new Item(Items.FISHING_BAIT_313) }, new Item(Items.GROUND_FISHING_BAIT_12129)),
	SEAWEED(new Item[] { new Item(Items.SEAWEED_401) }, new Item(Items.GROUND_SEAWEED_6683)),
	BAT_BONES(new Item[] { new Item(Items.BAT_BONES_530) }, new Item(Items.GROUND_BAT_BONES_2391)),
	CHARCOAL(new Item[] { new Item(Items.CHARCOAL_973) }, new Item(Items.GROUND_CHARCOAL_704)),
	ASTRAL_RUNE_SHARDS(new Item[] { new Item(Items.ASTRAL_RUNE_SHARDS_11156) }, new Item(Items.GROUND_ASTRAL_RUNE_11155)),
	GARLIC(new Item[] { new Item(Items.GARLIC_1550) }, new Item(Items.GARLIC_POWDER_4668)),
	DRAGON_SCALE(new Item[] { new Item(Items.BLUE_DRAGON_SCALE_243) }, new Item(Items.DRAGON_SCALE_DUST_241)),
	ANCHOVIES(new Item[] { new Item(Items.ANCHOVIES_319) }, new Item(Items.ANCHOVY_PASTE_11266)),
	CHOCOLATE_BAR(new Item[] {new Item(Items.CHOCOLATE_BAR_1973)}, new Item(Items.CHOCOLATE_DUST_1975)),
	GUAM_LEAF(new Item[] { new Item(Items.CLEAN_GUAM_249) }, new Item(Items.GROUND_GUAM_6681));

	/**
	 * Represents the item to grind.
	 */
	private final Item[] items;

	/**
	 * Represents the product item.
	 */
	private final Item product;

	/**
	 * Constructs a new {@code GrindingItem} {@code Object}.
	 * @param items the items.
	 * @param product the product.
	 */
	GrindingItem(final Item[] items, final Item product) {
		this.items = items;
		this.product = product;
	}

	/**
	 * Gets the item.
	 * @return The item.
	 */
	public Item[] getItems() {
		return items;
	}

	/**
	 * Gets the product.
	 * @return The product.
	 */
	public Item getProduct() {
		return product;
	}

	/**
	 * Gets the grinding item.
	 * @param item the item.
	 * @return the item.
	 */
	public static GrindingItem forItem(final Item item) {
		for (GrindingItem g : values()) {
			for (Item i : g.getItems()) {
				if (i.getId() == item.getId()) {
					return g;
				}
			}
		}
		return null;
	}
}
