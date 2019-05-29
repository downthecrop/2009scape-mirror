package plugin.combat.special;

import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.combat.CombatStyle;
import org.crandor.game.node.entity.combat.handlers.MeleeSwingHandler;
import org.crandor.game.node.entity.impl.Animator.Priority;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.audio.Audio;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Handles the granite maul special attack.
 * @author Emperor
 */
@InitializablePlugin
public final class QuickSmashSpecialHandler extends MeleeSwingHandler implements Plugin<Object> {

	/**
	 * The special energy required.
	 */
	private static final int SPECIAL_ENERGY = 50;

	/**
	 * The attack animation.
	 */
	private static final Animation ANIMATION = new Animation(1667, Priority.HIGH);

	/**
	 * The graphic.
	 */
	private static final Graphics GRAPHIC = new Graphics(340, 96);

	@Override
	public Object fireEvent(String identifier, Object... args) {
		switch (identifier) {
		case "instant_spec":
			return true;
		}
		return null;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		CombatStyle.MELEE.getSwingHandler().register(4153, this);
		CombatStyle.MELEE.getSwingHandler().register(14792, this);
		return this;
	}

	@Override
	public int swing(Entity entity, Entity victim, BattleState state) {
		Player p = (Player) entity;
		if (victim == null) {
			victim = p.getProperties().getCombatPulse().getLastVictim();
			if (victim == null || GameWorld.getTicks() - p.getAttribute("combat-stop", -1) > 2 || !MeleeSwingHandler.canMelee(p, victim, 1)) {
				p.getPacketDispatch().sendMessage("Warning: Since the maul's special is an instant attack, it will be wasted when used ");
				p.getPacketDispatch().sendMessage("on a first strike.");
				return -1;
			}
		}
		if (!p.getSettings().drainSpecial(SPECIAL_ENERGY)) {
			return -1;
		}
		visualize(entity, victim, null);
		int hit = 0;
		if (isAccurateImpact(entity, victim)) {
			hit = RandomFunction.random(calculateHit(entity, victim, 1.));
		}
		victim.getImpactHandler().handleImpact(entity, hit, CombatStyle.MELEE);
		entity.asPlayer().getAudioManager().send(new Audio(2715), true);
		return 1;
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		entity.visualize(ANIMATION, GRAPHIC);
		victim.animate(victim.getProperties().getDefenceAnimation());
	}

}
