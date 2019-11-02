package plugin.npc.familiar;

import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.node.entity.player.Player;

/**
 * Represents the Talon Beast familiar.
 * @author Aero
 */
@InitializablePlugin
public class TalonBeastNPC extends Familiar {

	/**
	 * Constructs a new {@code TalonBeastNPC} {@code Object}.
	 */
	public TalonBeastNPC() {
		this(null, 7347);
	}

	/**
	 * Constructs a new {@code TalonBeastNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public TalonBeastNPC(Player owner, int id) {
		super(owner, id, 4900, 12794, 6, WeaponInterface.STYLE_AGGRESSIVE);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new TalonBeastNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		return false;
	}

	@Override
	public int[] getIds() {
		return new int[] { 7347, 7348 };
	}

}
