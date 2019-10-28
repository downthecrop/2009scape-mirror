package plugin.interaction.npc;

import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.game.content.skill.free.crafting.TanningProduct;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * @author 'Vexia
 */
@InitializablePlugin
public class TanningNPC extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.forId(1041).getConfigurations().put("option:trade", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		TanningProduct.open(player, ((NPC) node).getId());
		return true;
	}

}
