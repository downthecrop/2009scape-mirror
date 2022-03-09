package core.game.interaction.item;

import core.cache.def.impl.ItemDefinition;
import core.game.content.global.action.DigSpadeHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to handle the dig option on a spade.
 * @author 'Vexia
 * @author Emperor
 */
@Initializable
public class SpadeDigOptionPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(952).getHandlers().put("option:dig", this);
		return null;
	}

	@Override
	public boolean handle(final Player player, Node node, String option) {
		player.getAudioManager().send(1470,1,1);
		if (!DigSpadeHandler.dig(player)) {
			player.sendMessage("You dig but find nothing.");
		}
		return true;
	}

	@Override
	public boolean isWalk() {
		return false;
	}

}
