package core.game.interaction.city;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to handle nodes related to camelot.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class CamelotNodePlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(26017).getHandlers().put("option:climb-down", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final int id = ((Scenery) node).getId();
		switch (id) {
		case 26017:
			player.getPacketDispatch().sendMessage("Court is not in session.");
			break;
		}
		return true;
	}

	@Override
	public Location getDestination(Node node, Node n) {
		if (n instanceof Scenery) {
			int id = ((Scenery) n).getId();
			switch (id) {
			case 993:
				if (node.getLocation().getX() <= 2638) {
					return Location.create(2637, 3350, 0);
				} else {
					return Location.create(2640, 3350, 0);
				}
			}
		}
		return null;
	}

}
