package core.game.content.zone.fremmenik;

import core.cache.def.impl.NPCDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Plugin;

/**
 * Handles the fremmenik plugin.
 * @author Vexia
 */
public class FremmenikPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.forId(5508).getHandlers().put("option:ferry-neitiznot", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (option) {
		case "ferry-neitiznot":
			return true;
		}
		return true;
	}

}
