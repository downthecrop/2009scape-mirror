package core.game.node.entity.combat.special;

import core.game.node.entity.skill.Skills;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import rs09.game.node.entity.combat.handlers.MeleeSwingHandler;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the Dragon pickaxe's special attack.
 * @author Crash
 */
@Initializable
public final class DragonPickaxeSpecialHandler extends MeleeSwingHandler implements Plugin<Object> {

	/**
	 * The special energy required.
	 */
	private static final int SPECIAL_ENERGY = 100;

	/**
	 * The attack animation.
	 */
	//private static final Animation ANIMATION = new Animation(1057, Priority.HIGH);

	/**
	 * The graphic.
	 */
	//private static final Graphics GRAPHIC = new Graphics(247);

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
		CombatStyle.MELEE.getSwingHandler().register(14723, this);
		return this;
	}

	@Override
	public int swing(Entity entity, Entity victim, BattleState state) {
		Player p = (Player) entity;
		if (!p.getSettings().drainSpecial(SPECIAL_ENERGY))
			return -1;
		p.sendChat("Smashing!");
		//p.visualize(ANIMATION, GRAPHIC);
		p.getSkills().updateLevel(Skills.MINING, 3, p.getSkills().getStaticLevel(Skills.MINING) + 3);
		return -1;
	}

}
