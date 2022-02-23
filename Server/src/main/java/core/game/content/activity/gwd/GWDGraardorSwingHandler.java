package core.game.content.activity.gwd;

import java.util.ArrayList;
import java.util.List;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import org.jetbrains.annotations.NotNull;
import rs09.game.node.entity.combat.CombatSwingHandler;
import core.game.node.entity.combat.InteractionType;
import core.game.node.entity.combat.equipment.ArmourSet;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.npc.NPC;
import core.game.world.map.RegionManager;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.update.flag.context.Animation;
import core.tools.RandomFunction;

/**
 * Handles General Graardor's combat.
 * @author Emperor
 */
public final class GWDGraardorSwingHandler extends CombatSwingHandler {

	/**
	 * The boss chamber.
	 */
	private static final ZoneBorders CHAMBER = new ZoneBorders(2864, 5351, 2876, 5369);

	/**
	 * The melee attack animation.
	 */
	private static final Animation MELEE_ATTACK = new Animation(7060, Priority.HIGH);

	/**
	 * The range attack animation.
	 */
	private static final Animation RANGE_ATTACK = new Animation(7063, Priority.HIGH);

	/**
	 * Constructs a new {@code GWDGraardorSwingHandler} {@Code Object}.
	 */
	public GWDGraardorSwingHandler() {
		super(CombatStyle.MELEE);
	}

	@Override
	public InteractionType canSwing(Entity entity, Entity victim) {
		return CombatStyle.MELEE.getSwingHandler().canSwing(entity, victim);
	}

	@Override
	public int swing(Entity entity, Entity victim, BattleState state) {
		int ticks = 1;
		if (RandomFunction.randomize(10) < 7) {
			int hit = 0;
			if (CombatStyle.MELEE.getSwingHandler().isAccurateImpact(entity, victim, CombatStyle.MELEE)) {
				int max = CombatStyle.MELEE.getSwingHandler().calculateHit(entity, victim, 1.0);
				hit = RandomFunction.random(max);
				state.setMaximumHit(max);
			}
			state.setEstimatedHit(hit);
			state.setStyle(CombatStyle.MELEE);
		} else {
			ticks += (int) Math.ceil(entity.getLocation().getDistance(victim.getLocation()) * 0.3);
			NPC npc = (NPC) entity;
			List<BattleState> list = new ArrayList<>(20);
			for (Entity t : RegionManager.getLocalPlayers(npc, 28)) {
				if (!CHAMBER.insideBorder(t.getLocation())) {
					continue;
				}
				if (t.isAttackable(npc, CombatStyle.RANGE, false)) {
					list.add(new BattleState(entity, t));
				}
			}
			BattleState[] targets;
			state.setStyle(CombatStyle.RANGE);
			state.setTargets(targets = list.toArray(new BattleState[0]));
			for (BattleState s : targets) {
				s.setStyle(CombatStyle.RANGE);
				int hit = 0;
				if (CombatStyle.RANGE.getSwingHandler().isAccurateImpact(entity, s.getVictim(), CombatStyle.RANGE)) {
					int max = CombatStyle.RANGE.getSwingHandler().calculateHit(entity, s.getVictim(), 1.0);
					s.setMaximumHit(max);
					hit = RandomFunction.random(max);
				}
				s.setEstimatedHit(hit);
			}
		}
		return ticks;
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		if(state != null) {
			if (state.getStyle() == CombatStyle.MELEE) {
				entity.animate(MELEE_ATTACK);
			} else {
				entity.animate(RANGE_ATTACK);
				for (BattleState s : state.getTargets()) {
					Projectile.ranged(entity, s.getVictim(), 1200, 0, 0, 46, 1).send();
				}
			}
		}
	}

	@Override
	public ArmourSet getArmourSet(Entity e) {
		if(getType() != null)
			return getType().getSwingHandler().getArmourSet(e);
		else return ArmourSet.AHRIM;
	}

	@Override
	public double getSetMultiplier(Entity e, int skillId) {
		if(getType() != null)
			return getType().getSwingHandler().getSetMultiplier(e, skillId);
		else return 0.0;
	}

	@Override
	public void impact(Entity entity, Entity victim, BattleState state) {
		if (state != null && state.getStyle() != null && state.getStyle() == CombatStyle.MELEE) {
			state.getStyle().getSwingHandler().impact(entity, victim, state);
			return;
		}
		if(state != null && state.getTargets() != null) {
			for (BattleState s : state.getTargets()) {
				if (s == null || s.getEstimatedHit() < 0) {
					continue;
				}
				int hit = s.getEstimatedHit();
				s.getVictim().getImpactHandler().handleImpact(entity, hit, CombatStyle.RANGE, s);
			}
		}
	}

	@Override
	public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
		if (state.getStyle() == CombatStyle.MELEE) {
			victim.animate(victim.getProperties().getDefenceAnimation());
			return;
		}
		for (BattleState s : state.getTargets()) {
			s.getVictim().animate(s.getVictim().getProperties().getDefenceAnimation());
		}
	}

	@Override
	public void adjustBattleState(Entity entity, Entity victim, BattleState state) {
		if (state.getStyle() == CombatStyle.MELEE) {
			state.getStyle().getSwingHandler().adjustBattleState(entity, victim, state);
			return;
		}
		for (BattleState s : state.getTargets()) {
			CombatStyle.RANGE.getSwingHandler().adjustBattleState(entity, s.getVictim(), s);
		}
	}

	@Override
	public int calculateAccuracy(Entity entity) {
		if(getType() != null)
			return getType().getSwingHandler().calculateAccuracy(entity);
		else return -1;
	}

	@Override
	public int calculateDefence(Entity victim, Entity attacker) {
		if(getType() != null)
			return getType().getSwingHandler().calculateDefence(victim, attacker);
		else return -1;
	}

	@Override
	public int calculateHit(Entity entity, Entity victim, double modifier) {
		if(getType() != null)
			return getType().getSwingHandler().calculateHit(entity, victim, modifier);
		else return -1;
	}

}
