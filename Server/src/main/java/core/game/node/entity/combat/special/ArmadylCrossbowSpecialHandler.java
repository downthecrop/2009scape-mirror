package core.game.node.entity.combat.special;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import rs09.game.node.entity.combat.handlers.RangeSwingHandler;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * Represents the new Armadyl C'bow special attack.
 * @author Splinter
 * @version 1.0
 */
@Initializable
public final class ArmadylCrossbowSpecialHandler extends RangeSwingHandler implements Plugin<Object> {

	/**
	 * The special energy required.
	 */
	private static final int SPECIAL_ENERGY = 50;

	/**
	 * The attack animation.
	 */
	private static final Animation ANIMATION = new Animation(4230, Priority.HIGH);

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		CombatStyle.RANGE.getSwingHandler().register(9185, this);
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
		if (isAccurateImpact(entity, victim, CombatStyle.RANGE, 2.0, 1.0)) {//2x the accuracy
			hit = RandomFunction.random(calculateHit(entity, victim, 1.0));
		}
		Companion.useAmmo(entity, state, victim.getLocation());
		state.setEstimatedHit(hit);
		return 1;
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		entity.animate(ANIMATION);
		Projectile.create(entity, victim, 698, 36, 25, 35, 50).send();
	}
}
