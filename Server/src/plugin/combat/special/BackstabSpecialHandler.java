package plugin.combat.special;

import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.combat.CombatStyle;
import org.crandor.game.node.entity.combat.handlers.MeleeSwingHandler;
import org.crandor.game.node.entity.impl.Animator.Priority;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Handles the Bone dagger special attack "Backstab".
 * @author Emperor
 */
@InitializablePlugin
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
