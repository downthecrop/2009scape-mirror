package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the search option.
 * @author 'Vexia
 */
@Initializable
public class SearchOptionPlugin extends OptionHandler {

	/**
	 * Represents an object to search.
	 * @author 'Vexia
	 */
	public enum Search {
		/**
		 * Represents a search.
		 */
		DEFAULT(-1, new Item(1059, 1));

		public static Search forId(int id) {
			for (Search search : Search.values()) {
				if (search.getObject() == id) {
					return search;
				}
			}
			return null;
		}

		/**
		 * The object id.
		 */
		private int object;

		/**
		 * The item rewarded.
		 */
		private Item item;

		/**
		 * Constructs a new {@code SearchOptionPlugin.java}
		 */
		Search(int object, Item item) {
			this.object = object;
			this.item = item;
		}

		/**
		 * @return the item.
		 */
		public Item getItem() {
			return item;
		}

		/**
		 * @return the object.
		 */
		public int getObject() {
			return object;
		}
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (node.getName().equals("Bookcase")) {
			player.getPacketDispatch().sendMessage("You search the books...");
			player.getPacketDispatch().sendMessage("You find nothing of interest to you.");
			return true;
		}
		if (node.getId() == 14743 && !player.getInventory().containItems(946) && !player.inCombat()) {
			player.getPacketDispatch().sendMessage("You mindlessly reach into the sack labeled 'knives'...");
			player.getPacketDispatch().sendMessage("Against all odds you pull out a knife without hurting yourself.",2);
			player.getInventory().add(new Item(946));
			return true;
		}
		player.getPacketDispatch().sendMessage("You search the " + node.getName().toLowerCase() + " but find nothing.");
		return true;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.setOptionHandler("search", this);
		return this;
	}
}
