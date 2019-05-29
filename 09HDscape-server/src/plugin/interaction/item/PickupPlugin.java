package plugin.interaction.item;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.content.global.action.PickupHandler;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.GroundItem;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the option handler used for ground items.
 * @author Vexia
 * @author Emperor
 */
@InitializablePlugin
public final class PickupPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.setOptionHandler("take", this);
		return this;
	}

	@Override
	public boolean handle(final Player player, Node node, String option) {
		if (player.getAttributes().containsKey("pickup"))
		    return false;	
		player.setAttribute("pickup", "true");
		boolean handleResult = PickupHandler.take(player, (GroundItem) node);
		player.removeAttribute("pickup");
		return handleResult;
	}

	@Override
	public Location getDestination(Node node, Node item) {
		return null;
	}

}
