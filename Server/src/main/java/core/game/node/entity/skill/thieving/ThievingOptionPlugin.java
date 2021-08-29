package core.game.node.entity.skill.thieving;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to handle thieving options.
 * @author 'Vexia
 * @date 22/10/2013
 */
@Initializable
public class ThievingOptionPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.setOptionHandler("steal-from", this);
		SceneryDefinition.setOptionHandler("steal from", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (option) {
		case "steal-from":
		case "steal from":
			player.getPulseManager().run(new StallThiefPulse(player, (Scenery) node, Stall.forObject((Scenery) node)));
			player.getLocks().lockInteractions(6);
			break;
		}
		return true;
	}

}
