package core.game.interaction.npc;

import core.cache.def.impl.NPCDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used for the npcs in alkharid which can heal you.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class AlkharidHealPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.forId(962).getHandlers().put("option:heal", this);
		NPCDefinition.forId(961).getHandlers().put("option:heal", this);
		NPCDefinition.forId(960).getHandlers().put("option:heal", this);
		NPCDefinition.forId(959).getHandlers().put("option:heal", this);
		NPCDefinition.forId(958).getHandlers().put("option:heal", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.getDialogueInterpreter().open(((NPC) node).getId(), ((NPC) node));
		return true;
	}

}
