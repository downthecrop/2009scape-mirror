package plugin.quest.arravshield;

import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.quest.Quest;
import org.crandor.game.node.item.GroundItemManager;
import org.crandor.game.world.map.Location;

/**
 * Represents the npc to handle Johnny the beard npc.
 * @author 'Vexia
 * @version 1.0
 */
public final class JohnnyBeardNPC extends AbstractNPC {

	/**
	 * The NPC ids of NPCs using this plugin.
	 */
	private static final int[] ID = { 645 };

	/**
	 * Constructs a new {@code JohnnyBeardNPC} {@code Object}.
	 */
	public JohnnyBeardNPC() {
		super(0, null);
	}

	/**
	 * Constructs a new {@code JohnnyBeardNPC} {@code Object}.
	 * @param id The NPC id.
	 * @param location The location.
	 */
	private JohnnyBeardNPC(int id, Location location) {
		super(id, location);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new JohnnyBeardNPC(id, location);
	}

	@Override
	public void finalizeDeath(final Entity killer) {
		super.finalizeDeath(killer);
		final Player p = ((Player) killer);
		final Quest quest = p.getQuestRepository().getQuest("Shield of Arrav");
		if (quest.getStage(p) == 60 && ShieldofArrav.isPhoenixMission(p) && !p.getInventory().containsItem(ShieldofArrav.INTEL_REPORT) && !p.getBank().containsItem(ShieldofArrav.INTEL_REPORT)) {
			GroundItemManager.create(ShieldofArrav.INTEL_REPORT, getLocation(), p);
		}
	}

	@Override
	public int[] getIds() {
		return ID;
	}

}
