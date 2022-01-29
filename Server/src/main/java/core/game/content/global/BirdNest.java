package core.game.content.global;

import org.rs09.consts.Items;
import core.game.node.entity.npc.drop.NPCDropTables;
import core.game.node.entity.player.Player;
import core.game.node.item.ChanceItem;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.tools.RandomFunction;
import core.tools.StringUtils;

/**
 * Represents a birds nest.
 * @author Vexia
 */
public enum BirdNest {
	SEED(new ChanceItem(5073, 1, 65),
			new ChanceItem(Items.ACORN_5312, 1, NPCDropTables.DROP_RATES[0]),
			new ChanceItem(Items.APPLE_TREE_SEED_5283, 1, NPCDropTables.DROP_RATES[0]),
			new ChanceItem(Items.WILLOW_SEED_5313, 1, NPCDropTables.DROP_RATES[0]),			
			new ChanceItem(Items.BANANA_TREE_SEED_5284, 1, NPCDropTables.DROP_RATES[0]),
			new ChanceItem(Items.ORANGE_TREE_SEED_5285, 1, NPCDropTables.DROP_RATES[0]),
			new ChanceItem(Items.CURRY_TREE_SEED_5286, 1, NPCDropTables.DROP_RATES[0]),
			new ChanceItem(Items.MAPLE_SEED_5314, 1, NPCDropTables.DROP_RATES[1]),
			new ChanceItem(Items.PINEAPPLE_SEED_5287, 1, NPCDropTables.DROP_RATES[1]),
			new ChanceItem(Items.PAPAYA_TREE_SEED_5288, 1, NPCDropTables.DROP_RATES[1]),
			new ChanceItem(Items.YEW_SEED_5315, 1, NPCDropTables.DROP_RATES[2]),
			new ChanceItem(Items.PALM_TREE_SEED_5289, 1, NPCDropTables.DROP_RATES[2]),
			new ChanceItem(Items.CALQUAT_TREE_SEED_5290, 1, NPCDropTables.DROP_RATES[2]),
			new ChanceItem(Items.SPIRIT_SEED_5317, 1, NPCDropTables.DROP_RATES[3]),
			new ChanceItem(Items.MAGIC_SEED_5316, 1, NPCDropTables.DROP_RATES[3])),
	RING(new ChanceItem(5074, 1, 30),
			new ChanceItem(Items.GOLD_RING_1635, 1, NPCDropTables.DROP_RATES[0]),
			new ChanceItem(Items.SAPPHIRE_RING_1637, 1, NPCDropTables.DROP_RATES[0]),
			new ChanceItem(Items.EMERALD_RING_1639, 1, NPCDropTables.DROP_RATES[0]),
			new ChanceItem(Items.RUBY_RING_1641, 1, NPCDropTables.DROP_RATES[0]),
			new ChanceItem(Items.DIAMOND_RING_1643, 1, NPCDropTables.DROP_RATES[2])),
	RED(new ChanceItem(5070, 1, 5), new ChanceItem(5076)),
	GREEN(new ChanceItem(5071, 1, 5), new ChanceItem(5078)),
	BLUE(new ChanceItem(5072, 1, 5), new ChanceItem(5077)),
	RAVEN(new ChanceItem(11966, 1, 5), new ChanceItem(11964)),

