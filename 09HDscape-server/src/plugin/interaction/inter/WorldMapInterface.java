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

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		switch (button) {
		case 3:
			player.getInterfaceManager().openWindowsPane(new Component(player.getInterfaceManager().isResizable() ? 746 : 548));
			int posHash = player.getLocation().getX() << 14 | player.getLocation().getY();
			player.getPacketDispatch().sendScriptConfig(622, posHash);
			player.getPacketDispatch().sendScriptConfig(674, posHash);
			return true;
		}
		return false;
	}

}
