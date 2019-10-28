package plugin.interaction.npc;

import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the customs officer plugin.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class CustomsOfficerPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.forId(380).getConfigurations().put("option:pay-fare", this);
		NPCDefinition.forId(381).getConfigurations().put("option:pay-fare", this);
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