	WYSON(new ChanceItem(7413, 1, 1),
			new ChanceItem(Items.POTATO_SEED_5318, 14, NPCDropTables.DROP_RATES[0]),
			new ChanceItem(Items.SWEETCORN_SEED_5320, 3, NPCDropTables.DROP_RATES[0]),
			new ChanceItem(Items.TOMATO_SEED_5322, 6, NPCDropTables.DROP_RATES[0]),			
			new ChanceItem(Items.CABBAGE_SEED_5324, 9, NPCDropTables.DROP_RATES[0]),
			new ChanceItem(Items.LIMPWURT_SEED_5100, 2, NPCDropTables.DROP_RATES[0]),
			new ChanceItem(Items.ONION_SEED_5319, 11, NPCDropTables.DROP_RATES[0]),
			new ChanceItem(Items.STRAWBERRY_SEED_5323, 3, NPCDropTables.DROP_RATES[0]),
			new ChanceItem(Items.WATERMELON_SEED_5321, 2, NPCDropTables.DROP_RATES[0]),
			new ChanceItem(Items.ACORN_5312, 1, NPCDropTables.DROP_RATES[2]),
			new ChanceItem(Items.RANARR_SEED_5295, 1, NPCDropTables.DROP_RATES[2]),
			new ChanceItem(Items.WILLOW_SEED_5313, 1, NPCDropTables.DROP_RATES[2]),
			new ChanceItem(Items.MAPLE_SEED_5314, 1, NPCDropTables.DROP_RATES[2]),
			new ChanceItem(Items.YEW_SEED_5315, 1, NPCDropTables.DROP_RATES[2]),
			new ChanceItem(Items.MAGIC_SEED_5316, 1, NPCDropTables.DROP_RATES[3]),
			new ChanceItem(Items.SPIRIT_SEED_5317, 1, NPCDropTables.DROP_RATES[3]));

	/**
	 * The random nest items.
	 */
	private static final ChanceItem[] NESTS = new ChanceItem[6];

	/**
	 * The empty birds nest item.
	 */
	private static final Item EMPTY = new Item(5075);

	/**
	 * The birds nest item.
	 */
	private final ChanceItem nest;

	/**
	 * The loot item from the nest.
	 */
	private final ChanceItem[] loot;

	/**
	 * Constructs a new {@code BirdNest} {@code Object}.
	 * @param nest the nest.
	 * @param loot the loot.
	 */
	private BirdNest(final ChanceItem nest, final ChanceItem... loot) {
		this.nest = nest;
		this.loot = loot;
	}

	/**
	 * Drops a birds nest.
	 * @param player the player.
	 */
	public static void drop(final Player player) {
		final BirdNest nest = getRandomNest(false);
		player.getAudioManager().send(1997);
		GroundItemManager.create(nest.getNest(), player);
		player.getPacketDispatch().sendMessage("<col=FF0000>A bird's nest falls out of the tree.");
	}

	/**
	 * Searches a bird nest.
	 * @param player the player.
	 * @param item the item searched.
	 */
	public void search(final Player player, Item item) {
		if (player.getInventory().freeSlots() < 1) {
			player.getPacketDispatch().sendMessage("You don't have enough inventory space.");
			return;
		}
		final ChanceItem loot = ordinal() > 1 && this != WYSON ? getLoot()[0] : RandomFunction.getChanceItem(getLoot());
		final String name = loot.getName().toLowerCase();
		final String input = (StringUtils.isPlusN(name) ? "an" : "a") + " " + name;
		player.lock(1);
		player.getInventory().add(loot);
		player.getInventory().replace(EMPTY, item.getSlot());
		player.getPacketDispatch().sendMessage("You take " + input + " out of the bird's nest.");
	}

	/**
	 * Gets the random nest.
	 * @return the nest.
	 */
	public static BirdNest getRandomNest(boolean wyson) {
		ChanceItem item = RandomFunction.getChanceItem(NESTS);
		for (BirdNest n : BirdNest.values()) {
			if (n.getNest() == item) {
				if (wyson && n == SEED) {
					return WYSON;
				} else if (!wyson && n == WYSON) {
					return SEED;
				}
				return n;
			}
		}
		return null;
	}

	/**
	 * Gets the nest by the id.
	 * @param nest the nest.
	 * @return the nest.
	 */
	public static BirdNest forNest(final Item nest) {
		for (BirdNest n : values()) {
			if (n.getNest().getId() == nest.getId()) {
				return n;
			}
		}
		return null;
	}

	/**
	 * Gets the nest.
	 * @return The nest.
	 */
	public ChanceItem getNest() {
		return nest;
	}

	/**
	 * Gets the loot.
	 * @return The loot.
	 */
	public ChanceItem[] getLoot() {
		return loot;
	}

	/**
	 * static-block to add nests.
	 */
	static {
		for (int i = 0; i < NESTS.length; i++) {
			NESTS[i] = values()[i].getNest();
		}
	}

}
