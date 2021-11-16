package core.game.node.entity.combat.special;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import rs09.game.node.entity.combat.handlers.MeleeSwingHandler;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.audio.Audio;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Plugin;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * Handles the healing blade special attack.
 * @author Emperor
 * @version 1.0
 */
@Initializable
public final class HealingBladeSpecialHandler extends MeleeSwingHandler implements Plugin<Object> {

	/**
	 * The special energy required.
	 */
	private static final int SPECIAL_ENERGY = 50;

	/**
	 * The attack animation.
	 */
	private static final Animation ANIMATION = new Animation(7071, Priority.HIGH);

	/**
	 * The graphic.
	 */
	private static final Graphics GRAPHIC = new Graphics(1220);

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		CombatStyle.MELEE.getSwingHandler().register(11698, this);
		CombatStyle.MELEE.getSwingHandler().register(13452, this);
		return this;
	}

	@Override
	public int swing(Entity entity, Entity victim, BattleState state) {
		if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
			return -1;
		}
        state.setStyle(CombatStyle.MELEE);
		int hit = 0;
		if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.12, 0.98)) {
			hit = RandomFunction.random(calculateHit(entity, victim, 1.005));
		}
		state.setEstimatedHit(hit);
		int healthRestore = hit / 2;
		double prayerRestore = hit * 0.25;
		if (healthRestore < 10) {
			healthRestore = 10;
		}
		if (prayerRestore < 5) {
			prayerRestore = 5;
		}
		entity.getSkills().heal(healthRestore);
		entity.getSkills().incrementPrayerPoints(prayerRestore);
		entity.asPlayer().getAudioManager().send(new Audio(3857), true);
		return 1;
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		entity.visualize(ANIMATION, GRAPHIC);
	}

}
