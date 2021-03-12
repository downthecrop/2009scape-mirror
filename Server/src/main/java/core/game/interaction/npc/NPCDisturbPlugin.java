package core.game.interaction.npc;

import core.cache.def.impl.NPCDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.IdleAbstractNPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the disturb option.
 * @author Emperor
 *
 */
@Initializable
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
