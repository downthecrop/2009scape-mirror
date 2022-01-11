package core.game.node.entity.skill.agility.shortcuts;

import core.game.content.activity.ActivityManager;
import core.plugin.Initializable;
import core.game.node.entity.skill.agility.AgilityShortcut;
import core.game.interaction.MovementPulse;
import core.game.node.entity.impl.ForceMovement;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;

/**
 * Handles a stile shortcut.
 * @author Vexia
 */
@Initializable
public class StileShortcut extends AgilityShortcut {

	/**
	 * Constructs a new {@Code StileShortcut} {@Code Object}
	 */
	public StileShortcut() {
		super(new int[] { 993, 3730, 7527, 12982, 19222, 22302, 29460, 33842, 34776, 39508, 39509, 39510 }, 1, 0.0, false, 0.0, new String[] { "climb-over" });

	}

	@Override
	public void run(final Player player, final Scenery object, String option, boolean failed) {
		player.getWalkingQueue().reset();
		Location loc = getLocation(player, object, true);
		player.getWalkingQueue().addPath(loc.getX(), loc.getY());
		player.getPulseManager().run(new MovementPulse(player, loc) {
			@Override
			public boolean pulse() {
				climb(player, object);
				return true;
			}
		}, "movement");
	}

	/**
	 * Method used to climb the stile.
	 * @param player the player.
	 * @param object the object.
	 */
	public static void climb(final Player player, final Scenery object) {
		player.lock(1);
		int delay = 0;
		GameWorld.getPulser().submit(new Pulse(delay, player) {
			@Override
			public boolean pulse() {
				ForceMovement movement = new ForceMovement(player, getLocation(player, object, true), getLocation(player, object, false), Animation.create(839)) {
					@Override
					public void stop() {
						super.stop();
						if (object.getId() == 19222) {// falconry.
							handleFalconry(player, object);
						}
					}
				};
				movement.run(player, 5);
				return true;
			}
		});
	}

	/**
	 * Method used to get the end location.
	 * @param player the player.
	 * @param object the object.
	 * @param start if getting the start loc.
	 * @return the location.
	 */
	public static Location getLocation(final Player player, final Scenery object, boolean start) {
		if ((object.getDirection() == Direction.NORTH || object.getDirection() == Direction.SOUTH)) {
			if (player.getLocation().getY() <= object.getLocation().getY()) {
				return start ? object.getLocation() : object.getLocation().transform(0, 1, 0);
			} else {
				return start ? object.getLocation().transform(0, 1, 0) : object.getLocation();
			}
		} else {
			if (player.getLocation().getX() <= object.getLocation().getX()) {
				return start ? object.getLocation() : object.getLocation().transform(1, 0, 0);
			} else {
				return start ? object.getLocation().transform(1, 0, 0) : object.getLocation();
			}
		}
	}

	/**
	 * Method used to handle the falconry stile.
	 * @param player the player.
	 * @param object the object.
	 */
	private static void handleFalconry(final Player player, Scenery object) {
		if (player.getLocation().equals(Location.create(2371, 3621, 0))) {
			ActivityManager.start(player, "falconry", false);
		} else {
			ActivityManager.getActivity("falconry").leave(player, false);
		}
	}

}
