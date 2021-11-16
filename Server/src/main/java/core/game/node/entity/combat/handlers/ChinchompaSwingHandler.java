package core.game.node.entity.combat.handlers;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.InteractionType;
import core.game.node.entity.combat.equipment.Ammunition;
import core.game.node.entity.combat.equipment.RangeWeapon;
import core.game.node.entity.combat.equipment.Weapon;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Graphics;
import core.tools.RandomFunction;
import rs09.game.node.entity.combat.handlers.RangeSwingHandler;

import java.util.List;

/**
 * Handles a combat swing using red chinchompas.
 * @author Emperor
 */
public final class ChinchompaSwingHandler extends RangeSwingHandler {

	/**
	 * The instance.
	 */
	private static final ChinchompaSwingHandler INSTANCE = new ChinchompaSwingHandler();

	/**
	 * The impact graphic.
	 */
	private static final Graphics END_GRAPHIC = new Graphics(157, 96);

	/**
	 * Constructs a new {@code ChinchompaSwingHandler} {@code Object}.
	 */
	private ChinchompaSwingHandler() {
		super();
	}

	@Override
	public int swing(Entity entity, Entity victim, BattleState state) {
		boolean multi = entity.getProperties().isMultiZone() && victim.getProperties().isMultiZone();
		state.setStyle(CombatStyle.RANGE);
		if (!multi) {
			int ticks = super.swing(entity, victim, state);
			state.setTargets(new BattleState[] { state });
			return ticks;
		}
		Player p = (Player) entity;
		RangeWeapon rw = RangeWeapon.get(p.getEquipment().getNew(3).getId());
		if (rw == null) {
			return -1;
		}
		Weapon w = new Weapon(p.getEquipment().get(3), rw.getAmmunitionSlot(), p.getEquipment().getNew(rw.getAmmunitionSlot()));
		w.setType(rw.getWeaponType());
		state.setRangeWeapon(rw);
		state.setAmmunition(Ammunition.get(w.getAmmunition().getId()));
		state.setWeapon(w);
		if (!Companion.hasAmmo(entity, state)) {
			entity.getProperties().getCombatPulse().stop();
			return -1;
		}
		@SuppressWarnings("rawtypes")
		List list = victim instanceof NPC ? RegionManager.getSurroundingNPCs(victim, 14, entity) : RegionManager.getSurroundingPlayers(victim, 14, entity);
		BattleState[] targets = new BattleState[list.size()];
		int count = 0;
		for (Object o : list) {
			Entity e = (Entity) o;
			if (canSwing(entity, e) != InteractionType.NO_INTERACT) {
				BattleState s = targets[count++] = new BattleState(entity, e);
				s.setStyle(CombatStyle.RANGE);
				int hit = 0;
				if (isAccurateImpact(entity, e, CombatStyle.RANGE)) {
					hit = RandomFunction.random(calculateHit(entity, e, 1.0));
				}
				s.setEstimatedHit(hit);
			}
		}
		state.setTargets(targets);
		Companion.useAmmo(entity, state, null);
		return 2 + (int) Math.ceil(entity.getLocation().getDistance(victim.getLocation()) * 0.3);
	}

	@Override
	public void adjustBattleState(Entity entity, Entity victim, BattleState state) {
		for (BattleState s : state.getTargets()) {
			if (s != null) {
				super.adjustBattleState(entity, s.getVictim(), s);
			}
		}
	}

	@Override
	public void addExperience(Entity entity, Entity victim, BattleState state) {
		int hit = 0;
		for (BattleState s : state.getTargets()) {
			if (s != null && s.getEstimatedHit() > 0) {
				hit += s.getEstimatedHit();
			}
		}
		entity.getSkills().addExperience(Skills.HITPOINTS, hit * 1.33, true);
		if (entity.getProperties().getAttackStyle().getStyle() == WeaponInterface.STYLE_DEFENSIVE) {
			entity.getSkills().addExperience(Skills.RANGE, hit * 2, true);
			entity.getSkills().addExperience(Skills.DEFENCE, hit * 2, true);
		} else {
			entity.getSkills().addExperience(Skills.RANGE, hit * 4, true);
		}
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		Graphics start = null;
		if (state.getAmmunition() != null && state.getAmmunition().getProjectile() != null) {
			start = state.getAmmunition().getStartGraphics();
			if (victim != null) {
				state.getAmmunition().getProjectile().copy(entity, victim, 5).send();
			}
		}
		RangeWeapon weapon;
		int anim = entity.getProperties().getAttackAnimation().getId();
		if ((anim == 422 || anim == 423) && state.getWeapon().getId() > 0 && (weapon = RangeWeapon.get(state.getWeapon().getId())) != null) {
			entity.visualize(weapon.getAnimation(), start);
			return;
		}
		entity.visualize(entity.getProperties().getAttackAnimation(), start);
	}

	@Override
	public void impact(final Entity entity, final Entity victim, final BattleState state) {
		for (BattleState s : state.getTargets()) {
			if (s != null) {
				super.impact(entity, s.getVictim(), s);
			}
		}
	}

	@Override
	public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
		victim.graphics(END_GRAPHIC);
		for (BattleState s : state.getTargets()) {
			if (s != null) {
				s.getVictim().animate(s.getVictim().getProperties().getDefenceAnimation());
			}
		}
	}

	/**
	 * Gets the ChinchompaSwingHandler instance.
	 * @return The instance.
	 */
	public static ChinchompaSwingHandler getInstance() {
		return INSTANCE;
	}
}
