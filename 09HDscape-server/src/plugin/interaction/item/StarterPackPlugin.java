package plugin.interaction.item;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * The plugin used for the free to play starter pack item
 * @author Splinter
 * 
 */
@InitializablePlugin
public final class StarterPackPlugin extends OptionHandler {
	
	/**
	 * The item from the starter pack.
	 */
	private static final Item[] ITEMS = { new Item(558, 50), new Item(556, 50), new Item(1379, 1), new Item(740, 1), new Item(882, 100), new Item(1167, 1), new Item(1129, 1), new Item(1095, 1), new Item(1063, 1), new Item(1363, 1), new Item(1323, 1), new Item(1267, 1), new Item(1349, 1), new Item(1153, 1), new Item(1101, 1), new Item(1067, 1), new Item(1175, 1), new Item(841, 1)};
	

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(14775).getConfigurations().put("option:open", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (option) {
		case "open":
			if(player.getInventory().freeSlots() >= ITEMS.length){
				if (player.getInventory().remove((Item) node)) {
					player.getInventory().add(ITEMS);
					return true;
				}
			} else {
				player.sendMessage("You need "+(ITEMS.length - player.getInventory().freeSlots())+" more free inventory slot(s) to open the box.");
				return true;
			}
			break;
		}
		return true;
	}

	@Override
	public boolean isWalk() {
		return false;
	}
}
