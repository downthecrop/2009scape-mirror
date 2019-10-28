package plugin.interaction.player;

import org.crandor.game.interaction.Option;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.request.RequestType;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used to handle the player option interacting.
 * @author 'Vexia
 */
@InitializablePlugin
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
