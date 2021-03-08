package core.game.interaction.item;

import core.cache.def.impl.ItemDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the godsword dismantle plugin.
 * @author Emperor
 * @version 1.0
 */
@Initializable
public final class GodswordDismantlePlugin extends OptionHandler {

	/**
	 * Represents the godsword blade item.
	 */
	private static final Item BLADE = new Item(11690);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(11694).getHandlers().put("option:dismantle", this);
		ItemDefinition.forId(11696).getHandlers().put("option:dismantle", this);
		ItemDefinition.forId(11698).getHandlers().put("option:dismantle", this);
		ItemDefinition.forId(11700).getHandlers().put("option:dismantle", this);
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
