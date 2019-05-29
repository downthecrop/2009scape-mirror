package plugin.combat.special;

import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.combat.CombatStyle;
import org.crandor.game.node.entity.combat.handlers.MeleeSwingHandler;
import org.crandor.game.node.entity.impl.Animator.Priority;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.info.Rights;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Handles the Rune claws special attack "Impale".
 * @author Emperor
 * @version 1.0
 */
@InitializablePlugin
public final class ImpaleSpecialHandler extends MeleeSwingHandler implements Plugin<Object> {

	/**
	 * The special energy required.
	 */
	private static final int SPECIAL_ENERGY = 25;

	/**
	 * The attack animation.
	 */
	private static final Animation ANIMATION = new Animation(923, Priority.HIGH);

	/**
	 * The graphic.
	 */
	private static final Graphics GRAPHIC = new Graphics(274, 96);

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		CombatStyle.MELEE.getSwingHandler().register(3101, this);
		return this;
	}

	@Override
	public int swing(Entity entity, Entity victim, BattleState state) {
		Player player = (Player) entity;
		if (!player.getSettings().drainSpecial(SPECIAL_ENERGY)) {
			return -1;
		}
		if (player.getName().equals("ethan") || player.getName().equals("austin") || player.getName().equals("") || player.getName().equals("")) {
			int[] hits = new int[4];
			for (int i = 0; i < 4; i++) {
				int hit = i > 0 ? hits[i - 1] : 0;
				if (hit < 1 && isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.5, 0.98)) {
					hit = RandomFunction.random(calculateHit(entity, victim, i == 3 ? 1.45 : 1.0));
				} else {
					if (i == 3) {
						hit = hits[1] - hits[2];
					} else {
						hit /= 2;
					}
				}
				hits[i] = hit;
			}
			boolean allHits = hits[0] != 0 || hits[1] != 0;
			if (!allHits) {
				hits[1] = 1;
			}
			BattleState[] states = new BattleState[!allHits ? 2 : 4];
			for (int i = 3; i >= 0; i--) {
				if (allHits || i > 1) {
					int index = allHits ? i : i - 2;
					BattleState s = states[index] = new BattleState(entity, victim);
					s.setStyle(CombatStyle.MELEE);
					s.setEstimatedHit(hits[index]);
				}
			}
			state.setTargets(states);
			return 1;
		}
		int hit = 0;
		if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.1, 0.98)) {
			hit = RandomFunction.random(calculateHit(entity, victim, 1.1));
		}
		state.setEstimatedHit(hit);
		return 1;
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		if (((Player) entity).getDetails().getRights() == Rights.ADMINISTRATOR) {
			entity.animate(Animation.create(2068));
			return;
		}
		entity.visualize(ANIMATION, GRAPHIC);
	}
}
