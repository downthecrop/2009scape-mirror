package plugin.skill.agility.shortcuts;

import org.crandor.game.content.skill.member.agility.AgilityShortcut;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.impl.ForceMovement;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.map.Direction;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.plugin.InitializablePlugin;

/**
 * Handles the al kharid pit shortcut.
 * @author adam
 */
@InitializablePlugin
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
	public void run(Player player, GameObject object, String option, boolean failed) {
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
		final GameObject object = (GameObject) n;
		if (object.getId() == 9331) {
			return object.getLocation().transform(1, 0, 0);
		}
		return null;
	}
}
