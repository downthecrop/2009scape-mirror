package content.global.handlers.item.equipment.special;

import core.game.node.entity.skill.Skills;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.MeleeSwingHandler;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Plugin;
import core.plugin.Initializable;
import core.tools.RandomFunction;
import org.rs09.consts.Sounds;

import static core.api.ContentAPIKt.playGlobalAudio;

/**
 * Handles the Warstrike special attack.
 * @author Emperor
 */
@Initializable
public final class WarstrikeSpecialHandler extends MeleeSwingHandler implements Plugin<Object> {

	/**
	 * The special energy required.
	 */
	private static final int SPECIAL_ENERGY = 100;

	/**
	 * The attack animation.
	 */
	private static final Animation ANIMATION = new Animation(7073, Priority.HIGH);

	/**
	 * The graphic.
	 */
	private static final Graphics GRAPHIC = new Graphics(1223);

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		CombatStyle.MELEE.getSwingHandler().register(11696, this);
		CombatStyle.MELEE.getSwingHandler().register(13451, this);
		return this;
	}

	@Override
	public int swing(Entity entity, Entity victim, BattleState state) {
		if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
			return -1;
		}
        state.setStyle(CombatStyle.MELEE);
		int hit = 0;
		if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 2.0, 1.0)) {
			hit = RandomFunction.random((int) (calculateHit(entity, victim, 1.1) * 1.1) + 1);
		}
		state.setEstimatedHit(hit);
		if (victim instanceof Player) {
			((Player) victim).getPacketDispatch().sendMessage("You have been drained.");
		}
		int left = -victim.getSkills().updateLevel(Skills.DEFENCE, -hit, 0);
		if (left > 0) {
			left = -victim.getSkills().updateLevel(Skills.STRENGTH, -left, 0);
			if (left > 0) {
				left = (int) -(victim.getSkills().getPrayerPoints() + left);
				victim.getSkills().decrementPrayerPoints(left);
				if (left > 0) {
					left = -victim.getSkills().updateLevel(Skills.ATTACK, -left, 0);
					if (left > 0) {
						left = -victim.getSkills().updateLevel(Skills.MAGIC, -left, 0);
						if (left > 0)
							victim.getSkills().updateLevel(Skills.RANGE, -left, 0);
					}
				}
			}
		}
		return 1;
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		playGlobalAudio(entity.getLocation(), Sounds.GODWARS_SARADOMIN_MAGIC_CASTANDFIRE_3834);
		entity.visualize(ANIMATION, GRAPHIC);
	}
}
