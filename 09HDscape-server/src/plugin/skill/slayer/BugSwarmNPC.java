package plugin.skill.slayer;

import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;

/**
 * Represents the plugin used to handle the harpie bug swarm.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class BugSwarmNPC extends AbstractNPC {

	/**
	 * Represents the lit latern item.
	 */
	private static final Item LIT_LANTERN = new Item(7053);

	/**
	 * Constructs a new {@code BugSwarmNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	public BugSwarmNPC(int id, Location location) {
		super(id, location, true);
	}

	/**
	 * Constructs a new {@code BugSwarmNPC} {@code Object}.
	 */
	public BugSwarmNPC() {
		super(0, null);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new BugSwarmNPC(id, location);
	}

	@Override
	public void checkImpact(BattleState state) {
		super.checkImpact(state);
		if (state.getAttacker() instanceof Player) {
			final Player player = (Player) state.getAttacker();
			if (!player.getEquipment().containsItem(LIT_LANTERN)) {
				if (state.getEstimatedHit() > -1) {
					state.setEstimatedHit(0);
				}
				if (state.getSecondaryHit() > -1) {
					state.setSecondaryHit(0);
				}
			}
		}
	}

	@Override
	public int[] getIds() {
		return new int[] { 3153 };
	}

}
