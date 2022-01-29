package core.game.node.entity.npc.familiar;

import core.game.node.entity.skill.SkillBonus;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the Spirit Kyatt familiar.
 * @author Aero
 */
@Initializable
public class SpiritKyattNPC extends Familiar {

	/**
	 * Constructs a new {@code SpiritKyattNPC} {@code Object}.
	 */
	public SpiritKyattNPC() {
		this(null, 7366);
	}

	/**
	 * Constructs a new {@code SpiritKyattNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public SpiritKyattNPC(Player owner, int id) {
		super(owner, id, 4900, 12812, 3, WeaponInterface.STYLE_ACCURATE);
		boosts.add(new SkillBonus(Skills.HUNTER, 5));		
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new SpiritKyattNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		if (!super.isOwnerAttackable()) {
			return false;
		}
		call();
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 7365, 7366 };
	}

}
