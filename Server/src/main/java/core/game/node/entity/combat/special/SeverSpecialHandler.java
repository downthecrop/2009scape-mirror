package core.game.node.entity.combat.special;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import rs09.game.node.entity.combat.handlers.MeleeSwingHandler;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.prayer.PrayerType;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Plugin;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * Handles the Sever special attack.
 * @author Emperor
 */
@Initializable
public final class SeverSpecialHandler extends MeleeSwingHandler implements Plugin<Object> {

	/**
	 * The special energy required.
	 */
	private static final int SPECIAL_ENERGY = 55;

	/**
	 * The attack animation.
	 */
	private static final Animation ANIMATION = new Animation(1872, Priority.HIGH);

	/**
	 * The graphic.
	 */
	private static final Graphics GRAPHIC = new Graphics(347, 96);

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		if (CombatStyle.MELEE.getSwingHandler().register(4587, this) && CombatStyle.MELEE.getSwingHandler().register(13477, this))
			;
		return this;
	}

	@Override
	public int swing(Entity entity, Entity victim, BattleState state) {
		if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY))
			return -1;
        state.setStyle(CombatStyle.MELEE);
		int hit = 0;
		if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.124, 1.0)) {
			hit = RandomFunction.random(calculateHit(entity, victim, 1.0));
			if (victim instanceof Player) {
				Player p = (Player) victim;
				if (p.getPrayer().get(PrayerType.PROTECT_FROM_MAGIC)) {
					p.getPrayer().toggle(PrayerType.PROTECT_FROM_MAGIC);
				}
				if (p.getPrayer().get(PrayerType.PROTECT_FROM_MELEE)) {
					p.getPrayer().toggle(PrayerType.PROTECT_FROM_MELEE);
				}
				if (p.getPrayer().get(PrayerType.PROTECT_FROM_MISSILES)) {
					p.getPrayer().toggle(PrayerType.PROTECT_FROM_MISSILES);
				}
				if (p.getPrayer().get(PrayerType.PROTECT_FROM_SUMMONING)) {
					p.getPrayer().toggle(PrayerType.PROTECT_FROM_SUMMONING);
				}
			}
		}
		entity.asPlayer().getAudioManager().send(2540);
		state.setEstimatedHit(hit);
		return 1;
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		entity.visualize(ANIMATION, GRAPHIC);
	}
}
