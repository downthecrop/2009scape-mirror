package org.crandor.game.node.entity.combat.handlers;

import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.*;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.SpellBookManager;

/**
 * Handles a combat swing using a trident.
 * @author Empathy
 */
public class TridentOfSeasHandler extends CombatSwingHandler {

	/**
	 * The Spell
	 */
	private static final CombatSpell SPELL = (CombatSpell) SpellBookManager.SpellBook.MODERN.getSpell(126);
	
	/**
	 * The instance.
	 */
	public static final TridentOfSeasHandler INSTANCE = new TridentOfSeasHandler();

	/**
	 * Constructs a new {@Code TridentOfSeas} {@Code Object}
	 * @param type the type.
	 */
	public TridentOfSeasHandler() {
		super(CombatStyle.MAGIC);
	}

	@Override
	public InteractionType canSwing(Entity entity, Entity victim) {
		if (victim instanceof Player) {
			if (victim.getZoneMonitor().isInZone("Wilderness")) {
				return InteractionType.NO_INTERACT;
			}
		}
		return CombatStyle.MAGIC.getSwingHandler().canSwing(entity, victim);
	}
	
	@Override
	public int swing(Entity entity, Entity victim, BattleState state) {
		if (entity instanceof Player) {
			Player player = entity.asPlayer();
			if (player.getProperties().getAutocastSpell() == null || player.getProperties().getAutocastSpell() != SPELL) {
				player.getProperties().setAutocastSpell(SPELL);
			}
		}
		return CombatStyle.MAGIC.getSwingHandler().swing(entity, victim, state);
	}

	@Override
	public void impact(Entity entity, Entity victim, BattleState state) {
		CombatStyle.MAGIC.getSwingHandler().impact(entity, victim, state);
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		CombatStyle.MAGIC.getSwingHandler().visualize(entity, victim, state);
	}

	@Override
	public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
		CombatStyle.MAGIC.getSwingHandler().visualizeImpact(entity, victim, state);
	}

	@Override
	public int calculateAccuracy(Entity entity) {
		return CombatStyle.MAGIC.getSwingHandler().calculateAccuracy(entity);
	}

	@Override
	public int calculateHit(Entity entity, Entity victim, double modifier) {
		return CombatStyle.MAGIC.getSwingHandler().calculateHit(entity, victim, modifier);
	}

	@Override
	public void addExperience(Entity entity, Entity victim, BattleState state) {
		SPELL.addExperience(entity, state.getEstimatedHit());
	}

	@Override
	public int calculateDefence(Entity entity, Entity attacker) {
		return CombatStyle.MAGIC.getSwingHandler().calculateDefence(entity, attacker);
	}

	@Override
	public double getSetMultiplier(Entity e, int skillId) {
		return CombatStyle.MAGIC.getSwingHandler().getSetMultiplier(e, skillId);
	}
}
