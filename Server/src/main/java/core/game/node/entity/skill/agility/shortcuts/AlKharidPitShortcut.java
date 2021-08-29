package core.game.node.entity.skill.agility.shortcuts;

import core.plugin.Initializable;
import core.game.node.entity.skill.agility.AgilityShortcut;
import core.game.node.Node;
import core.game.node.entity.impl.ForceMovement;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;

/**
 * Handles the al kharid pit shortcut.
 * @author adam
 */
@Initializable
public class AlKharidPitShortcut extends AgilityShortcut {

	/**
	 * Represents the scaling down anmation.
	 */
	private static final Animation ANIMATION = new Animation(1148);

	/**
	 * Represents the scaling animation.
	 */
	private static final Animation SCALE = new Animation(740);

	/**
	 * Constructs a new {@Code AlKharidPitShortcut} {@Code Object}
	 */
	public AlKharidPitShortcut() {
		super(new int[] { 9331, 9332 }, 38, 0.0, "climb");
	}

	@Override
	public void run(Player player, Scenery object, String option, boolean failed) {
		switch (object.getId()) {
		case 9331:
			ForceMovement.run(player, player.getLocation(), Location.create(3303, 3315, 0), ANIMATION, ANIMATION, Direction.EAST, 13).setEndAnimation(Animation.RESET);
			break;
		case 9332:
			ForceMovement.run(player, player.getLocation(), Location.create(3307, 3315, 0), SCALE, SCALE, Direction.EAST, 13).setEndAnimation(Animation.RESET);
			break;
		}
	}

	@Override
	public Location getDestination(Node node, Node n) {
		final Scenery object = (Scenery) n;
		if (object.getId() == 9331) {
			return object.getLocation().transform(1, 0, 0);
		}
		return null;
	}
}
