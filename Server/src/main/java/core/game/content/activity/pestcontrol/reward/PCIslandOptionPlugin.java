package core.game.content.activity.pestcontrol.reward;

import core.cache.def.impl.NPCDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;

/**
 * Represents the option plugin used to handle the pc island related nodes.
 * @author 'Vexia
 */
@Initializable
public final class PCIslandOptionPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (int id : new int[] { 3786, 3788, 3789, 5956 }) {
			NPCDefinition.forId(id).getHandlers().put("option:exchange", this);
		}
		ClassScanner.definePlugin(new PCRewardInterface());
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
