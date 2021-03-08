package core.game.interaction.npc;

import core.cache.def.impl.NPCDefinition;
import core.plugin.Initializable;
import core.game.node.entity.skill.crafting.TanningProduct;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Plugin;

/**
 * @author 'Vexia
 */
@Initializable
public class TanningNPC extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.forId(1041).getHandlers().put("option:trade", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		TanningProduct.open(player, ((NPC) node).getId());
		return true;
	}

}
