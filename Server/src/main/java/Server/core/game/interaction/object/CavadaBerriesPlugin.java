package core.game.interaction.object;

import core.cache.def.impl.ItemDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to handle the cadava potion.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class CavadaBerriesPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(756).getHandlers().put("option:look-at", this);
		ItemDefinition.forId(756).getHandlers().put("option:drink", this);
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
