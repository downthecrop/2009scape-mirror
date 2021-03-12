package core.game.node.entity.npc.other;

import core.game.node.entity.npc.AbstractNPC;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * Represents the lumbridge instructor npcs.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class LumbridgeInstructorNPC extends AbstractNPC {

	/**
	 * The NPC ids of NPCs using this plugin.
	 */
	private static final int[] ID = { 4707, 4906, 705, 1861, 4903, 4899, 4900, 4904 };

	/**
	 * Constructs a new {@code LumbridgeInstructorNPC} {@code Object}.
	 */
	public LumbridgeInstructorNPC() {
		super(0, null);
	}

	/**
	 * Constructs a new {@code LumbridgeInstructorNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	private LumbridgeInstructorNPC(int id, Location location) {
		super(id, location);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new LumbridgeInstructorNPC(id, location);
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	public int getWalkRadius() {
		return 4;
	}

	@Override
	public Location getMovementDestination() {
		return getProperties().getSpawnLocation().transform(-2 + RandomFunction.random(getWalkRadius()), -2 + RandomFunction.random(getWalkRadius()), 0);
	}

	@Override
	public int[] getIds() {
		return ID;
	}

}
