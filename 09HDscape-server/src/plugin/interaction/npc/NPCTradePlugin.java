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
 * Represents the plugin used for an npc with the trade option.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class NPCTradePlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.setOptionHandler("trade", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final NPC npc = (NPC) node;
		if (npc.getId() == 2824) {
			TanningProduct.open(player, 2824);
			return true;
		}
		return node.asNpc().openShop(player);
	}

}
