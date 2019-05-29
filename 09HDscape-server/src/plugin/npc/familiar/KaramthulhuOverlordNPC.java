package plugin.npc.familiar;

import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.node.entity.player.Player;

/**
 * Represents the Karamthulhu Overlord familiar.
 * @author Aero
 */
@InitializablePlugin
public class KaramthulhuOverlordNPC extends Familiar {

	/**
	 * Constructs a new {@code KaramthulhuOverlordNPC} {@code Object}.
	 */
	public KaramthulhuOverlordNPC() {
		this(null, 6809);
	}

	/**
	 * Constructs a new {@code KaramthulhuOverlordNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public KaramthulhuOverlordNPC(Player owner, int id) {
		super(owner, id, 4400, 12023, 3, WeaponInterface.STYLE_RANGE_ACCURATE);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new KaramthulhuOverlordNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		return false;
	}

	@Override
	public int[] getIds() {
		return new int[] { 6809, 6810 };
	}

}
