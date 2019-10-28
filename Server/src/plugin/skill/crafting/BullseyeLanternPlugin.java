package plugin.skill.crafting;

import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles the bullseye lantern crafting.
 * @author Vexia
 *
 */
@InitializablePlugin
public class BullseyeLanternPlugin extends UseWithHandler {
	
	/**
	 * Constructs the {@code BullseyeLanternPlugin}
	 */
	public BullseyeLanternPlugin() {
		super(4542, 1607);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(4544, ITEM_TYPE, this);
		return this;
	}
	
	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		final Item with = event.getUsedWith().asItem();
		int rewardId = 4546;
		if (with.getId() == 1607) {
			rewardId = 4700;
		}
		if (player.getInventory().remove(event.getUsedItem())) {
			player.getInventory().replace(new Item(rewardId), event.getUsedWith().asItem().getSlot());
		}
		return true;
	}

}
