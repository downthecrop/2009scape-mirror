package plugin.combat.special;

import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.combat.CombatStyle;
import org.crandor.game.node.entity.combat.handlers.MeleeSwingHandler;
import org.crandor.game.node.entity.impl.Animator.Priority;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.audio.Audio;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Handles the Abyssal whip's Energy drain special attack.
 * @author Emperor
 * @version 1.0
 */
@InitializablePlugin
public final class EnergyDrainSpecialHandler extends MeleeSwingHandler implements Plugin<Object> {

	/**
	 * The special energy required.
	 */
	private static final int SPECIAL_ENERGY = 50;

	/**
	 * The attack animation.
	 */
	private static final Animation ANIMATION = new Animation(1658, Priority.HIGH);

	/**
	 * The graphic.
	 */
	private static final Graphics GRAPHIC = new Graphics(341, 96);

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		CombatStyle.MELEE.getSwingHandler().register(4151, this);
		return this;
	}

	@Override
	public int swing(Entity entity, Entity victim, BattleState state) {
		if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
			return -1;
		}
		int hit = 0;
		if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.2, 1.0)) {
			hit = RandomFunction.random(calculateHit(entity, victim, 1));
		}
		if (victim instanceof Player) {
			((Player) victim).getSettings().updateRunEnergy(10);
			((Player) entity).getSettings().updateRunEnergy(-10);
		}
		state.setEstimatedHit(hit);
		entity.asPlayer().getAudioManager().send(new Audio(2713), true);
		return 1;
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		entity.animate(ANIMATION);
		victim.graphics(GRAPHIC);
	}
}
