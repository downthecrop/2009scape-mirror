package plugin.skill.cooking;

import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used to create nettle tea in a cup.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class NettleTeaPlugin extends UseWithHandler {

	/**
	 * Represents the empty cup item.
	 */
	private static final Item EMPTY_CUP = new Item(1980, 1);

	/**
	 * Represents the nettle tea item.
	 */
	private static final Item NETTLE_TEA = new Item(4239, 1);

	/**
	 * Represents the bowl item.
	 */
	private static final Item BOWL = new Item(1923);

	/**
	 * Represents the cup of tea item.
	 */
	private static final Item CUP_OF_TEA = new Item(4242, 1);

	/**
	 * Constructs a new {@code NettleTeaPlugin} {@code Object}.
	 */
	public NettleTeaPlugin() {
		super(1980);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(4239, ITEM_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		if (player.getInventory().remove(EMPTY_CUP) && player.getInventory().remove(NETTLE_TEA)) {
			player.getInventory().add(BOWL);
			player.getInventory().add(CUP_OF_TEA);
		}
		return true;
	}

}
