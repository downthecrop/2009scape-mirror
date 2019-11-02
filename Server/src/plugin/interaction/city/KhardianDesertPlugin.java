package plugin.interaction.city;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.global.action.DoorActionHandler;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles interactions in the khardian desert.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class KhardianDesertPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(6481).getConfigurations().put("option:enter", this);
		ObjectDefinition.forId(6545).getConfigurations().put("option:open", this);
		ObjectDefinition.forId(6547).getConfigurations().put("option:open", this);
		ObjectDefinition.forId(6551).getConfigurations().put("option:use", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (node.getId()) {
		case 6481:
			player.teleport(new Location(3233, 9313, 0));
			break;
		case 6545:
		case 6547:
			// player.getPacketDispatch().sendMessage("A mystical power has sealed this door...");
			DoorActionHandler.handleAutowalkDoor(player, node.asObject());
			break;
		case 6551:
			player.teleport(new Location(3233, 2887, 0));
			break;
		}
		return true;
	}

}
