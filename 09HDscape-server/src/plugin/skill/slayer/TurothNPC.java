package plugin.skill.slayer;

import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;

/**
 * Handles the turoth npc.
 * @author Vexia
 */
@InitializablePlugin
public final class TurothNPC extends AbstractNPC {

	/**
	 * Constructs a new {@code TurothNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	public TurothNPC(int id, Location location) {
		super(id, location);
	}

	/**
	 * Constructs a new {@code TurothNPC} {@code Object}.
	 */
	public TurothNPC() {
		super(0, null);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new TurothNPC(id, location);
	}

	@Override
	public void checkImpact(final BattleState state) {
		super.checkImpact(state);
		boolean effective = false;
		if (state.getAttacker() instanceof Player) {
			final Player player = (Player) state.getAttacker();
			if ((state.getWeapon() != null && state.getWeapon().getId() == 4158) || (state.getAmmunition() != null && state.getAmmunition().getItemId() == 4160) || (state.getSpell() != null && state.getSpell().getSpellId() == 31 && player.getSpellBookManager().getSpellBook() == SpellBook.MODERN.getInterfaceId())) {
				effective = true;
			}
		}
		if (!effective) {
			state.setEstimatedHit(0);
			if (state.getSecondaryHit() > 0) {
				state.setSecondaryHit(0);
			}
		}
	}

	@Override
	public int[] getIds() {
		return new int[] { 1626, 1627, 1628, 1629, 1630 };
	}

}
