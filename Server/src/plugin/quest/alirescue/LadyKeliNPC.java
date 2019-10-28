package plugin.quest.alirescue;

import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.map.Location;

/**
 * Represents the abstract lady keli npc.
 * @author 'Vexia
 * @version 1.0
 */
public final class LadyKeliNPC extends AbstractNPC {

	/**
	 * The NPC ids of NPCs using this plugin.
	 */
	private static final int[] ID = { 919 };

	/**
	 * Constructs a new {@code LadyKeliNPC} {@code Object}.
	 */
	public LadyKeliNPC() {
		super(0, null, true);
	}

	/**
	 * Constructs a new {@code LadyKeliNPC} {@code Object}.
	 * @param id The NPC id.
	 * @param location The location.
	 */
	private LadyKeliNPC(int id, Location location) {
		super(id, location, true);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new LadyKeliNPC(id, location);
	}

	@Override
	public boolean isHidden(final Player player) {
		return player.getAttribute("keli-gone", 0) > GameWorld.getTicks();
	}

	@Override
	public int[] getIds() {
		return ID;
	}

	@Override
	public int getWalkRadius() {
		return 2;
	}

}
