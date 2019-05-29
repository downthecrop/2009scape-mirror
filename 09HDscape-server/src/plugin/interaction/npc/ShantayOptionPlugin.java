package plugin.interaction.npc;

import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.game.content.dialogue.DialogueInterpreter;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the option plguin.
 * @author 'Vexia
 */
@InitializablePlugin
public class ShantayOptionPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.forId(836).getConfigurations().put("option:buy-pass", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		DialogueInterpreter interpreter = player.getDialogueInterpreter();
		if (player.getInventory().remove(new Item(995, 5))) {
			player.getInventory().add(new Item(1854));
			interpreter.sendItemMessage(1854, "You purchase a Shantay Pass.");
		} else {
			interpreter.sendDialogues(player, null, "Sorry, I don't seem to have enough money.");
		}
		return true;
	}

}
