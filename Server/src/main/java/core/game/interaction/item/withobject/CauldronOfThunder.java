package core.game.interaction.item.withobject;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin handler used for with the cauldron of thunder.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class CauldronOfThunder extends UseWithHandler {

	/**
	 * Represents the animation to use.
	 */
	private static final Animation ANIMATION = new Animation(833);

	/**
	 * Represents the enchanted items.
	 */
	private static final Item[] ITEMS = new Item[] { new Item(524, 1), new Item(522, 1), new Item(523, 1), new Item(525, 1) };

	/**
	 * Constructs a new {@code CauldronOfThunder} {@code Object}.
	 */
	public CauldronOfThunder() {
		super(2136, 2132, 2134, 2138);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(2142, OBJECT_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		event.getPlayer().animate(ANIMATION);
		event.getPlayer().getInventory().remove(event.getUsedItem());
		if (event.getUsedItem().getId() == 2136) {
			event.getPlayer().getInventory().add(ITEMS[0]);
		} else if (event.getUsedItem().getId() == 2132) {
			event.getPlayer().getInventory().add(ITEMS[1]);
		} else if (event.getUsedItem().getId() == 2134) {
			event.getPlayer().getInventory().add(ITEMS[2]);
		} else if (event.getUsedItem().getId() == 2138) {
			event.getPlayer().getInventory().add(ITEMS[3]);
		}
		event.getPlayer().getPacketDispatch().sendMessage("You dip the " + event.getUsedItem().getName().toLowerCase() + " in the cauldron.");
		return true;
	}

}
