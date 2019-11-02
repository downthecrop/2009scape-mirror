package plugin.npc.familiar;

import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.node.entity.player.Player;

/**
 * Represents the Moss Titan familiar.
 * @author Aero
 */
@InitializablePlugin
public class MossTitanNPC extends Familiar {

	/**
	 * Constructs a new {@code MossTitanNPC} {@code Object}.
	 */
	public MossTitanNPC() {
		this(null, 7357);
	}

	/**
	 * Constructs a new {@code MossTitanNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public MossTitanNPC(Player owner, int id) {
		super(owner, id, 5800, 12804, 20, WeaponInterface.STYLE_AGGRESSIVE);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new MossTitanNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		return false;
	}

	@Override
	public int[] getIds() {
		return new int[] { 7357, 7358 };
	}

}
