package plugin.skill.cooking;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used for the fire pottery object.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class FireOptionPotteryPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(2643).getConfigurations().put("option:fire", this);
		ObjectDefinition.forId(4308).getConfigurations().put("option:fire", this);
		ObjectDefinition.forId(11601).getConfigurations().put("option:fire", this);
		ObjectDefinition.forId(34802).getConfigurations().put("option:fire", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.faceLocation(node.getLocation());
		player.getDialogueInterpreter().open(99843, true, true);
		return true;
	}

}
