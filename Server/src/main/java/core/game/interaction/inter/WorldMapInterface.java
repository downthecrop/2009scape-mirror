package core.game.interaction.inter;

import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the world map interface.
 * @author Emperor
 *
 */
@Initializable
public final class WorldMapInterface extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.forId(755).setPlugin(this);
		return this;
	}

	//Thanks snicker!
	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		switch (button) {
			case 3:
				player.getInterfaceManager().openWindowsPane(new Component(player.getInterfaceManager().isResizable() ? 746 : 548), 2);
				player.getPacketDispatch().sendRunScript(1187, "ii", 0, 0);
				player.updateSceneGraph(true);
				return true;
			default:
				//SystemLogger.logErr("World map: buttonid: " + button + ", opcode: " + opcode + ", slot: " + slot);
				return true;
		}
	}

}
