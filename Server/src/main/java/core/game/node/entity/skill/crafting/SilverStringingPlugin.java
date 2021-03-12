package core.game.node.entity.skill.crafting;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to string jewelry.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class SilverStringingPlugin extends UseWithHandler {

	/**
	 * Constructs a new {@code SilverStringingPlugin} {@code Object}.
	 */
	public SilverStringingPlugin() {
		super(1714, 1720);
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		SilverProduct silver = null;
		if (event.getUsedItem().getId() != 1759) {
			silver = SilverProduct.forProductId(event.getUsedItem().getId());
		} else if (((Item) event.getUsedWith()).getId() != 1759) {
			silver = SilverProduct.forProductId((((Item) event.getUsedWith()).getId()));
		}
		if (silver == null) {
			return true;
		}
		player.getInventory().remove(new Item(silver.getProduct(), 1));
		player.getInventory().remove(new Item(1759, 1));
		player.getInventory().add(new Item(silver.getStrung(), 1));
		return true;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(1759, ITEM_TYPE, this);
		return null;
	}

}
