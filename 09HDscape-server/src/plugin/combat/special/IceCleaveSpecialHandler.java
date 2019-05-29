package plugin.combat.special;

import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.combat.CombatStyle;
import org.crandor.game.node.entity.combat.handlers.MeleeSwingHandler;
import org.crandor.game.node.entity.impl.Animator.Priority;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.state.EntityState;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Handles the Ice cleave special attack.
 * @author Emperor
 * @version 1.0
 */
@InitializablePlugin
public final class IceCleaveSpecialHandler extends MeleeSwingHandler implements Plugin<Object> {

	/**
	 * The special energy required.
	 */
	private static final int SPECIAL_ENERGY = 60;

	/**
	 * The attack animation.
	 */
	private static final Animation ANIMATION = new Animation(7070, Priority.HIGH);

	/**
	 * The graphic.
	 */
	private static final Graphics GRAPHIC = new Graphics(1221);

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		CombatStyle.MELEE.getSwingHandler().register(11700, this);
		CombatStyle.MELEE.getSwingHandler().register(13453, this);
		return this;
	}

	@Override
	public int swing(Entity entity, Entity victim, BattleState state) {
		if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
			return -1;
		}
		int hit = 0;
		if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.075, 0.98)) {
			hit = RandomFunction.random(calculateHit(entity, victim, 1.005));
		}
		state.setEstimatedHit(hit);
		entity.asPlayer().getAudioManager().send(3846);
		return 1;
	}

	@Override
	public void adjustBattleState(Entity entity, Entity victim, BattleState state) {
		super.adjustBattleState(entity, victim, state);
		if (state.getEstimatedHit() > 0) {
			victim.getStateManager().set(EntityState.FROZEN, 33);
			victim.graphics(Graphics.create(369));
		}
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		entity.visualize(ANIMATION, GRAPHIC);
	}
}
