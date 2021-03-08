package core.game.node.entity.skill.cooking;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to make a cake.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class CakeMakingPlugin extends UseWithHandler {

	/**
	 * Represents the bucket of milk item.
	 */
	private static final Item BUCKET_OF_MILK = new Item(1927);

	/**
	 * Represents the egg item.
	 */
	private static final Item EGG = new Item(1944);

	/**
	 * Represents the cake tin item.
	 */
	private static final Item CAKE_TIN = new Item(1887);

	/**
	 * Represents the pot of flour item.
	 */
	private static final Item POT_OF_FLOUR = new Item(1933);

	/**
	 * Represents the uncooked cake item.
	 */
	private static final Item UNCOOKED_CAKE = new Item(1889);

	/**
	 * Constructs a new {@code CakeMakingPlugin} {@code Object}.
	 */
	public CakeMakingPlugin() {
		super(1933);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(1887, ITEM_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		if (event.getUsedItem().getId() == 1887 && ((Item) event.getUsedWith()).getId() == 1933 || event.getUsedWith().getName().equalsIgnoreCase("cake tin") && ((Item) event.getUsedItem()).getName().equalsIgnoreCase("pot of flour")) {
			if (event.getPlayer().getInventory().contains(1933, 1) && event.getPlayer().getInventory().contains(1927, 1) && event.getPlayer().getInventory().contains(1944, 1)) {
				if (event.getPlayer().getInventory().remove(BUCKET_OF_MILK, EGG, CAKE_TIN, POT_OF_FLOUR)) {
					event.getPlayer().getInventory().add(UNCOOKED_CAKE);
					event.getPlayer().getPacketDispatch().sendMessage("You mix the milk, flour and egg together to make a raw cake mix.");
					return true;
				}
			}
		}
		return false;
	}

}
