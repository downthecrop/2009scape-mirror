package core.game.node.entity.skill.agility.shortcuts;

import core.cache.def.impl.SceneryDefinition;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.game.node.entity.skill.agility.AgilityShortcut;
import core.game.node.Node;
import core.game.node.entity.impl.ForceMovement;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;

/**
 * Handles the strange floor shortcut.
 * @author Vexia
 */
@Initializable
public class StrangeFloorShortcut extends AgilityShortcut {

	/**
	 * Represents the running animation.
	 */
	private static final Animation RUNNING_ANIM = new Animation(1995);

	/**
	 * Represents the jumping animation.
	 */
	private static final Animation JUMP_ANIM = new Animation(1603);

	/**
	 * Constructs a new {@Code StrangeFloorShortcut} {@Code
	 * Object}
	 */
	public StrangeFloorShortcut() {
		super(new int[] { 9294 }, 80, 0.0, "jump-over");
	}

	@Override
	public void run(final Player player, Scenery object, String option, boolean failed) {
		GameWorld.getPulser().submit(new Pulse(1, player) {
			@Override
			public boolean pulse() {
				player.getAnimator().forceAnimation(JUMP_ANIM);
				return true;
			}
		});
		GameWorld.getPulser().submit(new Pulse(2, player) {

			@Override
			public boolean pulse() {
				player.getAnimator().forceAnimation(RUNNING_ANIM);
				return true;
			}

		});
		ForceMovement.run(player, player.getLocation().getX() >= 2880 ? Location.create(2881, 9813, 0) : Location.create(2877, 9813, 0), player.getLocation().getX() >= 2880 ? Location.create(2877, 9813, 0) : Location.create(2881, 9813, 0), RUNNING_ANIM, 13);

	}

	@Override
	public Location getDestination(Node node, Node n) {
		return node.getLocation().getX() >= 2880 ? Location.create(2881, 9813, 0) : Location.create(2877, 9813, 0);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		configure(this);
		return this;
	}

	@Override
	public void configure(AgilityShortcut shortcut) {
		SceneryDefinition.forId(getIds()[0]).getHandlers().put("option:jump-over",this);
	}
}
