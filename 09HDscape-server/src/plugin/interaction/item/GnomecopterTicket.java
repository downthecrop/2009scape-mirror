package plugin.interaction.item;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.component.Component;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * The gnomecopter ticket handling plugin.
 * @author Emperor
 */
@InitializablePlugin
public final class GnomecopterTicket extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(12843).getConfigurations().put("option:read", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.getInterfaceManager().open(new Component(729));
		String info = "Gnomecopter ticket:";
		info += "<br>" + "Castle Wars"; // Destination
		info += "<br>" + "Ref. #000";
		for (int i = 3; i < 8; i++) {
			info += RandomFunction.randomize(10);
		}
		player.getPacketDispatch().sendString(info, 729, 2);
		return true;
	}

}
