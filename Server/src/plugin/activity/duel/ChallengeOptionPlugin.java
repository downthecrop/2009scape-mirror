package plugin.activity.duel;

import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.Plugin;

/**
 * Handles the challenge option for dueling.
 * @author Vexia
 *
 */
public class ChallengeOptionPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		DuelArenaActivity.CHALLENGE_OPTION.setHandler(this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		Player other = (Player) node;
		if (other.getInterfaceManager().isOpened() || other.getExtension(DuelSession.class) != null) {
			player.getPacketDispatch().sendMessage("Other player is busy at the moment.");
			return true;
		}
		if (other.getRequestManager().getTarget() == player && other.getAttribute("duel:partner") == player) {
			player.getRequestManager().request(other, other.getAttribute("duel:staked", false) ? DuelArenaActivity.STAKE_REQUEST : DuelArenaActivity.FRIEND_REQUEST);
			return true;
		}
		player.getInterfaceManager().open(DuelArenaActivity.DUEL_TYPE_SELECT);
		player.setAttribute("duel:staked", false);
		player.setAttribute("duel:partner", other);
		player.getConfigManager().set(283, 1 << 26);
		return true;
	}

}
