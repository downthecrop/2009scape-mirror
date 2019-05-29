package plugin.interaction.item;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the godsword dismantle plugin.
 * @author Emperor
 * @version 1.0
 */
@InitializablePlugin
public final class GodswordDismantlePlugin extends OptionHandler {

	/**
	 * Represents the godsword blade item.
	 */
	private static final Item BLADE = new Item(11690);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(11694).getConfigurations().put("option:dismantle", this);
		ItemDefinition.forId(11696).getConfigurations().put("option:dismantle", this);
		ItemDefinition.forId(11698).getConfigurations().put("option:dismantle", this);
		ItemDefinition.forId(11700).getConfigurations().put("option:dismantle", this);
		return this;
	}

	@Override
	public boolean handle(final Player player, Node node, String option) {
		final Item item = (Item) node;
		if (item.getSlot() < 0 || player.getInventory().getNew(item.getSlot()).getId() != item.getId()) {
			return true;
		}
		final int freeSlot = player.getInventory().freeSlot();
		if (freeSlot == -1) {
			player.getPacketDispatch().sendMessage("Not enough space in your inventory!");
			return true;
		}
		player.getPacketDispatch().sendMessage("You detach the hilt from the blade.");
		player.getInventory().replace(null, item.getSlot(), false);
		player.getInventory().add(BLADE, new Item(11702 + (item.getId() - 11694)));
		return true;
	}

	@Override
	public boolean isWalk() {
		return false;
	}

}
