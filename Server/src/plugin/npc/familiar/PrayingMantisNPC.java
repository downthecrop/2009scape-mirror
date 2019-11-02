package plugin.npc.familiar;

import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.node.entity.player.Player;

/**
 * Represents the Praying Mantis familiar.
 * @author Aero
 */
@InitializablePlugin
public class PrayingMantisNPC extends Familiar {

	/**
	 * Constructs a new {@code PrayingMantisNPC} {@code Object}.
	 */
	public PrayingMantisNPC() {
		this(null, 6798);
	}

	/**
	 * Constructs a new {@code PrayingMantisNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public PrayingMantisNPC(Player owner, int id) {
		super(owner, id, 6900, 12011, 6, WeaponInterface.STYLE_ACCURATE);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new PrayingMantisNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		return false;
	}

	@Override
	public int[] getIds() {
		return new int[] { 6798, 6799 };
	}

}
