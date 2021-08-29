package core.game.node.entity.skill.agility.shortcuts;

import core.game.node.entity.skill.agility.AgilityShortcut;
import core.game.node.entity.impl.ForceMovement;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;

/**
 * Handles a crumbling wall.
 * @author Vexia
 */
@Initializable
public class CrumblingWallShortcut extends AgilityShortcut {

	/**
	 * Represents the locations used in this force movement.
	 */
	private static final Location[] LOCATIONS = new Location[] { new Location(2936, 3355, 0), new Location(2934, 3355, 0) };

	/**
	 * Constructs a new {@Code CrumblingWallShortcut} {@Code
	 * Object}
	 */
	public CrumblingWallShortcut() {
		super(new int[] { 11844 }, 5, 0.0, "climb-over");
	}

	@Override
	public void run(Player player, Scenery object, String option, boolean failed) {
		ForceMovement.run(player, player.getLocation().getX() >= 2936 ? LOCATIONS[0] : LOCATIONS[1], player.getLocation().getX() >= 2936 ? LOCATIONS[1] : LOCATIONS[0], Animation.create(839), 10);
	}

}
