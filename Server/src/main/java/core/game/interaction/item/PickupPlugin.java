package core.game.interaction.item;

import core.cache.def.impl.ItemDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.GroundItem;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;
import rs09.game.content.global.action.PickupHandler;

/**
 * Represents the option handler used for ground items.
 * @author Vexia
 * @author Emperor
 */
@Initializable
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
		//player.setAttribute("pickup", "true");
		boolean handleResult = PickupHandler.take(player, (GroundItem) node);
		player.removeAttribute("pickup");
		return handleResult;
	}

	@Override
	public Location getDestination(Node node, Node item) {
		return null;
	}

}
