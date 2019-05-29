package plugin.interaction.object;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.global.action.DoorActionHandler;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used for the door near wydin.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class WydinDoorPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(2069).getConfigurations().put("option:open", this);
		return null;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (!player.getEquipment().contains(1005, 1) && player.getLocation().getX() >= 3011 && player.getLocation().getX() <= 3018) {
			player.getDialogueInterpreter().open(557, true, true);
			return true;
		} else {
			final GameObject object = (GameObject) node;
			DoorActionHandler.handleAutowalkDoor(player, object);
		}
		return true;
	}

	@Override
	public Location getDestination(Node node, Node n) {
		return DoorActionHandler.getDestination(((Entity) node), ((GameObject) n));
	}
}
