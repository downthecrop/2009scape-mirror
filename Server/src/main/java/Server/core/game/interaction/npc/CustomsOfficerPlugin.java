package core.game.interaction.npc;

import core.cache.def.impl.NPCDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the customs officer plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class CustomsOfficerPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.forId(380).getHandlers().put("option:pay-fare", this);
		NPCDefinition.forId(381).getHandlers().put("option:pay-fare", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (!player.getQuestRepository().isComplete("Pirate's Treasure")) {
			player.getDialogueInterpreter().open(((NPC) node).getId(), ((NPC) node));
			player.getPacketDispatch().sendMessage("You may only use the Pay-fare option after completing Pirate's Treasure.");
			return true;
		}
		player.getDialogueInterpreter().open(((NPC) node).getId(), ((NPC) node), true);
		return true;
	}

}
