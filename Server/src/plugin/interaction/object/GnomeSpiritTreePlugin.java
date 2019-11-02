package plugin.interaction.object;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin to handle gnome tree teleporting.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class GnomeSpiritTreePlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(1317).getConfigurations().put("option:talk-to", this);
		ObjectDefinition.forId(1317).getConfigurations().put("option:teleport", this);
		ObjectDefinition.forId(1293).getConfigurations().put("option:talk-to", this);
		ObjectDefinition.forId(1293).getConfigurations().put("option:teleport", this);
		ObjectDefinition.forId(1294).getConfigurations().put("option:talk-to", this);
		ObjectDefinition.forId(1294).getConfigurations().put("option:teleport", this);
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
