package core.game.node.entity.combat.special;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import rs09.game.node.entity.combat.handlers.MeleeSwingHandler;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.Player;
import core.game.node.entity.state.EntityState;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.Point;
import core.game.world.map.path.Pathfinder;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the dragon spear special attack.
 * @author Emperor
 */
@Initializable
public final class ShoveSpecialHandler extends MeleeSwingHandler implements Plugin<Object> {

	/**
	 * The special energy required.
	 */
	private static final int SPECIAL_ENERGY = 25;

	/**
	 * The attack animation.
	 */
	private static final Animation ANIMATION = new Animation(1064, Priority.HIGH);

	/**
	 * The graphic.
	 */
	private static final Graphics GRAPHIC = new Graphics(253, 96);

	/**
	 * The stun animation.
	 */
	private static Animation STUN_ANIM = new Animation(424);

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		CombatStyle.MELEE.getSwingHandler().register(1249, this);
		CombatStyle.MELEE.getSwingHandler().register(1263, this);
		CombatStyle.MELEE.getSwingHandler().register(3176, this);
		CombatStyle.MELEE.getSwingHandler().register(5716, this);
		CombatStyle.MELEE.getSwingHandler().register(5730, this);
		CombatStyle.MELEE.getSwingHandler().register(11716, this);
		CombatStyle.MELEE.getSwingHandler().register(14662, this);
		return this;
	}

	@Override
	public int swing(Entity entity, final Entity victim, BattleState state) {
		if (victim.size() > 1) {
			((Player) entity).getPacketDispatch().sendMessage("That creature is too large to knock back!");
			((Player) entity).getSettings().toggleSpecialBar();
			return -1;
		}
		if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
			return -1;
		}
		state.setEstimatedHit(-1);
		Direction dir = null;
		int vx = victim.getLocation().getX();
		int vy = victim.getLocation().getY();
		int sx = entity.getLocation().getX();
		int sy = entity.getLocation().getY();
		if (vx == sx && vy > sy) {
			dir = Direction.NORTH;
		} else if (vx == sx && vy < sy) {
			dir = Direction.SOUTH;
		} else if (vx > sx && vy == sy) {
			dir = Direction.EAST;
		} else if (vx < sx && vy == sy) {
			dir = Direction.WEST;
		} else if (vx > sx && vy > sy) {
			dir = Direction.NORTH_EAST;
		} else if (vx < sx && vy > sy) {
			dir = Direction.NORTH_WEST;
		} else if (vx > sx && vy < sy) {
			dir = Direction.SOUTH_EAST;
		} else if (vx < sx && vy < sy) {
			dir = Direction.SOUTH_WEST;
		}
		victim.getWalkingQueue().reset();
		victim.getPulseManager().clear();
		victim.animate(STUN_ANIM);
		victim.getStateManager().set(EntityState.STUNNED, 5);
		if (dir != null) {
			Point p = Direction.getWalkPoint(dir);
			Location dest = victim.getLocation().transform(p.getX(), p.getY(), 0);
			if (Pathfinder.find(victim, dest, false, Pathfinder.DUMB).isSuccessful()) {
				victim.getWalkingQueue().addPath(dest.getX(), dest.getY());
			}
		}
		entity.asPlayer().getAudioManager().send(2533);
		return 1;
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		entity.visualize(ANIMATION, GRAPHIC);
	}

	@Override
	public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
	}

	@Override
	public void impact(Entity entity, Entity victim, BattleState state) {
	}

}
