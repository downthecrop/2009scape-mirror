package plugin.interaction.object;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.global.action.ClimbActionHandler;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used for managing ladders.
 * @author Emperor
 * @version 2.0
 */
@InitializablePlugin
public final class LadderManagingPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.setOptionHandler("climb-up", this);
		ObjectDefinition.setOptionHandler("climb-down", this);
		ObjectDefinition.setOptionHandler("climb", this);
		return this;
	}

	@Override
	public boolean handle(final Player player, Node node, final String option) {
		ClimbActionHandler.climbLadder(player, (GameObject) node, option);
		return true;
	}

	@Override
	public Location getDestination(Node n, Node object) {
		return ClimbActionHandler.getDestination((GameObject) object);
	}

}
