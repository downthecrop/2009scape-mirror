package core.game.node.entity.skill.agility.shortcuts;

import core.plugin.Initializable;
import core.game.node.entity.skill.agility.AgilityHandler;
import core.game.node.entity.skill.agility.AgilityShortcut;
import core.game.node.Node;
import core.game.node.entity.impl.ForceMovement;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import kotlin.Unit;
import rs09.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;
import core.tools.RandomFunction;

/**
 * Handles the monkey bar shortcut.
 * @author Vexia
 */
@Initializable
public class MonkeyBarShortcut extends AgilityShortcut {

	/**
	 * Represents the location cache data for the monkey bars.
	 */
	private static final Location[][] MBAR_LOCATIONS = new Location[][] { { new Location(3120, 9969, 0), Location.create(3121, 9969, 0) }, { new Location(3119, 9969, 0), Location.create(3120, 9969, 0) }, { new Location(3120, 9964, 0), Location.create(3121, 9964, 0) }, { Location.create(3120, 9963, 0), Location.create(3120, 9964, 0) }, { new Location(2598, 9489, 0), new Location(2597, 9488, 0) }, { new Location(2598, 9489, 0), new Location(2600, 9488, 0) }, { new Location(2598, 9494, 0), new Location(2597, 9495, 0) }, { new Location(2599, 9494, 0), new Location(2600, 9495, 0) } };

	/**
	 * Constructs a new {@Code MonkeyBarShortcut} {@Code Object}
	 */
	public MonkeyBarShortcut() {
		super(new int[] { 29375 }, 1, 14.0, "swing across");
	}

	/**
	 * Constructs a new {@Code MonkeyBarShortcut} {@Code Object}
	 * @param ids the ids.
	 * @param level the level.
	 * @param experience the exp.
	 * @param option the option.
	 */
	public MonkeyBarShortcut(int[] ids, int level, double experience, String option) {
		super(ids, level, experience, option);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		configure(new MonkeyBarShortcut(new int[] { 2321 }, 57, 20, "swing across"));
		return super.newInstance(arg);
	}

	@Override
	public void run(final Player player, final Scenery object, String option, boolean failed) {
		player.lock(5);
		Direction direct = Direction.get((object.getDirection().toInteger() + 2) % 4);
		if (object.getId() == 29375) {
			if (direct == Direction.SOUTH && player.getLocation().getY() < 9969) {
				direct = Direction.NORTH;
			} else if (direct == Direction.NORTH && player.getLocation().getY() >= 9969) {
				direct = Direction.SOUTH;
			}
		} else if (object.getId() == 2321) {
			if (player.getLocation().getY() >= 9494) {
				direct = Direction.SOUTH;
			} else {
				direct = Direction.NORTH;
			}
		}
		player.getAppearance().setAnimations(Animation.create(745));
		final Location start = player.getLocation();
		final Direction dir = direct;
		ForceMovement.run(player, start.transform(dir), start.transform(dir.getStepX() << 1, dir.getStepY() << 1, 0), Animation.create(742), Animation.create(744));
		player.logoutListeners.put("monkey-bar", p -> {
			p.setLocation(start);
			return Unit.INSTANCE;
		});
		GameWorld.getPulser().submit(new Pulse(2, player) {
			int count;
			boolean failed;

			@Override
			public boolean pulse() {
				if (++count == 1) {
					if (object.getId() == 2321 && (failed = AgilityHandler.hasFailed(player, 57, 0.01))) {
						setDelay(1);
						player.animate(Animation.create(743));
						return false;
					}
					setDelay(4);
					AgilityHandler.walk(player, -1, player.getLocation().transform(dir), player.getLocation().transform(dir.getStepX() * 4, dir.getStepY() * 4, 0), Animation.create(662), 0.0, null);
				} else if (count == 2) {
					if (failed) {
						player.getAppearance().setAnimations();
						player.getAppearance().sync();
						AgilityHandler.fail(player, 2, new Location(2599, 9564, 0), Animation.create(768), RandomFunction.random(1, 3), null);
						player.logoutListeners.remove("monkey-bar");
						return true;
					}
					AgilityHandler.forceWalk(player, -1, player.getLocation(), player.getLocation().transform(dir), Animation.create(743), 10, getExperience(), null);
					player.logoutListeners.remove("monkey-bar");
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public Location getDestination(Node n, Node node) {
		if (node.getLocation().equals(new Location(2598, 9489, 0))) {
			return new Location(2597, 9488, 0);
		} else if (node.getLocation().equals(new Location(2598, 9494, 0))) {
			return new Location(2597, 9495, 0);
		} else if (node.getLocation().equals(new Location(2599, 9489, 0))) {
			return new Location(2600, 9488, 0);
		}
		for (Location[] locations : MBAR_LOCATIONS) {
			if (n.getLocation().equals(locations[0])) {
				return locations[1];
			}
		}
		if (node.getLocation().equals(new Location(2598, 9489, 0)) || node.getLocation().equals(new Location(2599, 9489, 0))) {
			return new Location(2600, 9495, 0);
		}
		return null;
	}
}
