package plugin.interaction.item.withitem;

import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used to handle the creation of a crystal key.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class CrystalKeyCreatePlugin extends UseWithHandler {

	/**
	 * Represents the crystal key item.
	 */
	private static final Item KEY = new Item(989, 1);

	/**
	 * Constructs a new {@code CrystalKeyCreatePlugin} {@code Object}.
	 */
	public CrystalKeyCreatePlugin() {
		super(985);
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		if (player.getInventory().remove(event.getBaseItem(), event.getUsedItem())) {
			player.getInventory().add(KEY);
			player.getPacketDispatch().sendMessage("You join the loop half of a key and the tooth half of a key to make a crystal key.");
		}
		return true;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(987, ITEM_TYPE, this);
		return this;
	}

}
