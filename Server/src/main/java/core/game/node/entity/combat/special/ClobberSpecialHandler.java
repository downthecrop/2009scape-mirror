package core.game.node.entity.combat.special;

import core.game.node.entity.skill.Skills;
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

/**
 * Handles the Dragon axe special attack.
 * @author Crash
 */
@Initializable
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
		switch (identifier) {
		case "instant_spec":
		case "ncspec":
			return true;
		}
		return null;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		CombatStyle.MELEE.getSwingHandler().register(6739, this);
		return this;
	}

	@Override
	public int swing(Entity entity, Entity victim, BattleState state) {
		Player p = (Player) entity;
		if (!p.getSettings().drainSpecial(SPECIAL_ENERGY))
			return -1;
		p.sendChat("Chop chop!");
		p.visualize(ANIMATION, GRAPHIC);
		p.getSkills().updateLevel(Skills.WOODCUTTING, 3, p.getSkills().getStaticLevel(Skills.WOODCUTTING) + 3);
		return -1;
	}

}
