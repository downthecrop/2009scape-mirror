package core.game.node.entity.skill.cooking;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the pie shell making plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class PieShellPlugin extends UseWithHandler {

	/**
	 * Represents the pie sell item.
	 */
	private static final Item PIE_SHELL = new Item(2315, 1);

	/**
	 * Represents the pie dish item.
	 */
	private static final Item PIE_DISH = new Item(2313, 1);

	/**
	 * Represents the pastry dough item.
	 */
	private static final Item PASTRY_DOUGH = new Item(1953, 1);

	/**
	 * Constructs a new {@code PieMakingPlugin} {@code Object}.
	 */
	public PieShellPlugin() {
		super(1953);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(2313, ITEM_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		if (player.getInventory().remove(PASTRY_DOUGH, PIE_DISH)) {
			player.getInventory().add(PIE_SHELL);
			player.getPacketDispatch().sendMessage("You put the pastry dough into the pie dish to make a pie shell.");
		}
		return true;
	}

}
