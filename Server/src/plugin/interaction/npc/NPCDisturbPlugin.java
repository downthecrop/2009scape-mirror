package plugin.interaction.npc;

import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.npc.IdleAbstractNPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles the disturb option.
 * @author Emperor
 *
 */
@InitializablePlugin
public final class NPCDisturbPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.setOptionHandler("disturb", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (node instanceof IdleAbstractNPC) {
			IdleAbstractNPC npc = (IdleAbstractNPC) node;
			if (npc.canDisturb(player)) {
				npc.disturb(player);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isWalk(final Player player, final Node node) {
		if (node instanceof IdleAbstractNPC) {
			IdleAbstractNPC npc = (IdleAbstractNPC) node;
			return !npc.inDisturbingRange(player);
		}
		return false;
	}

	@Override
	public boolean isWalk() {
		return false;
	}

}
