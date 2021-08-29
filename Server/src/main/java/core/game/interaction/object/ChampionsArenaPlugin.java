package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the champions arena plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class ChampionsArenaPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(10556).getHandlers().put("option:open", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		int id = node instanceof Scenery ? ((Scenery) node).getId() : ((NPC) node).getId();
		switch (id) {
		case 10556:
			player.getDialogueInterpreter().open(3050, true, true);
			break;
		}
		return true;
	}

}
