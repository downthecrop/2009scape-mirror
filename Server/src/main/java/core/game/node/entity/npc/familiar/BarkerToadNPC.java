package core.game.node.entity.npc.familiar;

import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.game.world.update.flag.context.Graphics;

/**
 * Represents the Barker Toad familiar.
 * @author Aero
 */
@Initializable
public class BarkerToadNPC extends Familiar {

	/**
	 * Constructs a new {@code BarkerToadNPC} {@code Object}.
	 */
	public BarkerToadNPC() {
		this(null, 6889);
	}

	/**
	 * Constructs a new {@code BarkerToadNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public BarkerToadNPC(Player owner, int id) {
		super(owner, id, 800, 12123, 6, WeaponInterface.STYLE_AGGRESSIVE);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new BarkerToadNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		final Entity target = special.getTarget();
		if (!canCombatSpecial(target)) {
			return false;
		}
		animate(getProperties().getAttackAnimation());
		graphics(Graphics.create(1403));
		sendFamiliarHit(target, 8, Graphics.create(1404));
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 6889, 6890 };
	}

}
