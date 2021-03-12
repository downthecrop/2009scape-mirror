package core.game.content.quest.free.therestlessghost;

import core.cache.def.impl.ItemDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/*
 * 8
 */
@Initializable
public class SkullDropPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(964).getHandlers().put("option:drop", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.getPacketDispatch().sendMessage("You can't drop this! Return it to the ghost.");
		return true;
	}

	@Override
	public boolean isWalk() {
		return false;
	}
}
