package core.game.node.entity.combat.special;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import rs09.game.node.entity.combat.handlers.MeleeSwingHandler;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;

/**
 * Handles the Dragon claws special attack "Slice and Dice".
 * @author Emperor
 *
 */
@Initializable
public final class SliceAndDiceSpecialHandler extends MeleeSwingHandler implements Plugin<Object> {

	/**
	 * The special energy required.
	 */
	private static final int SPECIAL_ENERGY = 50;

	/**
	 * The attack animation.
	 */
	private static final Animation ANIMATION = new Animation(10961, Priority.HIGH);

	/**
	 * The graphic.
	 */
	private static final Graphics GRAPHIC = new Graphics(1950);

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		CombatStyle.MELEE.getSwingHandler().register(14484, this);
		CombatStyle.MELEE.getSwingHandler().register(14486, this);
		return this;
	}

	@Override
	public int swing(Entity entity, Entity victim, BattleState state) {
		if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
			return -1;
		}
		int maximum = calculateHit(entity, victim, 1.0);
		int[] hits = new int[] {0, 1};
		int hit = getHit(entity, victim, maximum);
		if (hit > 0) {
			hits = new int[] {hit, hit / 2, (hit / 2) / 2, (hit / 2) - ((hit / 2) / 2)};
		} else {
			hit = getHit(entity, victim, maximum);
			if (hit > 0) {
				hits = new int[] {0, hit, hit / 2, hit - (hit / 2)};
			} else {
				hit = getHit(entity, victim, maximum);
				if (hit > 0) {
					hits = new int[] {0, 0, hit / 2, (hit / 2) + 10};
				} else {
					hit = getHit(entity, victim, (int) (maximum * 1.5));
					if (hit > 0) {
						hits = new int[] {0, 0, 0, hit};
					} else {
						hits = new int[] {0, RandomFunction.random(2)};
					}
				}
			}
		}
		BattleState[] states = new BattleState[hits.length];
		for (int i = 0; i < hits.length; i++) {
			BattleState s = states[i] = new BattleState(entity, victim);
			s.setStyle(CombatStyle.MELEE);
			s.setEstimatedHit(hits[i]);
		}
		state.setTargets(states);
		return 1;
	}
	
	/**
	 * Gets the current hit.
	 * @param entity The attacking entity.
	 * @param victim The victim.
	 * @param maximum The maximum hit.
	 * @return The hit.
	 */
	private int getHit(Entity entity, Entity victim, int maximum) {
		if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.25, 0.98)) {
			return RandomFunction.random(maximum);
		}
		return 0;
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		entity.visualize(ANIMATION, GRAPHIC);
	}
}
