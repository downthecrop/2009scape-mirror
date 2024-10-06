package content.global.handlers.item.equipment.special;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.SwingHandlerFlag;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;
import core.game.node.entity.combat.RangeSwingHandler;
import core.game.world.GameWorld;
import org.rs09.consts.Sounds;

import static core.api.ContentAPIKt.playGlobalAudio;

/**
 * Handles the magic shortbow special attack "Snapshot".
 * @author Emperor
 */
@Initializable
public final class SnapshotSpecialHandler extends RangeSwingHandler implements Plugin<Object> {

	/**
	 * Constructs a new {@code SnapshotSpecialHandler} {@code Object}.
	 */
	public SnapshotSpecialHandler() {
		super(SwingHandlerFlag.IGNORE_PRAYER_BOOSTS_DAMAGE);
	}

	/**
	 * The special energy required.
	 */
	private static final int SPECIAL_ENERGY = 55;

	/**
	 * The attack animation.
	 */
	private static final Animation ANIMATION = new Animation(1074, Priority.HIGH);

	/**
	 * The graphic.
	 */
	private static final Graphics GRAPHIC = new Graphics(249, 96);

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		CombatStyle.RANGE.getSwingHandler().register(861, this);
		return this;
	}

	@Override
	public int swing(Entity entity, Entity victim, BattleState state) {
		Player p = (Player) entity;
		configureRangeData(p, state);
		if (state.getWeapon() == null || !Companion.hasAmmo(entity, state)) {
			entity.getProperties().getCombatPulse().stop();
			p.getSettings().toggleSpecialBar();
			return -1;
		}
		if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
			return -1;
		}
        state.setStyle(CombatStyle.RANGE);
		int max = calculateHit(entity, victim, 1.0);
		state.setMaximumHit(max);
		int hit = 0;
		if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.43, 1.0)) {
			hit = RandomFunction.random(max + 1);
		}
		state.setEstimatedHit(hit);
		hit = 0;
		if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.43, 1.0)) {
			hit = RandomFunction.random(max + 1);
		}
		state.setSecondaryHit(hit);
		Companion.useAmmo(entity, state, victim.getLocation());
		return 1 + (int) Math.ceil(entity.getLocation().getDistance(victim.getLocation()) * 0.3);
	}

	@Override
	public void impact(final Entity entity, final Entity victim, final BattleState state) {
		int hit = state.getEstimatedHit();
		victim.getImpactHandler().handleImpact(entity, hit, CombatStyle.RANGE, state);
		if (state.getSecondaryHit() > -1) {
			final int hitt = state.getSecondaryHit();
			if (victim.getLocation().withinDistance(entity.getLocation(), 2)) {
				victim.getImpactHandler().handleImpact(entity, hitt, CombatStyle.RANGE, state);
				return;
			}
			GameWorld.getPulser().submit(new Pulse(1, victim) {
				@Override
				public boolean pulse() {
					victim.getImpactHandler().handleImpact(entity, hitt, CombatStyle.RANGE, state);
					return true;
				}
			});
		}
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		playGlobalAudio(entity.getLocation(), Sounds.SNAPSHOT_2545);
		entity.visualize(ANIMATION, GRAPHIC);
		int speed = (int) (27 + (entity.getLocation().getDistance(victim.getLocation()) * 5));
		Projectile.create(entity, victim, 249, 40, 36, 20, speed, 15, 11).send();
		speed = (int) (32 + (entity.getLocation().getDistance(victim.getLocation()) * 10));
		Projectile.create(entity, victim, 249, 40, 36, 50, speed, 15, 11).send();
	}
}
