package plugin.skill.agility.shortcuts;

import org.crandor.game.content.skill.member.agility.AgilityHandler;
import org.crandor.game.content.skill.member.agility.AgilityShortcut;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles the log balance shortcut.
 * @author Vexia
 */
@InitializablePlugin
public class LogBalanceShortcut extends AgilityShortcut {

	/**
	 * The start and ending location.
	 */
	private Location start;

	/**
	 * The ending and start location.
	 */
	private Location end;

	/**
	 * Constructs a new {@Code LogBalanceShortcut} {@Code Object}
	 * @param ids the ids.
	 * @param level the level.
	 * @param experience the experience.
	 * @param options the options.
	 */
	public LogBalanceShortcut(int[] ids, int level, double experience, Location start, Location end, String... options) {
		super(ids, level, experience, options);
		this.start = start;
		this.end = end;
	}

	/**
	 * Constructs a new {@Code LogBalanceShortcut} {@Code Object}
	 */
	public LogBalanceShortcut() {
		super(new int[] {}, 0, 0.0);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) {
		configure(new LogBalanceShortcut(new int[] { 2296 }, 20, 5, new Location(2598, 3477, 0), Location.create(2603, 3477, 0), "walk-across"));
		configure(new LogBalanceShortcut(new int[] { 9322, 9324 }, 48, 1, Location.create(2722, 3592, 0), Location.create(2722, 3596, 0), "walk-across"));
		configure(new LogBalanceShortcut(new int[] { 35997, 35999 }, 33, 1, Location.create(2602, 3336, 0), Location.create(2598, 3336, 0), "walk-across"));
		configure(new LogBalanceShortcut(new int[] { 2332 }, 1, 1, Location.create(2910, 3049, 0), Location.create(2906, 3049, 0), "cross"));
		return this;
	}

	@Override
	public void run(Player player, GameObject object, String option, boolean failed) {
		Location destination = start;
		if (player.getLocation().getDistance(start) < player.getLocation().getDistance(end)) {
			destination = end;
		}
		AgilityHandler.walk(player, -1, player.getLocation(), destination, Animation.create(155), getExperience(), null);
	}

	@Override
	public Location getDestination(Node node, Node n) {
		if (node.getLocation().getDistance(start) < node.getLocation().getDistance(end)) {
			return start;
		}
		return end;
	}

}
