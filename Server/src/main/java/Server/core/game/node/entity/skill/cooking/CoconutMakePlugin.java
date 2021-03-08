package core.game.node.entity.skill.cooking;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to make a coconut.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class CoconutMakePlugin extends UseWithHandler {

	/**
	 * Represents the items related to coconut making plugin.
	 */
	private static final Item[] ITEMS = new Item[] { new Item(5974, 1), new Item(5976, 1), new Item(229), new Item(5935, 1), new Item(5978) };

	/**
	 * Constructs a new {@code CoconutMakePlugin} {@code Object}.
	 */
	public CoconutMakePlugin() {
		super(5974, 5976);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(2347, ITEM_TYPE, this);
		addHandler(229, ITEM_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		final Item usedWith = (Item) event.getUsedWith();
		if (usedWith.getId() == 5974 && event.getUsedItem().getId() == 2347 || usedWith.getId() == 2347 && event.getUsedItem().getId() == 5974) {
			player.getInventory().remove(ITEMS[0]);
			player.getInventory().add(ITEMS[1]);
			player.getPacketDispatch().sendMessage("You crush the coconut with a hammer.");
		}
		if (usedWith.getId() == 5976 && event.getUsedItem().getId() == 229 || usedWith.getId() == 229 && event.getUsedItem().getId() == 5976) {
			player.getInventory().remove(ITEMS[1]);
			player.getInventory().remove(ITEMS[2]);
			player.getInventory().add(ITEMS[3]);
			player.getInventory().add(ITEMS[4]);
			player.getPacketDispatch().sendMessage("You overturn the coconut into a vial.");
		}
		return true;
	}

}
