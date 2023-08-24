package content.region.wilderness.handlers.revenants;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.equipment.SwitchAttack;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.prayer.PrayerType;
import core.game.world.map.zone.impl.WildernessZone;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.game.node.entity.combat.MultiSwingHandler;
import core.game.world.GameWorld;
import org.rs09.consts.Sounds;

import static core.api.ContentAPIKt.*;

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
					playGlobalAudio(victim.getLocation(), 4061, 0, 1, 10);
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
				if (attack.getStyle() == CombatStyle.RANGE && !hasTimerActive(victim, "frozen") && !hasTimerActive(victim, "frozen:immunity")) {
                                        registerTimer(victim, spawnTimer("frozen", 16, true));
					sendMessage((Player) victim, "The icy darts freeze your muscles!");
					playGlobalAudio(victim.getLocation(), 4059, 0, 1, 10);
				} else if (attack.getStyle() == CombatStyle.MAGIC) {
					int ticks = 500;
					if (victim.asPlayer().getPrayer().get(PrayerType.PROTECT_FROM_MAGIC)) {
						ticks /= 2;
					}
					if (hasTimerActive(victim, "teleblock")) {
						playGlobalAudio(victim.getLocation(), 4064, 0, 1, 10);
					} else {
                                                registerTimer (victim, spawnTimer("teleblock", ticks));
					}
				}
			}
		}
		if (!isPoisoned(victim) && (WildernessZone.getWilderness(entity) >= 50 || entity.getId() == 6998)) {
                        applyPoison(victim, entity, 6);
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
					playGlobalAudio(entity.getLocation(), Sounds.TELEPORTBLOCK_CAST_202, 0, 1, 10);
				} else if (attack.getStyle() == CombatStyle.RANGE) {
					playGlobalAudio(entity.getLocation(), 4062, 0, 1, 10);
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
