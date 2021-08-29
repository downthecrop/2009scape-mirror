package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.content.global.action.ClimbActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used for managing ladders.
 * @author Emperor
 * @version 2.0
 */
@Initializable
public final class LadderManagingPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.setOptionHandler("climb-up", this);
		SceneryDefinition.setOptionHandler("climb-down", this);
		SceneryDefinition.setOptionHandler("climb", this);
		return this;
	}

	@Override
	public boolean handle(final Player player, Node node, final String option) {
		ClimbActionHandler.climbLadder(player, (Scenery) node, option);
		return true;
	}

	@Override
	public Location getDestination(Node n, Node object) {
		return ClimbActionHandler.getDestination((Scenery) object);
	}

}
