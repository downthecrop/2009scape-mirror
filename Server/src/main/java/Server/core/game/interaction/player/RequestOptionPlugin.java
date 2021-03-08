package core.game.interaction.player;

import core.game.interaction.Option;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.request.RequestType;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to handle the player option interacting.
 * @author 'Vexia
 */
@Initializable
public final class RequestOptionPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		Option._P_ASSIST.setHandler(this);
		Option._P_TRADE.setHandler(this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.getRequestManager().request((Player) node, RequestType.forOption(option));
		return true;
	}
}
