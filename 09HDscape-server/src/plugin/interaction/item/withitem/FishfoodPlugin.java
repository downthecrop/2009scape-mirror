package plugin.interaction.item.withitem;

import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the poison fish food making plugin.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class FishfoodPlugin extends UseWithHandler {

	/**
	 * Represents the poisoned fish food item.
	 */
	private static final Item POISONED_FISH_FOOD = new Item(274);

	/**
	 * Constructs a new {@code FishfoodPlugin} {@code Object}.
	 */
	public FishfoodPlugin() {
		super(272, 274);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(273, ITEM_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		if (player.getInventory().remove(event.getBaseItem(), event.getUsedItem())) {
			player.getInventory().add(POISONED_FISH_FOOD);
			player.getPacketDispatch().sendMessage("You poison the fish food.");
		}
		return true;
	}

}
