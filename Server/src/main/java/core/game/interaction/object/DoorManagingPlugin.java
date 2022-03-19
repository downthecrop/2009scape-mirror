package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.content.global.action.DoorActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Plugin used for handling the opening/closing of (double)
 * doors/gates/fences/...
 * @author Emperor
 */
@Initializable
public final class DoorManagingPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.setOptionHandler("open", this);
		SceneryDefinition.setOptionHandler("close", this);
		SceneryDefinition.setOptionHandler("shut", this);
		SceneryDefinition.setOptionHandler("go-through", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		Scenery object = (Scenery) node;
		if (object.getType() != 9 && !player.getLocation().equals(node.getLocation()) && !player.getLocation().isNextTo(object) && object.getName().contains("cupboard")) {
			return true;
		}
		String name = object.getName().toLowerCase();
		if (name.contains("drawers") || name.contains("wardrobe") || name.contains("cupboard")) {
			switch(option) {
				case "open":
				case "go-through":
					if (object.isActive()) {
						SceneryBuilder.replace(object, object.transform(object.getId() + 1), 80);
					}
					return true;
				case "close":
				case "shut":
					SceneryBuilder.replace(object, object.transform(object.getId() - 1));
					return true;
			}
			return true;
		}
		if (name.contains("trapdoor") || name.contains("trap door")) {
			Location destination = object.getLocation().transform(0, 6400, 0);
			if (!RegionManager.isTeleportPermitted(destination)) {
				player.getPacketDispatch().sendMessage("This doesn't seem to go anywhere.");
				return true;
			}
			player.getProperties().setTeleportLocation(destination);
			return true;
		}
		if (node.asScenery().getId() == 25341) {// Mithril door
			return false;
		}
		if (!name.contains("door") && !name.contains("gate") && !name.contains("fence") && !name.contains("wall") && !name.contains("exit") && !name.contains("entrance")) {
			return false;
		}
		DoorActionHandler.handleDoor(player, object);
		return true;
	}

	@Override
	public Location getDestination(Node n, Node node) {
		Scenery o = (Scenery) node;
		if (o.getType() < 4 || o.getType() == 9) {
			return DoorActionHandler.getDestination((Player) n, o);
		}
		return null;
	}

}
