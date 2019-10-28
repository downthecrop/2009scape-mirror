package plugin.zone.fremmenik;

import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.Plugin;

/**
 * Handles the fremmenik plugin.
 * @author Vexia
 */
public class FremmenikPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.forId(5508).getConfigurations().put("option:ferry-neitiznot", this);
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
