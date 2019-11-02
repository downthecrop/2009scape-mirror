package plugin.npc.familiar;

import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.node.entity.player.Player;

/**
 * Represents the Spirit Graahk familiar.
 * @author Aero
 */
@InitializablePlugin
public class SpiritGraahkNPC extends Familiar {

	/**
	 * Constructs a new {@code SpiritGraahkNPC} {@code Object}.
	 */
	public SpiritGraahkNPC() {
		this(null, 7363);
	}

	/**
	 * Constructs a new {@code SpiritGraahkNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public SpiritGraahkNPC(Player owner, int id) {
		super(owner, id, 4900, 12810, 3, WeaponInterface.STYLE_AGGRESSIVE);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new SpiritGraahkNPC(owner, id);
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
		return new int[] { 7363, 7364 };
	}

}
