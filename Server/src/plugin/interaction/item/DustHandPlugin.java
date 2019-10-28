package plugin.interaction.item;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used to dust your hands.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class DustHandPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(8865).getConfigurations().put("option:dust-hands", this);
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
