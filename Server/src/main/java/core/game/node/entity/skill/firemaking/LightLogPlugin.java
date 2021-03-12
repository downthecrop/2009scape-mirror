package core.game.node.entity.skill.firemaking;

import core.cache.def.impl.ItemDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.GroundItem;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to light a log.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class LightLogPlugin extends OptionHandler {

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.getPulseManager().run(new FireMakingPulse(player, ((Item) node), ((GroundItem) node)));
		return true;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.setOptionHandler("light", this);
		return this;
	}

}
