package plugin.interaction.item;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.content.global.action.DigSpadeHandler;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used to handle the dig option on a spade.
 * @author 'Vexia
 * @author Emperor
 */
@InitializablePlugin
public class SpadeDigOptionPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(952).getConfigurations().put("option:dig", this);
		return null;
	}

	@Override
	public boolean handle(final Player player, Node node, String option) {
		if (!DigSpadeHandler.dig(player)) {
			player.sendMessage("You dig but find nothing.");
		}
		return true;
	}

	@Override
	public boolean isWalk() {
		return false;
	}

}
