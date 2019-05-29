package plugin.interaction.object;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the mortynia swamp plugin.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class MoyrtniaSwampPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(3506).getConfigurations().put("option:open", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.getDialogueInterpreter().sendDialogue("There's a message attached to this gate, it reads:~", "~ Mort Myre is a dangerous Ghast infested swamp. ~", "~ Do not enter if you value your life. ~", "~ All persons wishing to enter must see Drezel. ~");
		return true;
	}

}
