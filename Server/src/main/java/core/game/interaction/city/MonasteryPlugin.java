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
 * Handles the edgeville monastery.
 * @author Vexia
 */
@Initializable
public final class MonasteryPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(2641).getHandlers().put("option:climb-up", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (node.getId()) {
		case 2641:
			final boolean abbot = node.getLocation().equals(new Location(3057, 3483, 0));
			if (!player.getSavedData().getGlobalData().isJoinedMonastery()) {
				player.getDialogueInterpreter().open(abbot ? 801 : 7727, true);
			} else {
				ClimbActionHandler.climbLadder(player, (Scenery) node, option);
			}
			break;
		}
		return true;
	}

}
