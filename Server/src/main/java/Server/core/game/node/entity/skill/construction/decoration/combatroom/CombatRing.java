package core.game.node.entity.skill.construction.decoration.combatroom;


import core.cache.def.impl.ObjectDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the combat ring.
 * @author Emperor
 *
 */
@Initializable
public final class CombatRing extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(13129).getHandlers().put("option:climb-over", this); //Boxing ring
		ObjectDefinition.forId(13133).getHandlers().put("option:climb-over", this); //Fencing ring
		ObjectDefinition.forId(13137).getHandlers().put("option:climb-over", this); //Combat ring
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		return false;
	}

}