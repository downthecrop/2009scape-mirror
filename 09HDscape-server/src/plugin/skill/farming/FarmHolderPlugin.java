package plugin.skill.farming;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.content.skill.member.farming.FarmingItemHolder;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;
import org.crandor.tools.StringUtils;

/**
 * Represents the plugin used to handle the managing of sacks and baskets.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class FarmHolderPlugin implements Plugin<Object> {

	/**
	 * Represents the empty sack item.
	 */
	private static final Item SACK = new Item(5418);

	/**
	 * Represents the empty basket item.
	 */
	private static final Item BASKET = new Item(5376);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		new ItemHolderPlugin().newInstance(arg);
		new HaySackPlugin().newInstance(arg);
		return this;
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return true;
	}

	/**
	 * Represents the plugin used to fill an empty sack with hay.
	 * @author 'Vexia
	 * @version 1.0
	 */
	public static final class HaySackPlugin extends UseWithHandler {

		/**
		 * Represents the hay bale items.
		 */
		private static final int[] BALES = new int[] { 304, 8713, 8715, 36892, 36893 };

		/**
		 * Represents the hay sack item.
		 */
		private static final Item HAY_SACK = new Item(6057);

		/**
		 * Constructs a new {@code HaySackPlugin} {@code Object}.
		 */
		public HaySackPlugin() {
			super(SACK.getId());
		}

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			for (int id : BALES) {
				addHandler(id, OBJECT_TYPE, this);
			}
			return this;
		}

		@Override
		public boolean handle(NodeUsageEvent event) {
			final Player player = event.getPlayer();
			if (player.getInventory().remove(event.getUsedItem())) {
				player.getInventory().add(HAY_SACK);
				player.getPacketDispatch().sendMessage("You fill the sack with straw.");
			}
			return true;
		}

	}

	/**
	 * Represents the plugin used to handle the item holders.
	 * @author 'Vexia
	 * @version 1.0
	 */
	public static final class ItemHolderPlugin extends OptionHandler {

		/**
		 * Represents the options to use.
		 */
		private static final String[] OPTIONS = new String[] { "fill", "empty", "remove-one" };

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			for (int i = 0; i < 2; i++) {
				ItemDefinition.forId(SACK.getId()).getConfigurations().put("option:" + OPTIONS[i], this);
				ItemDefinition.forId(BASKET.getId()).getConfigurations().put("option:" + OPTIONS[i], this);
			}
			for (FarmingItemHolder holder : FarmingItemHolder.values()) {
				for (Item item : holder.getHolders()) {
					for (String option : OPTIONS) {
						ItemDefinition.forId(item.getId()).getConfigurations().put("option:" + option, this);
					}
				}
			}
			new ItemHolderHandler().newInstance(arg);
			return this;
		}

		@Override
		public boolean handle(Player player, Node node, String option) {
			final Item item = (Item) node;
			final FarmingItemHolder holder = FarmingItemHolder.forHolder(item);
			switch (option) {
			case "fill":
				final boolean sack = holder != null && holder.ordinal() < 3 ? true : (item.getId() == SACK.getId() ? true : false);
				final Item base = getRemoveItem(player, item, sack);
				if (base == null) {
					player.getPacketDispatch().sendMessage(sack ? "You don't have any potatoes, onions, or cabbages." : "You don't have any fruit to fill the basket with.");
					return true;
				}
				final Item remove = new Item(base.getId(), player.getInventory().getAmount(base));
				final int defaultMax = (sack ? 10 : 5);
				if (holder == null) {
					fill(player, item, remove, FarmingItemHolder.forItem(remove), defaultMax);
					return true;
				}
				if (holder.getAmount(item) == defaultMax) {
					player.getPacketDispatch().sendMessage("The " + (sack ? "sack" : "fruit basket") + " is full.");
					return true;
				}
				if (holder.getItem().getId() != base.getId()) {
					player.sendMessage("You can't put " + base.getName().toLowerCase() + "s in there. You've already got " + holder.getItem().getName().toLowerCase() + "s in the sack.");
					return true;
				}
				final int initialAmount = holder.getAmount(item);
				fill(player, item, remove, holder, defaultMax - initialAmount);
				break;
			case "empty":
			case "remove-one":
				remove(player, item, holder, option.equals("empty") ? holder.getAmount(item) : 1);
				break;
			}
			return true;
		}

		/**
		 * Method used to fill a basket or sack.
		 * @param player the player.
		 * @param item the item.
		 * @param remove the remove.
		 * @param holder the holder.
		 * @param max the max.
		 */
		private void fill(final Player player, final Item item, final Item remove, final FarmingItemHolder holder, final int max) {
			if (remove.getAmount() > max) {
				remove.setAmount(max);
			}
			if (player.getInventory().remove(remove)) {
				final int old = item.getId() == BASKET.getId() || item.getId() == SACK.getId() ? 0 : holder.getAmount(item);
				player.getInventory().replace(holder.getItem(old + remove.getAmount()), item.getSlot());
			}
		}

		/**
		 * Method used to remove an item.
		 * @param player the player.
		 * @param item the item.
		 * @param holder the holder.
		 * @param amount the amount.
		 */
		private void remove(final Player player, final Item item, final FarmingItemHolder holder, final int amount) {
			final Item remove = new Item(holder.getItem().getId(), amount);
			if (!player.getInventory().hasSpaceFor(remove)) {
				player.getPacketDispatch().sendMessage("You don't have enough inventory space.");
				return;
			}
			if (player.getInventory().add(remove)) {
				player.getInventory().replace(holder.getItem(holder.getAmount(item) - remove.getAmount()), item.getSlot());
				player.getPacketDispatch().sendMessage("You take " + (remove.getAmount() < 2 ? (StringUtils.isPlusN(remove.getName().toLowerCase()) ? "an" : "a") : "" + remove.getAmount()) + " " + remove.getName().toLowerCase() + (remove.getAmount() > 1 ? "s" : "") + " out of the " + (holder.ordinal() < 3 ? "sack" : "basket") + ".");
			}
		}

		/**
		 * Gets the item to remove from the inventory.
		 * @param player the player.
		 * @param item the item.
		 * @param sack if a sack.
		 * @return {@code Item} or {@code Null}.
		 */
		private Item getRemoveItem(final Player player, final Item item, final boolean sack) {
			final int start = sack ? 0 : 3;
			final int end = sack ? 3 : FarmingItemHolder.values().length;
			for (int i = start; i < end; i++) {
				if (player.getInventory().containsItem(FarmingItemHolder.values()[i].getItem())) {
					return FarmingItemHolder.values()[i].getItem();
				}
			}
			return null;
		}

		@Override
		public boolean isWalk() {
			return false;
		}

		/**
		 * Represents the use with handler for sacks & baskets.
		 * @author 'Vexia
		 * @version 1.0
		 */
		public final class ItemHolderHandler extends UseWithHandler {

			/**
			 * Constructs a new {@code ItemHolderHandler} {@code Object}.
			 */
			public ItemHolderHandler() {
				super(BASKET.getId(), SACK.getId(), 5460, 5462, 5464, 5466, 5468, 5470, 5472, 5474, 5476, 5478, 5440, 5442, 5444, 5446, 5448, 5450, 5452, 5454, 5456, 5458, 5420, 5422, 5424, 5426, 5428, 5430, 5432, 5434, 5436, 5438, 5378, 5380, 5382, 5384, 5386, 5408, 5410, 5412, 5414, 5416, 5388, 5390, 5392, 5394, 5396, 5398, 5400, 5402, 5404, 5406, 5960, 5962, 5964, 5966, 5968);
			}

			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				for (FarmingItemHolder holder : FarmingItemHolder.values()) {
					addHandler(holder.getItem().getId(), ITEM_TYPE, this);
				}
				return this;
			}

			@Override
			public boolean handle(NodeUsageEvent event) {
				return false;
			}

		}
	}
}
