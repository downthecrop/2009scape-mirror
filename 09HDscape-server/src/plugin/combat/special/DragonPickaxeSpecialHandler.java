package plugin.combat.special;

import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.combat.CombatStyle;
import org.crandor.game.node.entity.combat.handlers.MeleeSwingHandler;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles the Dragon Pickaxe's special attack.
 * @author Splinter
 * @version 1.0
 */
@InitializablePlugin
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
		CombatStyle.MELEE.getSwingHandler().register(14669, this);
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
