package plugin.skill.agility.shortcuts;

import org.crandor.game.content.skill.member.agility.AgilityHandler;
import org.crandor.game.content.skill.member.agility.AgilityShortcut;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.diary.DiaryType;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.map.Direction;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles a pipe shortcut.
 * @author Vexia
 */
@InitializablePlugin
public class PipeShortcut extends AgilityShortcut {

	/**
	 * Constructs a new {@Code PipeShortcut} {@Code Object}
	 */
	public PipeShortcut() {
		super(new int[] {}, 0, 0.0, "");
	}

	/**
	 * Constructs a new {@Code PipeShortcut} {@Code Object}
	 * @param ids the ids.
	 * @param level the level.
	 * @param experience the experience.
	 * @param options the options.
	 */
	public PipeShortcut(int[] ids, int level, double experience, String... options) {
		super(ids, level, experience, options);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) {
		configure(new PipeShortcut(new int[] { 9293 }, 70, 0.0, "squeeze-through"));
		configure(new PipeShortcut(new int[] { 29370 }, 51, 0.0, "squeeze-through"));
		configure(new PipeShortcut(new int[] { 2290 }, 49, 7.5, "squeeze-through"));
		return this;
	}

	@Override
	public void run(Player player, GameObject object, String option, boolean failed) {
		Direction dir = getObjectDirection(object.getDirection());
		if (object.getId() != 9293 && object.getId() != 2290) {
			dir = dir.getOpposite();
		}
		int offset = 0;
		if (object.getId() == 2290) {
			offset = 1;
		}
		if (object.getId() == 29370 && !player.getAchievementDiaryManager().getDiary(DiaryType.VARROCK).isComplete(2, 5)) {
			player.getAchievementDiaryManager().getDiary(DiaryType.VARROCK).updateTask(player, 2, 5, true);
		}
		final Location dest = player.getLocation().transform(dir, 3 + offset);
		player.lock(5);
		AgilityHandler.forceWalk(player, -1, player.getLocation(), dest, Animation.create(749), 10, 0, null);
		AgilityHandler.forceWalk(player, -1, dest, dest.transform(dir, 2 + offset), Animation.create(844), 10, 0, null, 3);
		AgilityHandler.forceWalk(player, 0, dest.transform(dir, 2), dest.transform(dir, 3), Animation.create(748), 20, getExperience(), null, 5);
	}

	@Override
	public Location getDestination(Node n, Node node) {
		Direction direction = getObjectDirection(node.asObject().getDirection());
		boolean negative = direction == Direction.EAST;
		int transform = 1;
		if (negative) {
			if (node.getId() == 9293) {
				transform = -1;
			} else {
				transform = 2;
			}
		}
		if (node.getId() == 9293 && direction == Direction.WEST) {
			transform = -2;
		} else if (node.getId() == 2290 && direction == Direction.WEST) {
			transform = -2;
		} else if (node.getId() == 2290 && direction == Direction.EAST) {
			return new Location(2572, 9506, 0);
		}
		return node.getLocation().transform(direction, transform);
	}
}
