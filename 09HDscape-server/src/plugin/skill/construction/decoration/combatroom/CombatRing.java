package plugin.skill.construction.decoration.combatroom;


import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles the combat ring.
 * @author Emperor
 *
 */
@InitializablePlugin
public final class CombatRing extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(13129).getConfigurations().put("option:climb-over", this); //Boxing ring
		ObjectDefinition.forId(13133).getConfigurations().put("option:climb-over", this); //Fencing ring
		ObjectDefinition.forId(13137).getConfigurations().put("option:climb-over", this); //Combat ring
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		return false;
	}

}