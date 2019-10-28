package plugin.combat.special;

import org.crandor.game.content.skill.Skills;
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
 * Represents the dragon axe's clobbrer special handler.
 * @author Emperor
 * @version 1.0
 */
@InitializablePlugin
public final class ClobberSpecialHandler extends MeleeSwingHandler implements Plugin<Object> {

	/**
	 * The special energy required.
	 */
	private static final int SPECIAL_ENERGY = 100;

	/**
	 * The attack animation.
	 */
	private static final Animation ANIMATION = new Animation(2876, Priority.HIGH);

	/**
	 * The graphic.
	 */
	private static final Graphics GRAPHIC = new Graphics(479, 96);

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		CombatStyle.MELEE.getSwingHandler().register(6739, this);
		return this;
	}

	@Override
	public int swing(Entity entity, Entity victim, BattleState state) {
		if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
			return -1;
		}
		int hit = 0;
		if (entity instanceof Player) {
			entity.asPlayer().sendChat("Chop chop!");
		}
		if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.05, 1.0)) {
			hit = RandomFunction.random(calculateHit(entity, victim, 1.0));
			if (hit > 9) {
				int amount = hit / 10;
				victim.getSkills().updateLevel(Skills.DEFENCE, -amount, 0);
				victim.getSkills().updateLevel(Skills.MAGIC, -amount, 0);
			}
		}
		entity.getSkills().updateLevel(Skills.WOODCUTTING, 3);
		state.setEstimatedHit(hit);
		return 1;
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		entity.visualize(ANIMATION, GRAPHIC);
	}
}
