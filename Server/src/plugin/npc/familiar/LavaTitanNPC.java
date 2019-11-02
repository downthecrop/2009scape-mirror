package plugin.npc.familiar;

import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.node.entity.player.Player;

/**
 * Represents the Lava Titan familiar.
 * @author Aero
 */
@InitializablePlugin
public class LavaTitanNPC extends Familiar {

	/**
	 * Constructs a new {@code LavaTitanNPC} {@code Object}.
	 */
	public LavaTitanNPC() {
		this(null, 7341);
	}

	/**
	 * Constructs a new {@code LavaTitanNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public LavaTitanNPC(Player owner, int id) {
		super(owner, id, 6100, 12788, 4, WeaponInterface.STYLE_AGGRESSIVE);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new LavaTitanNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		return false;
	}

	@Override
	public int[] getIds() {
		return new int[] { 7341, 7342 };
	}

}
