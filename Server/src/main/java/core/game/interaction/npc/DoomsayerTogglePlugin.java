package core.game.interaction.npc;

import core.cache.def.impl.NPCDefinition;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the option plugin used to toggle the doomsayer interface.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public class DoomsayerTogglePlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.forId(3777).getHandlers().put("option:toggle-warnings", this);
		new WarningMessagePlugin().newInstance(arg);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.getWarningMessages().open(player);
		return true;
	}

	/**
	 * Represents the plugin used to handle the warning message plugin.
	 * @author 'Vexia
	 */
	public final class WarningMessagePlugin extends ComponentPlugin {

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			ComponentDefinition.put(583, this);
			return this;
		}

		@Override
		public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
			if (button > 45 && button < 74) {
				player.getWarningMessages().getMessage(button).toggle(player);
			}
			return true;
		}

	}
}
