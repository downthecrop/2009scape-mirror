package content.global.handlers.item.equipment.special;

import core.game.node.entity.skill.Skills;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.MeleeSwingHandler;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Plugin;
import core.plugin.Initializable;
import core.tools.RandomFunction;
import org.rs09.consts.Sounds;

import static core.api.ContentAPIKt.playGlobalAudio;

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
		if (!victim.getProperties().getCombatPulse().isAttacking() || isAccurateImpact(entity, victim, CombatStyle.MELEE)) {
			hit = RandomFunction.random(calculateHit(entity, victim, 1.0) + 1);
			if (victim.getSkills().getStaticLevel(Skills.DEFENCE) >= victim.getSkills().getDynamicLevels()[Skills.DEFENCE])
				victim.getSkills().updateLevel(Skills.DEFENCE, -hit, 0);
		}
		state.setEstimatedHit(hit);
		return 1;
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		playGlobalAudio(entity.getLocation(), Sounds.DTTD_BONE_DAGGER_STAB_1084);
		entity.visualize(ANIMATION, GRAPHIC);
	}
}
