package core.game.node.entity.npc.familiar;

import core.game.node.entity.combat.equipment.WeaponInterface;
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
public class FireTitanNPC extends ElementalTitanNPC {

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
		super(owner, id, 6200, 12802, 20, WeaponInterface.STYLE_CAST);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new FireTitanNPC(owner, id);
	}

	@Override
	public int[] getIds() {
		return new int[] { 7355, 7356 };
	}

}
