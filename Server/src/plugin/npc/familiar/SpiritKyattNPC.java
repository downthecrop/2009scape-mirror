package plugin.npc.familiar;

import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.node.entity.player.Player;

/**
 * Represents the Spirit Kyatt familiar.
 * @author Aero
 */
@InitializablePlugin
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
