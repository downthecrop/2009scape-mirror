package content.global.handlers.npc;

import core.game.node.entity.Entity;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.game.world.map.Location;
import content.data.Quests;

/**
 * Represents a rat npc.
 * @author 'Vexia
 */
@Initializable
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
			if (p.getQuestRepository().getQuest(Quests.WITCHS_POTION).isStarted(p)) {
				GroundItemManager.create(RAT_TAIL, getLocation(), p);
			}
		}
	}

	@Override
	public int[] getIds() {
		return ID;
	}

}
