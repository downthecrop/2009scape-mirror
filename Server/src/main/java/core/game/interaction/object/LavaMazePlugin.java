package core.game.interaction.object;

import core.cache.def.impl.ObjectDefinition;
import core.game.content.global.action.ClimbActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.object.GameObject;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the lava maze.
 * @author Vexia
 */
@Initializable
public final class LavaMazePlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(1767).getHandlers().put("option:climb-down", this);
		ObjectDefinition.forId(1768).getHandlers().put("option:climb-up", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (node.getId()) {
		case 1767:
			if (node.getLocation().getX() == 3069) {
				ClimbActionHandler.climb(player, null, Location.create(3017, 10248, 0));
				return true;
			}
			ClimbActionHandler.climbLadder(player, (GameObject) node, option);
			return true;
		case 1768:
			if (node.getLocation().getX() == 3017) {
				ClimbActionHandler.climb(player, null, Location.create(3069, 3857, 0));
				return true;
			}
			ClimbActionHandler.climbLadder(player, (GameObject) node, option);
			return true;
		}
		return true;
	}

}
