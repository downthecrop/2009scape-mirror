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
		//Makes sure player uses correct attack styles for lumbridge dummies
		if (node.getId() == 4474 && !(player.getSwingHandler(false).getType() == CombatStyle.MAGIC)){ player.sendMessage("You can only attack this with magic."); return true; }
		if (node.getId() == 7891 && !(player.getSwingHandler(false).getType() == CombatStyle.MELEE)){ player.sendMessage("You must use the training sword to attack this."); return true; }
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
		NPCDefinition.setOptionHandler("attack", this);
		return this;
	}

	@Override
	public boolean isDelayed(Player player) {
		return false;
	}

}
