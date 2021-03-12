package core.game.interaction.item.withitem;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the karamjan silk plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class KaramjanSilkPlugin extends UseWithHandler {

	/**
	 * Represents the cloth item.
	 */
	private static final Item CLOTH = new Item(3188);

	/**
	 * Represents the silk item.
	 */
	private static final Item SILK = new Item(950);

	/**
	 * Constructs a new {@code KaramjanSilkPlugin} {@code Object}.
	 */
	public KaramjanSilkPlugin() {
		super(950);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(431, ITEM_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		if (player.getInventory().remove(SILK)) {
			player.getInventory().add(CLOTH);
			player.getPacketDispatch().sendMessage("You pour some of the Karamjan rum over the silk.");
		}
		return true;
	}

}
