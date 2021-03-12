package core.game.content.zone.rellekka;

import core.cache.def.impl.ObjectDefinition;
import core.game.content.global.action.DoorActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the light house plugin.
 * @author Vexia
 */
@Initializable
public class LightHousePlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(4577).getHandlers().put("option:walk-through", this);
		ObjectDefinition.forId(4383).getHandlers().put("option:climb", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (node.getId()) {
		case 4577:
			DoorActionHandler.handleDoor(player, node.asObject());
			return true;
		case 4383:
			return false;
		}
		return true;
	}

	@Override
	public Location getDestination(Node node, Node n) {
		if (n.getName().equals("Door")) {
			return DoorActionHandler.getDestination((Entity) node, n.asObject());
		}
		return null;
	}
}
