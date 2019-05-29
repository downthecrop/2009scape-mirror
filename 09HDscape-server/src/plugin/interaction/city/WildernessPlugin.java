package plugin.interaction.city;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.global.action.ClimbActionHandler;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.TeleportManager.TeleportType;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents a plugin used to handle wilderness nodes.
 * 
 * @author 'Vexia
 * @author Splinter
 * @version 1.0
 */
@InitializablePlugin
public final class WildernessPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		new KBDPlugin().newInstance(arg);
		ObjectDefinition.forId(37749).getConfigurations().put("option:go-through", this);
		ObjectDefinition.forId(37928).getConfigurations().put("option:go-through", this);
		ObjectDefinition.forId(37929).getConfigurations().put("option:go-through", this);
		ObjectDefinition.forId(38811).getConfigurations().put("option:go-through", this);
		ObjectDefinition.forId(39191).getConfigurations().put("option:climb-up", this);
		ObjectDefinition.forId(39188).getConfigurations().put("option:open", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (node.getId()) {
		case 37749:
			player.teleport(Location.create(2885, 4372, 2));
			break;
		case 39191:
			ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, Location.create(3239, 3606, 0), "You climb up the ladder to the surface.");
			break;
		case 39188:
			ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_DOWN, Location.create(3241, 9991, 0), "You descend into the cavern below.");
			break;
		case 37928:
			player.teleport(Location.create(3214, 3782, 0));
			break;
		case 38811:
		case 37929:
			player.teleport(player.getLocation().transform(player.getLocation().getX() < node.getLocation().getX() ? 4 : -4, 0, 0));
			break;
		}
		return true;
	}

	/**
	 * Represents the plugin used to handle kbd nodes.
	 * 
	 * @author 'Vexia
	 * @version 1.0
	 */
	public static final class KBDPlugin extends OptionHandler {

		/**
		 * Represents the locations used for kbd interactions.
		 */
		private static final Location[] LOCATIONS = new Location[] { new Location(3017, 3849, 0), Location.create(3069, 10257, 0), new Location(3069, 3856, 0), new Location(3017, 3850, 0), Location.create(2272, 4680, 0), Location.create(3067, 10253, 0), new Location(3069, 10256, 0) };

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			ObjectDefinition.forId(1765).getConfigurations().put("option:climb-down", this);// ladder
			ObjectDefinition.forId(1766).getConfigurations().put("option:climb-up", this);// ladder
			ObjectDefinition.forId(1816).getConfigurations().put("option:pull", this);// kbd
			ObjectDefinition.forId(1817).getConfigurations().put("option:pull", this);// kbd
			return this;
		}

		@Override
		public boolean handle(Player player, Node node, String option) {
			int id = ((GameObject) node).getId();
			switch (option) {
			case "climb-down":
				switch (id) {
				case 1765:// kbd ladder.
					if (node.getLocation().equals(LOCATIONS[0])) {
						ClimbActionHandler.climb(player, new Animation(827), LOCATIONS[1]);
					} else {
						ClimbActionHandler.climbLadder(player, (GameObject) node, option);
						return true;
					}
					break;
				}
				break;
			case "climb-up":
				switch (id) {
				case 1766:// kbd leave ladder.
					if (node.getLocation().equals(LOCATIONS[6])) {
						ClimbActionHandler.climb(player, new Animation(828), LOCATIONS[3]);
					} else {
						ClimbActionHandler.climbLadder(player, (GameObject) node, option);
						return true;
					}
					break;
				}
				break;
			case "pull":
				switch (id) {
				case 1816:// kbd lever.
					if (player.getLocation().withinDistance(LOCATIONS[5])) {
						player.getPacketDispatch().sendMessage("You pull the lever...");
						player.getTeleporter().send(LOCATIONS[4], TeleportType.NORMAL);
						player.getPacketDispatch().sendMessage("... and teleport into the lair of the King Black Dragon!", 5);
					}
					break;
				case 1817:
					if (player.getLocation().withinDistance(LOCATIONS[4])) {
						player.getPacketDispatch().sendMessage("You pull the lever...");
						player.getTeleporter().send(LOCATIONS[5], TeleportType.NORMAL);
						player.getPacketDispatch().sendMessage("... and teleport out of the lair of the King Black Dragon!", 5);
					}
					break;
				}
				break;
			}
			return true;
		}

		@Override
		public Location getDestination(Node node, Node n) {
			// if (n instanceof GameObject) {
			// int id = ((GameObject) n).getId();
			// if (id == 1817) {
			// return Location.create(2273, 4680, 0);
			// } else if (id == 1816) {
			// return Location.create(3067, 10253, 0);
			// }
			// }
			return null;
		}
	}
}
