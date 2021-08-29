package core.game.interaction.city;

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
 * Handles the burthorpe plugin.
 * @author Emperor
 */
@Initializable
public final class BurthorpePlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(4627).getHandlers().put("option:climb-up", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		Scenery object = (Scenery) node;
		switch (object.getId()) {
		case 4627:
			ClimbActionHandler.climb(player, null, Location.create(2899, 3565, 0));
			return true;
		}
		return false;
	}

}
