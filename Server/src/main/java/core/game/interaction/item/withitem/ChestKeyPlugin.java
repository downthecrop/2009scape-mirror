package core.game.interaction.item.withitem;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to handle the usage of a chest key on the chest.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class ChestKeyPlugin extends UseWithHandler {

	/**
	 * Represents the chest key item.
	 */
	private static final Item CHEST_KEY = new Item(432);

	/**
	 * Represents the pirate message item.
	 */
	private static final Item PIRATE_MESSAGE = new Item(433);

	/**
	 * Constructs a new {@code ChestKeyPlugin} {@code Object}.
	 */
	public ChestKeyPlugin() {
		super(432);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(2079, OBJECT_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		if (player.getInventory().remove(CHEST_KEY)) {
			SceneryBuilder.replace((Scenery) event.getUsedWith(), ((Scenery) event.getUsedWith()).transform(2080), 3);
			player.getInventory().add(PIRATE_MESSAGE);
			player.getPacketDispatch().sendMessage("You unlock the chest.");
			player.getPacketDispatch().sendMessage("All that's in the chest is a message...");
			player.getPacketDispatch().sendMessage("You take the message from the chest.");
		}
		return true;
	}

}
