package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.OptionHandler;
import core.game.interaction.UseWithHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * Represents the plugin used for the crystal chest.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class CrystalChestPlugin extends UseWithHandler {

	/**
	 * Represents the cyrstal key.
	 */
	private static final Item KEY = new Item(989);

	/**
	 * Constructs a new {@code CrystalChestPlugin} {@code Object}.
	 */
	public CrystalChestPlugin() {
		super(989);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(172, OBJECT_TYPE, this);
		ClassScanner.definePlugin(new OptionHandler() {

			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				SceneryDefinition.forId(172).getHandlers().put("option:open", this);
				return this;
			}

			@Override
			public boolean handle(Player player, Node node, String option) {
				unlock(player);
				return true;
			}

		});
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		unlock(event.getPlayer());
		return true;
	}

	/**
	 * Unlocks the chest.
	 * @param player the player,
	 * @return true if so.
	 */
	private boolean unlock(Player player) {
		if (!player.getInventory().contains(989, 1)) {
			player.getPacketDispatch().sendMessage("This chest is securely locked shut.");
			return true;
		}
		if (player.getInventory().freeSlots() == 0) {
			player.getPacketDispatch().sendMessage("Not enough inventory space.");
			return true;
		}
		if (player.getInventory().remove(KEY)) {
			Reward reward = Reward.getReward(player);
			for (Item i : reward.getItems()) {
				player.getInventory().add(i, player);
			}
			player.getPacketDispatch().sendMessage("You unlock the chest with your key.");
			player.getPacketDispatch().sendMessage("You find some teasure in the chest!");
		}
		return true;
	}

	/**
	 * Represents a crystal ches reward.
	 * @author 'Vexia
	 */
	public enum Reward {
		FIRST(39.69, new Item(1631, 1), new Item(1969, 1), new Item(995, 2000)), SECOND(16.72, new Item(1631, 1)), THIRD(10.57, new Item(1631, 1), new Item(371, 5), new Item(995, 1000)), FOURTH(7.73, new Item(1631, 1), new Item(556, 50), new Item(555, 50), new Item(557, 50), new Item(554, 50), new Item(559, 50), new Item(558, 50), new Item(565, 10), new Item(9075, 10), new Item(566, 10)), FIFTH(6.55, new Item(1631, 1), new Item(454, 100)), SIXTH(4.23, new Item(1631, 1), new Item(1603, 2), new Item(1601, 2)), SEVENTH(3.67, new Item(1631, 1), new Item(985, 1), new Item(995, 750)), EIGHT(3.51, new Item(1631, 1), new Item(2363, 3)), NINTH(3.26, new Item(1631, 1), new Item(987, 1), new Item(995, 750)), TENTH(2.75, new Item(1631, 1), new Item(441, 150)), ELEVENTH(1.06, new Item(1631, 1), new Item(1183, 1)), TWELFTH(0.26, new Item(1631, 1), new Item(1079, 1)), // FOR
		// MALES!
		TWELFTH_FEMALE(0.26, new Item(1631, 1), new Item(1093, 1)); // FOR
		// SHEMALES(Female)

		/**
		 * Represents the item rewards.
		 */
		private final Item[] items;

		/**
		 * Represents the chance of getting the item.
		 */
		private final double chance;

		/**
		 * Constructs a new {@code CrystalChestPlugin} {@code Object}.
		 * @param chance the chance.
		 * @param items the item.
		 */
		Reward(double chance, Item... items) {
			this.chance = chance;
			this.items = items;
		}

		/**
		 * Gets the reward.
		 * @param player the player.
		 * @return the reward.
		 */
		public static Reward getReward(final Player player) {
			int totalChance = 0;
			for (Reward r : values()) {
				if (r == Reward.TWELFTH_FEMALE && !player.getAppearance().isMale()) {
					continue;
				}
				totalChance += r.getChance();
			}
			final int random = RandomFunction.random(totalChance);
			int total = 0;
			for (Reward r : values()) {
				total += r.getChance();
				if (random < total) {
					return r;
				}
			}
			return null;
		}

		/**
		 * Gets the items.
		 * @return The items.
		 */
		public Item[] getItems() {
			return items;
		}

		/**
		 * Gets the chance.
		 * @return The chance.
		 */
		public double getChance() {
			return chance;
		}

	}

}
