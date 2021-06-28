package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to unlock the sheers cage.
 * @author 'Vexia
 * @versio 1.0
 */
@Initializable
public final class SeersCageUnlockPlugin extends OptionHandler {

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.getPacketDispatch().sendMessage("You can't unlock the pillory, you'll let all the prisoners out!");
		return true;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(6836).getHandlers().put("option:unlock", this);
		return this;
	}

}
