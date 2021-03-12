package core.game.interaction.inter;

import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.node.entity.player.Player;
import rs09.game.world.GameWorld;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the interface used to logout of the game.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class LogoutInterface extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(182, this);
		return this;
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		if (!player.getZoneMonitor().canLogout()) {
			return true;
		}
		if (player.inCombat()) {
			player.getPacketDispatch().sendMessage("You can't log out until 10 seconds after the end of combat.");
			return true;
		}
		if (player.getAttribute("logoutDelay", 0) < GameWorld.getTicks()) {
			player.getPacketDispatch().sendLogout();
			player.setAttribute("logoutDelay", GameWorld.getTicks() + 3);
		}
		return true;
	}
}
