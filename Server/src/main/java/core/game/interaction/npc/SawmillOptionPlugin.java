package core.game.interaction.npc;

import core.cache.def.impl.NPCDefinition;
import core.game.component.Component;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin to handle the options for saw mill man.
 * @author 'Vexia
 * @date Oct 8, 2013
 */
@Initializable
public class SawmillOptionPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.forId(4250).getHandlers().put("option:buy-plank", this);
		NPCDefinition.forId(4250).getHandlers().put("option:talk-to", this);
		NPCDefinition.forId(4250).getHandlers().put("option:trade", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (option) {
		case "buy-plank":
			player.getInterfaceManager().open(new Component(403));
			return true;
		case "talk-to":
			player.getDialogueInterpreter().open(4250, node);
			return true;
		case "trade":
			node.asNpc().openShop(player);
			return true;
		}
		return true;
	}

	@Override
	public Location getDestination(Node node, Node n) {
		return Location.create(3302, 3491, 0);
	}
}
