package core.game.content.activity.duel;

import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.node.entity.player.Player;
import rs09.game.world.GameWorld;
import core.plugin.Plugin;

/**
 * Handles the duel arena components.
 * @author Vexia
 *
 */
public class DuelComponentPlugin extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.forId(640).setPlugin(this);
		return this;
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		switch (component.getId()) {
		case 640:
			boolean staked = false;
			switch (button) {
			case 20:
				Player other = player.getAttribute("duel:partner");
				if (other == null || other.getExtension(DuelSession.class) != null) {
					player.getPacketDispatch().sendMessage("Other player is busy at the moment.");
					return true;
				}
				if (player.getAttribute("duel:staked", false) && other.getIronmanManager().isIronman() && !GameWorld.getSettings().isDevMode()) {
					other.sendMessage("You can't accept a staked duel as an Ironman.");
					player.sendMessage("You can't duel Ironman players.");
					return true;
				}
				player.getInterfaceManager().close();
				if (!player.getAttribute("duel:staked", false)) {
					player.getRequestManager().request(other, DuelArenaActivity.FRIEND_REQUEST);
				} else {
					player.getRequestManager().request(other, DuelArenaActivity.STAKE_REQUEST);
				}
				return true;
			case 18:
			case 22:
				staked = false;
				break;
			case 19:
			case 21:
				staked = true;
				if (player.getIronmanManager().isIronman()) {
					player.sendMessage("You can't stake as an Iron man.");
					staked = false;
				}
				break;
			default:
				return false;
			}
			player.setAttribute("duel:staked", staked);
			player.getConfigManager().set(283, (staked ? 2 : 1) << 26);
			break;
		}
		return true;
	}

}
