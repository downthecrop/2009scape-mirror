package plugin.interaction.item.withitem;

import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Used to handle the reward of splitting granite with a chisel in to smaller
 * pieces.
 * @author Splinter
 */
@InitializablePlugin
public class GraniteSplittingPlugin extends UseWithHandler {

	public GraniteSplittingPlugin() {
		super(1755);
	}

	/**
	 * Represents the 5kg granite
	 */
	private static final Item FIVE_KG = new Item(6983, 1);

	/**
	 * Represents the 2kg granite
	 */
	private static final Item TWO_KG = new Item(6981, 1);

	@Override
	public boolean handle(NodeUsageEvent event) {
		if (event.getUsedItem().getId() == 6981) {
			if (event.getPlayer().getInventory().freeSlots() < 4) {
				event.getPlayer().getPacketDispatch().sendMessage("You need four inventory slots to split this.");
			} else {
				if (event.getPlayer().getInventory().remove(TWO_KG)) {
					event.getPlayer().getInventory().add(new Item(6979, 4));
					event.getPlayer().getPacketDispatch().sendMessage("You chisel the 2kg granite into four smaller pieces.");
				}
			}
		}
		if (event.getUsedItem().getId() == 6983) {
			if (event.getPlayer().getInventory().freeSlots() < 4) {
				event.getPlayer().getPacketDispatch().sendMessage("You need four inventory slots to split this.");
			} else {
				if (event.getPlayer().getInventory().remove(FIVE_KG)) {
					event.getPlayer().getInventory().add(new Item(6981, 2));
					event.getPlayer().getInventory().add(new Item(6979, 2));
					event.getPlayer().getPacketDispatch().sendMessage("You chisel the 5kg granite into four smaller pieces.");
				}
			}
		}
		return true;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(6981, ITEM_TYPE, this);
		addHandler(6983, ITEM_TYPE, this);
		return this;
	}

}
