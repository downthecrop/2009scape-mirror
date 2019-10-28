package plugin.interaction.object;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.global.action.ClimbActionHandler;
import org.crandor.game.content.global.action.DoorActionHandler;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles the underground shortcut from the Canifis trapdoor to the swamp.
 * @author Splinter - March 1st
 */
@InitializablePlugin
public class BarrowsTunnelShortcut extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(5055).getConfigurations().put("option:open", this);
		ObjectDefinition.forId(5054).getConfigurations().put("option:climb-up", this);
		ObjectDefinition.forId(5052).getConfigurations().put("option:search", this);
		ObjectDefinition.forId(30261).getConfigurations().put("option:open", this);
		ObjectDefinition.forId(30262).getConfigurations().put("option:open", this);
		ObjectDefinition.forId(30265).getConfigurations().put("option:open", this);
		ObjectDefinition.forId(5005).getConfigurations().put("option:climb up", this);
		ObjectDefinition.forId(5005).getConfigurations().put("option:climb down", this);
		ObjectDefinition.forId(5002).getConfigurations().put("option:walk-here", this);
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
			DoorActionHandler.handleAutowalkDoor(player, ((GameObject) node));
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
