package plugin.interaction.object;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.global.action.DoorActionHandler;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.object.GameObject;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used for the museum gate plugin.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class MuseumGatePlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(24536).getConfigurations().put("option:open", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (player.getLocation().getY() >= 3447) {
			player.getDialogueInterpreter().open(5941);
		} else {
			DoorActionHandler.handleAutowalkDoor(player, (GameObject) node);
			return true;
		}
		return true;
	}
}
