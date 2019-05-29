package plugin.dialogue;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * @author 'Vexia
 */
@InitializablePlugin
public class VarrockBrokenCart extends OptionHandler {

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.getDialogueInterpreter().open(70099, "You search the cart but are surprised to find very little there. It's a", "little odd for a travelling trader not to have anything to trade.");
		return true;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(23055).getConfigurations().put("option:search", this);
		return this;
	}

}
