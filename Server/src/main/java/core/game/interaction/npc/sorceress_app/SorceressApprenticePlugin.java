package core.game.interaction.npc.sorceress_app;

import core.cache.def.impl.NPCDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;

/**
 * Teleport option for Sorceress Apprentice
 * @author SonicForce41
 */
@Initializable
public class SorceressApprenticePlugin extends OptionHandler {

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (option) {
		case "teleport":
			if (player.getSavedData().getGlobalData().hasSpokenToApprentice()) {
				NPC npc = (NPC) node;
				SorceressApprenticeDialogue.teleport(npc, player);
			} else {
				player.getDialogueInterpreter().sendDialogues(((NPC) node), null, "I can't do that now, I'm far too busy sweeping.");
			}
		}
		return true;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.forId(5532).getHandlers().put("option:teleport", this);
		SceneryDefinition.forId(21781).getHandlers().put("option:climb-up", this);
		ClassScanner.definePlugin(new SorceressApprenticeDialogue());
		return this;
	}

}
