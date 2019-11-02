package plugin.dialogue;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used to handle the enchanted gem related to slayer.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class ActivateEnchantedGem extends OptionHandler {

	/**
	 * Constructs a new {@code ActivateEnchantedGem} {@Code Object}.
	 */
	public ActivateEnchantedGem() {
		/**
		 * empty.
		 */
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (!player.getSlayer().hasStarted()) {
			player.getPacketDispatch().sendMessage("You try to activate the gem...");
			return true;
		}
		player.getDialogueInterpreter().open(77777);
		return true;
	}

	@Override
	public boolean isWalk() {
		return false;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(4155).getConfigurations().put("option:activate", this);
		return this;
	}
}
