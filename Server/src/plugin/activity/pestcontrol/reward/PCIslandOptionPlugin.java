package plugin.activity.pestcontrol.reward;

import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.PluginManager;

/**
 * Represents the option plugin used to handle the pc island related nodes.
 * @author 'Vexia
 */
@InitializablePlugin
public final class PCIslandOptionPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (int id : new int[] { 3786, 3788, 3789, 5956 }) {
			NPCDefinition.forId(id).getConfigurations().put("option:exchange", this);
		}
		PluginManager.definePlugin(new PCRewardInterface());
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (option) {
		case "exchange":
			PCRewardInterface.open(player);
			break;
		}
		return true;
	}

}
