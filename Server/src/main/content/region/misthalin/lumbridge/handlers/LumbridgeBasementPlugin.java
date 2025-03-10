package content.region.misthalin.lumbridge.handlers;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.OptionHandler;
import core.game.interaction.UseWithHandler;
import core.game.node.Node;
import core.game.node.entity.impl.ForceMovement;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.path.Pathfinder;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;
import core.plugin.ClassScanner;
import core.plugin.Initializable;
import core.tools.RandomFunction;

import static core.api.ContentAPIKt.hasRequirement;
import content.data.Quests;

/**
 * Handles the lumbridge basement.
 * @author Vexia
 *
 */
@Initializable
public class LumbridgeBasementPlugin extends OptionHandler {

	/**
	 * The animation to use for the shortcut.
	 */
	private static final Animation ANIMATION = new Animation(2240);

	/**
	 * The jumping animation for stepping stones.
	 */
	private static final Animation JUMP_ANIMATION = new Animation(741);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(6899).getHandlers().put("option:squeeze-through", this);
		SceneryDefinition.forId(6898).getHandlers().put("option:squeeze-through", this);
		SceneryDefinition.forId(6905).getHandlers().put("option:squeeze-through", this);
		SceneryDefinition.forId(6912).getHandlers().put("option:squeeze-through", this);
		SceneryDefinition.forId(5949).getHandlers().put("option:jump-across", this);
		SceneryDefinition.forId(6658).getHandlers().put("option:enter", this);
		SceneryDefinition.forId(32944).getHandlers().put("option:enter", this);
		SceneryDefinition.forId(40261).getHandlers().put("option:climb-up", this);
		SceneryDefinition.forId(40262).getHandlers().put("option:climb-up", this);
		SceneryDefinition.forId(40849).getHandlers().put("option:jump-down", this);
		SceneryDefinition.forId(40260).getHandlers().put("option:climb-through", this);
		SceneryDefinition.forId(41077).getHandlers().put("option:crawl-through", this);
		SceneryBuilder.add(new Scenery(40260, Location.create(2526, 5828, 2), 2));
		return this;
	}

	@Override
	public boolean handle(final Player player, Node node, String option) {
		switch (option) {
		case "squeeze-through":
			Direction dir = null;
			Location to = null;
			switch (node.getId()) {
			case 6912:
				to = node.getLocation().getY() == 9603 ? Location.create(3224, 9601, 0) : Location.create(3224, 9603, 0);
				dir = node.getLocation().getY() == 9603 ? Direction.SOUTH : Direction.NORTH;
				break;
			default:
				to = player.getLocation().getX() >= 3221 ? Location.create(3219, 9618, 0) : Location.create(3222, 9618, 0);
				dir = player.getLocation().getX() >= 3221 ? Direction.WEST : Direction.EAST;
				break;
			}
			player.sendMessage("You squeeze through the hole.");
			ForceMovement.run(player, player.getLocation(), to, ANIMATION, ANIMATION, dir, 20).setEndAnimation(Animation.RESET);
			return true;
		case "jump-across":
			Location f = null;
			Location s = null;
			switch (node.getId()) {
			case 5949:
				f = Location.create(3221, 9554, 0);
				s = player.getLocation().getY() >= 9556 ?  Location.create(3221, 9552, 0) : Location.create(3221, 9556, 0);
				break;
			}
			final Location first = f;
			final Location second = s;
			player.lock();
			GameWorld.getPulser().submit(new Pulse(2, player) {
				int counter = 1;

				@Override
				public boolean pulse() {
					if (counter == 3) {
						player.unlock();
						ForceMovement.run(player, player.getLocation(), second, JUMP_ANIMATION, 20);
						player.sendMessage("You leap across with a mighty leap!");
						return true;
					} else if (counter == 1){
						ForceMovement.run(player, player.getLocation(), first, JUMP_ANIMATION, 20);
					}
					counter++;
					return false;
				}
			});
			break;
		case "enter":
			switch (node.getId()) {
			case 32944:
				//Location.create(3353,3951,0)
				player.teleport(Location.create(3219, 9532, 2));
				break;
			case 6658:
				//Location.create(3226, 9542, 0)
				player.teleport(Location.create(3226, 9542, 0));
				break;
			}
			break;
		case "climb-up":
			switch (node.getId()) {
			case 40261:
				player.teleport(player.getLocation().transform(0, -1, 1));
				break;
			case 40262:
				player.teleport(player.getLocation().transform(0, -1, 1));
				break;
			}
			break;
		case "jump-down":
			switch (node.getId()) {
			case 40849:
				player.teleport(player.getLocation().transform(0, 1, -1));
				break;
			}
			break;
		case "climb-through":
			switch (node.getId()) {
			case 40260:
				player.teleport(Location.create(2525, 5810, 0));
				break;
			}
			break;
		case "crawl-through":
			switch (node.getId()) {
			case 41077:
				player.teleport(Location.create(2527, 5830, 2));
				break;
			}
			break;
		}
		return true;
	}

	@Override
	public Location getDestination(Node node, Node n) {
		if (n instanceof Scenery) {
			if (n.getId() == 5949) {
				return node.getLocation().getY() >= 9555 ? Location.create(3221, 9556, 0) : Location.create(3221, 9552, 0);
			} else if (n.getId() == 40262) {
				return n.getLocation().transform(0, 1, 0);
			} else if (n.getId() == 40261) {
				return n.getLocation().transform(0, 1, 0);
			}
		}
		return null;
	}

}
