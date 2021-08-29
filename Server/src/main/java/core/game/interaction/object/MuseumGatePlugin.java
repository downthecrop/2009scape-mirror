package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.content.global.action.DoorActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used for the museum gate plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class MuseumGatePlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(24536).getHandlers().put("option:open", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (player.getLocation().getY() >= 3447) {
			player.getDialogueInterpreter().open(5941);
		} else {
			DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
			return true;
		}
		return true;
	}
}
