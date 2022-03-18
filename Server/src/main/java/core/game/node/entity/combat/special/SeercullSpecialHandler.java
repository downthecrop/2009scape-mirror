package core.game.node.entity.combat.special;

import core.game.node.entity.skill.Skills;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import rs09.game.node.entity.combat.handlers.RangeSwingHandler;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Plugin;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * Represents the Seercull's special attack which lowers the opponent's magic
 * level.
 * @author Splinter
 * @version 1.0
 */
@Initializable
public final class SeercullSpecialHandler extends RangeSwingHandler implements Plugin<Object> {

	/**
	 * The special energy required.
	 */
	private static final int SPECIAL_ENERGY = 100;

	/**
	 * The attack animation.
	 */
	private static final Graphics DRAWBACK_GFX = new Graphics(472, 96);

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		CombatStyle.RANGE.getSwingHandler().register(6724, this);
		return this;
	}

	@Override
	public int swing(Entity entity, Entity victim, BattleState state) {
		Player p = (Player) entity;
		configureRangeData(p, state);
		if (state.getWeapon() == null || !Companion.hasAmmo(entity, state)) {
			entity.getProperties().getCombatPulse().stop();
			p.getSettings().toggleSpecialBar();
			return -1;
		}
		if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
			return -1;
		}
		int hit = 0;
		if (isAccurateImpact(entity, victim, CombatStyle.RANGE, 1.05, 1.0)) {
			hit = RandomFunction.random(calculateHit(entity, victim, 1.0));
			victim.getSkills().updateLevel(Skills.MAGIC, -hit, 0);
		}
		Companion.useAmmo(entity, state, victim.getLocation());
		state.setEstimatedHit(hit);
		return 1;
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		victim.graphics(new Graphics(474));
		int speed = (int) (35 + (entity.getLocation().getDistance(victim.getLocation()) * 10));
		entity.visualize(entity.getProperties().getAttackAnimation(), DRAWBACK_GFX);
		Projectile.create(entity, victim, 473, 40, 40, 40, speed, 15, 11).send();
	}

	@Override
	public void impact(final Entity entity, final Entity victim, final BattleState state) {
		int hit = state.getEstimatedHit();
		victim.getImpactHandler().handleImpact(entity, hit, CombatStyle.RANGE, state);
	}
}
