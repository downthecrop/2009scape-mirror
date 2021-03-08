package core.game.interaction.item;

import core.cache.def.impl.ItemDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.TeleportManager.TeleportType;
import core.game.node.item.Item;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;

@Initializable
public class HouseTeleTabOptionPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(8013).getHandlers().put("option:break", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (player.getHouseManager().getLocation().getExitLocation() == null) {
			player.sendMessage("You must have a house to teleport to before attempting that.");
			return false;
		}
		player.getInterfaceManager().close();
		player.lock(5);
		Location location = player.getHouseManager().getLocation().getExitLocation();
		if (player.getInventory().contains(node.asItem().getId(), 1)) {
			if (player.getTeleporter().send(location.transform(0, RandomFunction.random(3), 0), TeleportType.TELETABS, 1)) {
				player.getInventory().remove(new Item(node.asItem().getId(),1));
				return true;
			}
		}
		return false;
	}

}
