package core.game.node.entity.npc.familiar;

import core.plugin.Initializable;
import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;

/**
 * Represents the Hydra familiar.
 * @author Aero
 */
@Initializable
public class HydraNPC extends Familiar {

	/**
	 * Constructs a new {@code HydraNPC} {@code Object}.
	 */
	public HydraNPC() {
		this(null, 6811);
	}

	/**
	 * Constructs a new {@code HydraNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public HydraNPC(Player owner, int id) {
		super(owner, id, 4900, 12025, 6, WeaponInterface.STYLE_RANGE_ACCURATE);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new HydraNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		return false;
	}

	@Override
	public int[] getIds() {
		return new int[] { 6811, 6812 };
	}

}
