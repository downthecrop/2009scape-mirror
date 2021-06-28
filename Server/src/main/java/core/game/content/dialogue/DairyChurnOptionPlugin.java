package core.game.content.dialogue;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to handle dairy churning executing.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class DairyChurnOptionPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(10093).getHandlers().put("option:churn", this);
		SceneryDefinition.forId(10094).getHandlers().put("option:churn", this);
		SceneryDefinition.forId(25720).getHandlers().put("option:churn", this);
		SceneryDefinition.forId(34800).getHandlers().put("option:churn", this);
		SceneryDefinition.forId(35931).getHandlers().put("option:churn", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (!player.getInventory().contains(1927, 1) && !player.getInventory().contains(2130, 1) && !player.getInventory().contains(6697, 1)) {
			player.getPacketDispatch().sendMessage("You need some milk, cream or butter to use in the churn.");
			return true;
		}
		player.getDialogueInterpreter().open(984374);
		return true;
	}

}
