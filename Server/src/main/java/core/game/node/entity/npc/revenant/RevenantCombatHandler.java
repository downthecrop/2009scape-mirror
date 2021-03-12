package core.game.node.entity.npc.revenant;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.equipment.SwitchAttack;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.prayer.PrayerType;
import core.game.node.entity.state.EntityState;
import core.game.world.map.zone.impl.WildernessZone;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import rs09.game.node.entity.combat.handlers.MultiSwingHandler;
import rs09.game.world.GameWorld;

/**
 * Handles the multi swing combat handler for revenants.
 * @author Vexia
 */
public class RevenantCombatHandler extends MultiSwingHandler {

	/**
	 * The magic graphics.
	 */
	private static final Graphics MAGIC_GRAPHIC = Graphics.create(1276);

	/**
	 * The range graphics.
	 */
	private static final Graphics RANGE_GRAPHIC = Graphics.create(1278);

	/**
	 * Constructs a new {@code RevenantCombatHandler} {@code Object}
	 */
	public RevenantCombatHandler(Animation meleeAnimation, Animation magicAnimation, Animation rangeAnimation) {
		super(true, new SwitchAttack(CombatStyle.MELEE.getSwingHandler(), meleeAnimation), new SwitchAttack(CombatStyle.RANGE.getSwingHandler(), rangeAnimation, createProjectile(RANGE_GRAPHIC)), new SwitchAttack(CombatStyle.MAGIC.getSwingHandler(), magicAnimation, createProjectile(MAGIC_GRAPHIC)));
	}

	@Override
	public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
		if (victim instanceof Player) {
			SwitchAttack attack = getCurrent();
			if (attack != null) {
				if (attack.getStyle() == CombatStyle.RANGE) {
					victim.asPlayer().getAudioManager().send(4061, true);
				}
			}
		}
		super.visualizeImpact(entity, victim, state);
	}

	@Override
	public void impact(Entity entity, Entity victim, BattleState state) {
		if (victim instanceof Player) {
			SwitchAttack attack = getCurrent();
			if (attack != null) {
				if (attack.getStyle() == CombatStyle.RANGE && victim.getAttribute("freeze_immunity", -1) < GameWorld.getTicks()) {
					victim.getStateManager().set(EntityState.FROZEN, 16, "The icy darts freeze your muscles!");
					victim.asPlayer().getAudioManager().send(4059, true);
				} else if (attack.getStyle() == CombatStyle.MAGIC) {
					int ticks = 500;
					if (victim.asPlayer().getPrayer().get(PrayerType.PROTECT_FROM_MAGIC)) {
						ticks /= 2;
					}
					if (victim.getStateManager().hasState(EntityState.TELEBLOCK)) {
						victim.asPlayer().getAudioManager().send(4064, true);
					} else {
						victim.getStateManager().set(EntityState.TELEBLOCK, ticks);
					}
				}
			}
		}
		if (!victim.getStateManager().hasState(EntityState.POISONED) && (WildernessZone.getWilderness(entity) >= 50 || entity.getId() == 6998)) {
			victim.getStateManager().register(EntityState.POISONED, false, 68, entity);
		}
		super.impact(entity, victim, state);
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		super.visualize(entity, victim, state);
		if (victim.isPlayer()) {
			SwitchAttack attack = getCurrent();
			if (attack != null) {
				if (attack.getStyle() == CombatStyle.MAGIC) {
					victim.asPlayer().getAudioManager().send(202, true);
				} else if (attack.getStyle() == CombatStyle.RANGE) {
					victim.asPlayer().getAudioManager().send(4062, true);
				}
			}
		}
	}

	/**
	 * Creates a projectile with a graphic id.
	 * @param graphic the graphic.
	 * @return the projectile.
	 */
	public static Projectile createProjectile(Graphics graphic) {
		return Projectile.create(null, null, graphic.getId(), 48, 36, 34, 20);
	}
}
