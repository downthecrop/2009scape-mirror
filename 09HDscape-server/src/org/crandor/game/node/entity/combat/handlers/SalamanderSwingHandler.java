package org.crandor.game.node.entity.combat.handlers;

import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.combat.CombatStyle;
import org.crandor.game.node.entity.combat.CombatSwingHandler;
import org.crandor.game.node.entity.combat.InteractionType;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;

/**
 * Handles a combat swing using a salamander.
 * @author Vexia
 */
public class SalamanderSwingHandler extends CombatSwingHandler {

	/**
	 * The instance for the swing handler.
	 */
	public static final SalamanderSwingHandler INSTANCE = new SalamanderSwingHandler(CombatStyle.MELEE);

	/**
	 * The current combat style.
	 */
	private CombatStyle style;

	/**
	 * Constructs a new {@Code SalamanderSwingHandler} {@Code
	 * Object}
	 * @param type the type.
	 */
	public SalamanderSwingHandler(CombatStyle type) {
		super(type);
		style = type;
	}

	@Override
	public int swing(Entity entity, Entity victim, BattleState state) {
		int index = entity.getProperties().getAttackStyle().getStyle();
		if (index == 7) {
			style = CombatStyle.MAGIC;
		} else if (index == 4) {
			style = CombatStyle.RANGE;
		} else {
			style = CombatStyle.MELEE;
		}
		return style.getSwingHandler().swing(entity, victim, state);
	}

	@Override
	public void impact(Entity entity, Entity victim, BattleState state) {
		style.getSwingHandler().impact(entity, victim, state);
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		style.getSwingHandler().visualize(entity, victim, state);
		entity.visualize(Animation.create(5247), new Graphics(952, 100));
	}

	@Override
	public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
		style.getSwingHandler().visualizeImpact(entity, victim, state);
	}

	@Override
	public int calculateAccuracy(Entity entity) {
		return style.getSwingHandler().calculateAccuracy(entity);
	}

	@Override
	public int calculateHit(Entity entity, Entity victim, double modifier) {
		return style.getSwingHandler().calculateHit(entity, victim, modifier);
	}

	@Override
	public void addExperience(Entity entity, Entity victim, BattleState state) {
		entity.getSkills().addExperience(Skills.HITPOINTS, state.getEstimatedHit() * 1.33, true);
		if (state.getStyle().equals(CombatStyle.MAGIC)) {
			entity.getSkills().addExperience(Skills.MAGIC, state.getEstimatedHit() * 2, true);
		}
		if (state.getStyle().equals(CombatStyle.RANGE)) {
			entity.getSkills().addExperience(Skills.RANGE, state.getEstimatedHit() * 4, true);
		}
		if (state.getStyle().equals(CombatStyle.MELEE)) {
			entity.getSkills().addExperience(Skills.STRENGTH, state.getEstimatedHit() * 4, true);
		}
	}

	@Override
	public int calculateDefence(Entity entity, Entity attacker) {
		return style.getSwingHandler().calculateDefence(entity, attacker);
	}

	@Override
	public double getSetMultiplier(Entity e, int skillId) {
		return style.getSwingHandler().getSetMultiplier(e, skillId);
	}

	@Override
	public InteractionType canSwing(Entity entity, Entity victim) {
		return style.getSwingHandler().canSwing(entity, victim);
	}
}
