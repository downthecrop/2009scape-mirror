package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the mortynia swamp plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class MoyrtniaSwampPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(3506).getHandlers().put("option:open", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.getDialogueInterpreter().sendDialogue("There's a message attached to this gate, it reads:~", "~ Mort Myre is a dangerous Ghast infested swamp. ~", "~ Do not enter if you value your life. ~", "~ All persons wishing to enter must see Drezel. ~");
		return true;
	}

}
