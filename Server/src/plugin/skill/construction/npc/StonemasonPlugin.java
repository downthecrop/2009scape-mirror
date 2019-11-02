package plugin.skill.construction.npc;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.game.content.global.shop.Shop;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles the Keldagrim Stonemason.
 * @author Splinter
 */
@InitializablePlugin
public class StonemasonPlugin extends OptionHandler {
	
	/**
	 * The store that sells supplies.
	 */
	private static final SupplyStore STORE = new SupplyStore();

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.forId(4248).getConfigurations().put("option:trade", this);
		NPCDefinition.forId(4248).getConfigurations().put("option:talk-to", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (option) {
		case "trade":
		case "talk-to":
			STORE.open(player);
			break;
		}
		return true;
	}
	
	/**
	 * Stonemason's store.
	 * @author Splinter
	 */
	public static class SupplyStore extends Shop {

		/**
		 * Constructs a new {@Code SupplyStore} {@Code Object}
		 */
		public SupplyStore() {
			super("Keldagrim Stonemason", new Item[] { new Item(3420, 500_000), new Item(8786, 10_000), new Item(8784, 15_000), new Item(8788, 10_000) }, false);
		}

		@Override
		public boolean canSell(Player player, Item item, ItemDefinition def) {
			player.sendMessage("You cannot sell items to this store.");
			return false;
		}

		@Override
		public int getBuyPrice(Item item, Player player) {
			switch (item.getId()) {
			case 3420:
				return 26;
			case 8786:
				return 325_000;
			case 8784:
				return 130_000;
			case 8788:
				return 975_000;
			}
			return -1;
		}
	}
	
}