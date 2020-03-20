package plugin.npc.other;

import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.GroundItemManager;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.world.map.Location;

/**
 * Represents a rat npc.
 * @author 'Vexia
 */
@InitializablePlugin
public class RatNPC extends AbstractNPC {

	/**
	 * The NPC ids of NPCs using this plugin.
	 */
	private static final int[] ID = { 47, 2682, 2980, 2981, 3007, 3008, 3009, 3010, 3011, 3012, 3013, 3014, 3015, 3016, 3017, 3018, 4396, 4415, 7202, 7204, 7417, 7461 };

	/**
	 * Represents the rat tail item.
	 */
	private static final Item RAT_TAIL = new Item(300);

	/**
	 * Constructs a new {@code RatNPC} {@code Object}.
	 */
	public RatNPC() {
		super(0, null);
	}

	/**
	 * Constructs a new {@code RatNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	private RatNPC(int id, Location location) {
		super(id, location, true);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new RatNPC(id, location);
	}

	@Override
	public void finalizeDeath(final Entity killer) {
		super.finalizeDeath(killer);
		if (killer instanceof Player) {
			final Player p = ((Player) killer);
			if (p.getQuestRepository().getQuest("Witch's Potion").isStarted(p)) {
				GroundItemManager.create(RAT_TAIL, getLocation(), p);
			}
			if (!getName().equals("Giant rat")) {
				GroundItemManager.create(new Item(526), getLocation(), (Player) killer);
			}
		}
	}

	@Override
	public int[] getIds() {
		return ID;
	}

}
