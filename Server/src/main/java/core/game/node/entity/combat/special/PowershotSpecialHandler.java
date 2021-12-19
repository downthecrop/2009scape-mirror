package core.game.node.entity.combat.special;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;
import rs09.game.node.entity.combat.handlers.RangeSwingHandler;

/**
 * Handles the Magic longbow special attack "Powershot".
 * @author Emperor
 */
@Initializable
public final class PowershotSpecialHandler extends RangeSwingHandler implements Plugin<Object> {

	/**
	 * The special energy required.
	 */
	private static final int SPECIAL_ENERGY = 35;

	/**
	 * The graphic.
	 */
	private static final Graphics GRAPHIC = new Graphics(250, 96);

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		CombatStyle.RANGE.getSwingHandler().register(859, this);
		CombatStyle.RANGE.getSwingHandler().register(10284, this);
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
		int hit = 0;
		if (isAccurateImpact(entity, victim, CombatStyle.RANGE, 1.98, 1.0)) {
			hit = RandomFunction.random(calculateHit(entity, victim, 1.0));
		}
		entity.asPlayer().getAudioManager().send(2536);
		state.setEstimatedHit(hit);
		Companion.useAmmo(entity, state, victim.getLocation());
		return 1 + (int) Math.ceil(entity.getLocation().getDistance(victim.getLocation()) * 0.3);
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		entity.visualize(state.getRangeWeapon().getAnimation(), GRAPHIC);
		int speed = (int) (46 + (entity.getLocation().getDistance(victim.getLocation()) * 5));
		Projectile.create(entity, victim, 249, 40, 36, 45, speed, 5, 11).send();
	}
}
