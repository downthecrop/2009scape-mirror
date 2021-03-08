package core.game.interaction.item;

import core.cache.def.impl.ItemDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to dust your hands.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class DustHandPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(8865).getHandlers().put("option:dust-hands", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (option) {
		case "dust-hands":
			if (player.getInventory().remove((Item) node)) {
				player.getPacketDispatch().sendMessage("You dust your hands with the finely ground ash.");
				player.setAttribute("hand_dust", true);
			}
			break;
		}
		return true;
	}

	@Override
	public boolean isWalk() {
		return false;
	}
}
