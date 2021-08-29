package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.content.global.action.ClimbActionHandler;
import core.game.content.global.action.DoorActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the underground shortcut from the Canifis trapdoor to the swamp.
 * @author Splinter - March 1st
 */
@Initializable
public class BarrowsTunnelShortcut extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(5055).getHandlers().put("option:open", this);
		SceneryDefinition.forId(5054).getHandlers().put("option:climb-up", this);
		SceneryDefinition.forId(5052).getHandlers().put("option:search", this);
		SceneryDefinition.forId(30261).getHandlers().put("option:open", this);
		SceneryDefinition.forId(30262).getHandlers().put("option:open", this);
		SceneryDefinition.forId(30265).getHandlers().put("option:open", this);
		SceneryDefinition.forId(5005).getHandlers().put("option:climb up", this);
		SceneryDefinition.forId(5005).getHandlers().put("option:climb down", this);
		SceneryDefinition.forId(5002).getHandlers().put("option:walk-here", this);
		return this;
	}

	@Override
	public boolean handle(final Player player, Node node, String option) {
		switch (node.getId()) {
		case 5055:
			player.teleport(new Location(3477, 9845));
			break;
		case 5054:
			ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, new Location(3496, 3465, 0));
			break;
		case 5052:
			player.getPacketDispatch().sendMessage("You search the wall and find a lever.");
			DoorActionHandler.handleAutowalkDoor(player, ((Scenery) node));
			break;
		case 30261:
		case 30262:
			player.teleport(new Location(3509, 3448), 1);
			break;
		case 30265:
			player.teleport(new Location(3500, 9812), 1);
			break;
		case 5002:

			break;
		case 5005:// First tree
			if (node.getLocation().equals(new Location(3502, 3431))) {
				switch (option) {
				case "climb up":
					ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, new Location(3502, 3425, 0));
					break;
				case "climb down":
					ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_DOWN, new Location(3503, 3431, 0));
					break;
				}
				break;
			} else {// second tree
				switch (option) {
				case "climb up":
					ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, new Location(3503, 3431, 0));
					break;
				case "climb down":
					ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_DOWN, new Location(3502, 3425, 0));
					break;
				}
			}
		}
		return true;
	}

}
