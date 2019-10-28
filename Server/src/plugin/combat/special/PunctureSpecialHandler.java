package plugin.combat.special;

import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.combat.CombatStyle;
import org.crandor.game.node.entity.combat.handlers.MeleeSwingHandler;
import org.crandor.game.node.entity.impl.Animator.Priority;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Handles the puncture special attack combat swing.
 * @author Emperor
 */
@InitializablePlugin
public final class PunctureSpecialHandler extends MeleeSwingHandler implements Plugin<Object> {

	/**
	 * The special energy required.
	 */
	private static final int SPECIAL_ENERGY = 25;

	/**
	 * The attack animation.
	 */
	private static final Animation ANIMATION = new Animation(1062, Priority.HIGH);

	/**
	 * The graphic.
	 */
	private static final Graphics GRAPHIC = new Graphics(252, 96);

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public void impact(Entity entity, Entity victim, BattleState state) {
		victim.getImpactHandler().handleImpact(entity, state.getEstimatedHit(), CombatStyle.MELEE, state);
		victim.getImpactHandler().handleImpact(entity, state.getSecondaryHit(), CombatStyle.MELEE, state, null, true);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		if (CombatStyle.MELEE.getSwingHandler().register(1215, this) && CombatStyle.MELEE.getSwingHandler().register(1231, this) && CombatStyle.MELEE.getSwingHandler().register(5680, this) && CombatStyle.MELEE.getSwingHandler().register(5698, this) && CombatStyle.MELEE.getSwingHandler().register(13465, this) && CombatStyle.MELEE.getSwingHandler().register(13466, this) && CombatStyle.MELEE.getSwingHandler().register(13467, this) && CombatStyle.MELEE.getSwingHandler().register(13468, this))
			;
		return this;
	}

	@Override
	public int swing(Entity entity, Entity victim, BattleState state) {
		if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
			return -1;
		}
		// First hit
		//double accuracyMod, double defenceMod
		int hit = 0;
		// 											 		accuracyMod defenceMod
		if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.05, 1.0)) {
			hit = RandomFunction.random(calculateHit(entity, victim, 1.1306));
		}
		state.setEstimatedHit(hit);
		// Second hit
		// 											 		accuracyMod defenceMod
		if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.05, 1.0)) {
			hit = RandomFunction.random(calculateHit(entity, victim, 1.1306));
		} else {
			hit = 0;
		}
		entity.asPlayer().getAudioManager().send(2537);
		state.setSecondaryHit(hit);
		return 1;
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		entity.visualize(ANIMATION, GRAPHIC);
	}
}
