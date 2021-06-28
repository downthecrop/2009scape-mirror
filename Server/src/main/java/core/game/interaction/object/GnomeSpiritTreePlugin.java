package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin to handle gnome tree teleporting.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class GnomeSpiritTreePlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(1317).getHandlers().put("option:talk-to", this);
		SceneryDefinition.forId(1317).getHandlers().put("option:teleport", this);
		SceneryDefinition.forId(1293).getHandlers().put("option:talk-to", this);
		SceneryDefinition.forId(1293).getHandlers().put("option:teleport", this);
		SceneryDefinition.forId(1294).getHandlers().put("option:talk-to", this);
		SceneryDefinition.forId(1294).getHandlers().put("option:teleport", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (option) {
		case "talk-to":
			player.getDialogueInterpreter().open(1317);
			return true;
		case "teleport":
			player.getDialogueInterpreter().open(1317);
			return true;
		}
		return true;
	}

}
