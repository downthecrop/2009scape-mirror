package core.game.node.entity.combat.special;

import core.game.node.entity.skill.Skills;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import rs09.game.node.entity.combat.handlers.MeleeSwingHandler;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Plugin;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * Handles the Bone dagger special attack "Backstab".
 * @author Emperor
 */
@Initializable
public final class BackstabSpecialHandler extends MeleeSwingHandler implements Plugin<Object> {

	/**
	 * The special energy required.
	 */
	private static final int SPECIAL_ENERGY = 75;

	/**
	 * The attack animation.
	 */
	private static final Animation ANIMATION = new Animation(4198, Priority.HIGH);

	/**
	 * The graphic.
	 */
	private static final Graphics GRAPHIC = new Graphics(704);

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		CombatStyle.MELEE.getSwingHandler().register(8872, this);
		CombatStyle.MELEE.getSwingHandler().register(8874, this);
		CombatStyle.MELEE.getSwingHandler().register(8876, this);
		CombatStyle.MELEE.getSwingHandler().register(8878, this);
		return this;
	}

	@Override
	public int swing(Entity entity, Entity victim, BattleState state) {
		if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
			return -1;
		}
        state.setStyle(CombatStyle.MELEE);
		int hit = 0;
		double accuracy = 1.0;
		if (!victim.getProperties().getCombatPulse().isAttacking()) {
			accuracy = 1.75;
		}
		if (isAccurateImpact(entity, victim, CombatStyle.MELEE, accuracy, 0.98)) {
			hit = RandomFunction.random(calculateHit(entity, victim, 1.0));
			victim.getSkills().updateLevel(Skills.DEFENCE, -hit / 10, 0);
		}
		state.setEstimatedHit(hit);
		return 1;
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		entity.visualize(ANIMATION, GRAPHIC);
	}
}
