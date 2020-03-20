package plugin.interaction.inter;

import org.crandor.game.component.Component;
import org.crandor.game.component.ComponentDefinition;
import org.crandor.game.component.ComponentPlugin;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles the world map interface.
 * @author Emperor
 *
 */
@InitializablePlugin
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
				System.err.println("World map: buttonid: " + button + ", opcode: " + opcode + ", slot: " + slot);
				return true;
		}
	}

}
