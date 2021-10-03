package core.game.node.entity.npc.familiar;

import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the Moss Titan familiar.
 * @author Aero
 */
@Initializable
public class MossTitanNPC extends ElementalTitanNPC {

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
	public int[] getIds() {
		return new int[] { 7357, 7358 };
	}

}
