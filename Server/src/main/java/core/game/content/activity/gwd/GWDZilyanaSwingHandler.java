package core.game.content.activity.gwd;

import java.util.ArrayList;
import java.util.List;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import rs09.game.node.entity.combat.CombatSwingHandler;
import core.game.node.entity.combat.InteractionType;
import core.game.node.entity.combat.equipment.ArmourSet;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.npc.NPC;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.tools.RandomFunction;

/**
 * Handles the Commander Zilyana combat.
 * @author Emperor
 */
public class GWDZilyanaSwingHandler extends CombatSwingHandler {

	/**
	 * The melee attack animation.
	 */
	private static final Animation MELEE_ATTACK = new Animation(6964, Priority.HIGH);

	/**
	 * The magic attack animation.
	 */
	private static final Animation MAGIC_ATTACK = new Animation(6967, Priority.HIGH);

	/**
	 * The magic end graphic.
	 */
	private static final Graphics MAGIC_END_GRAPHIC = new Graphics(1207);

	/**
	 * Constructs a new {@code ZilyanaSwingHandler} {@Code Object}.
	 */
	public GWDZilyanaSwingHandler() {
		super(CombatStyle.MAGIC);
	}

	@Override
	public InteractionType canSwing(Entity entity, Entity victim) {
		return CombatStyle.MELEE.getSwingHandler().canSwing(entity, victim);
	}

	@Override
	public int swing(Entity entity, Entity victim, BattleState state) {
		BattleState[] targets;
		if (RandomFunction.randomize(10) < 7) {
			targets = new BattleState[] { state };
			setType(CombatStyle.MELEE);
			state.setStyle(CombatStyle.MELEE);
		} else {
			NPC npc = (NPC) entity;
			List<BattleState> list = new ArrayList<>(20);
			for (Entity t : RegionManager.getLocalPlayers(npc.getCenterLocation(), (npc.size() >> 1) + 2)) {
				if (t.getLocation().getX() < 2908 && t.isAttackable(npc, CombatStyle.MAGIC, false)) {
					list.add(new BattleState(entity, t));
				}
			}
			state.setTargets(targets = list.toArray(new BattleState[0]));
			state.setStyle(CombatStyle.MAGIC);
			setType(CombatStyle.MAGIC);
		}
		for (BattleState s : targets) {
			s.setStyle(state.getStyle());
			int hit = getType() == CombatStyle.MAGIC ? -1 : 0;
			if (isAccurateImpact(entity, s.getVictim())) {
				int max = calculateHit(entity, s.getVictim(), 1.0);
				s.setMaximumHit(max);
				hit = RandomFunction.random(max);
			}
			s.setEstimatedHit(hit);
		}
		return 1;
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		if(getType() != null) {
			switch (getType()) {
				case MELEE:
					entity.animate(MELEE_ATTACK);
					break;
				case MAGIC:
					entity.animate(MAGIC_ATTACK);
					break;
				default:
					break;
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
		else return -1;
	}

	@Override
	public void impact(Entity entity, Entity victim, BattleState state) {
		if(state != null)
			state.getStyle().getSwingHandler().impact(entity, victim, state);
	}

	@Override
	public void adjustBattleState(Entity entity, Entity victim, BattleState state) {
		state.getStyle().getSwingHandler().adjustBattleState(entity, victim, state);
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

	@Override
	public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
		if (state != null && state.getStyle() == CombatStyle.MAGIC) {
			for (BattleState s : state.getTargets()) {
				if (s.getEstimatedHit() > 0) {
					s.getVictim().graphics(MAGIC_END_GRAPHIC);
				}
			}
			return;
		}
		if(state != null)
			state.getStyle().getSwingHandler().visualizeImpact(entity, victim, state);
	}

}
