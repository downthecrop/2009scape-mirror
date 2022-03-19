package core.game.interaction.player;

import core.cache.def.impl.NPCDefinition;
import core.game.interaction.Option;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the attack option plugin handler.
 * @author Emperor
 * @version 1.0
 */
@Initializable
public final class AttackOptionPlugin extends OptionHandler {

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.attack(node);
		return true;
	}

	@Override
	public boolean isWalk() {
		return false;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		Option._P_ATTACK.setHandler(this);
		return this;
	}

	@Override
	public boolean isDelayed(Player player) {
		return false;
	}

}
