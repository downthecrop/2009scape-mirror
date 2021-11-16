package core.game.node.entity.combat.special;

import core.plugin.Initializable;
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
import core.tools.RandomFunction;

/**
 * Handles Statius' Warhammer special attack - Smash.
 * @author Splinter
 * @version 1.0
 */
@Initializable
public final class SmashSpecialHandler extends MeleeSwingHandler implements Plugin<Object> {

	/**
	 * The special energy required.
	 */
	private static final int SPECIAL_ENERGY = 35;

	/**
	 * The attack animation.
	 */
	private static final Animation ANIMATION = new Animation(10501, Priority.HIGH);

	/**
	 * The gfx
	 */
	private static final Graphics GRAPHIC = new Graphics(1840, 0, 16);
	
	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		if (CombatStyle.MELEE.getSwingHandler().register(13902, this) && CombatStyle.MELEE.getSwingHandler().register(13904, this))
			;
		return this;
	}

	@Override
	public int swing(Entity entity, Entity victim, BattleState state) {
		if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
			return -1;
		}
        state.setStyle(CombatStyle.MELEE);
		int hit = 0;
		if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.0, 1.0)) {
			hit = RandomFunction.random(calculateHit(entity, victim, RandomFunction.random(1.0, 1.5)));
			int lower = (int) (victim.getSkills().getLevel(Skills.DEFENCE) * 0.30);
			victim.getSkills().updateLevel(Skills.DEFENCE, -lower, 0);
		}
		state.setEstimatedHit(hit);
		return 1;
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		entity.visualize(ANIMATION, GRAPHIC);
	}
}
