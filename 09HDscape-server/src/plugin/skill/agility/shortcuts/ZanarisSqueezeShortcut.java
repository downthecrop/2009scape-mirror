package plugin.skill.agility.shortcuts;

import org.crandor.game.content.skill.Skills;
import org.crandor.game.content.skill.member.agility.AgilityShortcut;
import org.crandor.game.node.entity.impl.ForceMovement;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.map.Direction;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.plugin.InitializablePlugin;

/**
 * Handles the zanaris cosmic altar shortcuts for runecrafters.
 * @author Splinter
 */
@InitializablePlugin
public class ZanarisSqueezeShortcut extends AgilityShortcut {

	/**
	 * Represents the squeeze animation.
	 */
	private static final Animation ANIMATION = new Animation(2240);

	/**
	 * Constructs a new {@Code ZanarisSqueezeShortcut} {@Code Object}
	 */
	public ZanarisSqueezeShortcut() {
		super(new int[] { 12127 }, 46, 0.0, "squeeze-past");
	}

	@Override
	public void run(Player player, GameObject object, String option, boolean failed) {
		if (player.getSkills().getLevel(Skills.AGILITY) < 66 && (object.getLocation().equals(new Location(2408, 4395)) || object.getLocation().equals(new Location(2415, 4402)))) {
			player.getDialogueInterpreter().sendDialogue("You need an Agility level of at least 66 to negotiate this obstacle.");
			return;
		}
		Location to = player.getLocation().getY() < object.getLocation().getY() ? object.getLocation().transform(0, 1, 0) : object.getLocation().transform(0, -1, 0);
		Direction dir = player.getLocation().getY() < object.getLocation().getY() ? Direction.NORTH : Direction.SOUTH;
		ForceMovement.run(player, player.getLocation(), to, ANIMATION, ANIMATION, dir, 13).setEndAnimation(Animation.RESET);
	}
}
