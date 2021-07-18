package core.game.interaction.city;

import core.cache.def.impl.SceneryDefinition;
import core.game.content.global.action.DoorActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles interactions in the khardian desert.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class KhardianDesertPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(6481).getHandlers().put("option:enter", this);
		SceneryDefinition.forId(6545).getHandlers().put("option:open", this);
		SceneryDefinition.forId(6547).getHandlers().put("option:open", this);
		SceneryDefinition.forId(6551).getHandlers().put("option:use", this);
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
			DoorActionHandler.handleAutowalkDoor(player, node.asScenery());
			break;
		case 6551:
			player.teleport(new Location(3233, 2887, 0));
			break;
		}
		return true;
	}

}
