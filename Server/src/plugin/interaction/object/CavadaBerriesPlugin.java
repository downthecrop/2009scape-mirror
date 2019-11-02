package plugin.interaction.object;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used to handle the cadava potion.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class CavadaBerriesPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(756).getConfigurations().put("option:look-at", this);
		ItemDefinition.forId(756).getConfigurations().put("option:drink", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (option.equals("drink")) {
			player.getDialogueInterpreter().sendItemMessage(756, "You dare not drink.");
		}
		if (option.equals("look-at")) {
			player.getDialogueInterpreter().sendItemMessage(756, "This looks very colourful.");
		}
		return true;
	}

	@Override
	public boolean isWalk() {
		return false;
	}
}
