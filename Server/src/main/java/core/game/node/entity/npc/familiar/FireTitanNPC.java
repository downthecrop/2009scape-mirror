package core.game.node.entity.npc.familiar;

import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the Fire Titan familiar.
 * @author Aero
 */
@Initializable
public class FireTitanNPC extends Familiar {

	private static final int scrollHealAmount = 8;
	private static final double  scrollDefenceBoostPercent = 12.5;
	/**
	 * Constructs a new {@code FireTitanNPC} {@code Object}.
	 */
	public FireTitanNPC() {
		this(null, 7355);
	}

	/**
	 * Constructs a new {@code FireTitanNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public FireTitanNPC(Player owner, int id) {
		super(owner, id, 6200, 12802, 20);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new FireTitanNPC(owner, id);
	}

	/**
		raises defence by 12.5% and heals 8 hp (with ability to over heal)
	 */
	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		int currentDefenceLevel = owner.getSkills().getLevel(Skills.DEFENCE);
		owner.getSkills().updateLevel(Skills.DEFENCE, (int)(scrollDefenceBoostPercent * currentDefenceLevel), 112);
		int currentHp = owner.getSkills().getLifepoints();
		int maxHp = owner.getSkills().getMaximumLifepoints() + scrollHealAmount;
		int healAmount = Math.min(maxHp - currentHp, scrollHealAmount);
		if (healAmount> 0) {
			owner.getSkills().healNoRestrictions(healAmount);
			return true;
		}
		else {
			owner.sendMessage("You are already at maximum hitpoints!");
			return false;
		}
	}

	@Override
	public int[] getIds() {
		return new int[] { 7355, 7356 };
	}

}
