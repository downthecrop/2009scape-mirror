package plugin.npc.familiar;

import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.crandor.game.node.entity.player.Player;

/**
 * Represents the Spirit Pengatrice familiar.
 * @author Aero
 */
public class SpiritPengatriceNPC extends Familiar {

	/**
	 * Constructs a new {@code SpiritPengatriceNPC} {@code Object}.
	 */
	public SpiritPengatriceNPC() {
		this(null, 6883);
	}

	/**
	 * Constructs a new {@code SpiritPengatriceNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public SpiritPengatriceNPC(Player owner, int id) {
		super(owner, id, 3600, 12103, 3);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new SpiritPengatriceNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		return false;
	}

	@Override
	public int[] getIds() {
		return new int[] { 6883, 6884 };
	}

}