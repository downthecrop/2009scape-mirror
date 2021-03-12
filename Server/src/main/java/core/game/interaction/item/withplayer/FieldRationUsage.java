package core.game.interaction.item.withplayer;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles using a field ration on a player.
 * @author Emperor
 */
@Initializable
public final class FieldRationUsage extends UseWithHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(7934, PLAYER_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		Item item = (Item) event.getUsed();
		Player target = (Player) event.getUsedWith();
		if (target == null || !target.isActive() || target.getLocks().isInteractionLocked()) {
			event.getPlayer().getPacketDispatch().sendMessage("The other player is currently busy.");
			return true;
		}
		if (event.getPlayer().getInventory().remove(item)) {
			target.getSkills().heal(12);
			target.getPacketDispatch().sendMessage("You have been healed by a field ration.");
			event.getPlayer().getPacketDispatch().sendMessage("You use the field ration to heal the other player.");
			return true;
		}
		return false;
	}

}
