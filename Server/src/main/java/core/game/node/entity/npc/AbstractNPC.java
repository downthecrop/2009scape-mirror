package core.game.node.entity.npc;

import rs09.game.system.SystemLogger;
import core.game.world.map.Location;
import core.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Used as superclass for "special NPC" plugins.
 * @author Emperor
 */
public abstract class AbstractNPC extends NPC implements Plugin<Object> {

	/**
	 * The abstract NPC mapping.
	 */
	protected static Map<Integer, AbstractNPC> mapping = new HashMap<>();

	/**
	 * Constructs a new {@code AbstractNPC} {@Code Object}.
	 * @param id The id.
	 * @param location The location.
	 */
	public AbstractNPC(int id, Location location) {
		this(id, location, true);
	}

	/**
	 * Constructs a new {@code AbstractNPC} {@Code Object}.
	 * @param id The id.
	 * @param location The location.
	 * @param autowalk If the NPC should move around.
	 */
	public AbstractNPC(int id, Location location, boolean autowalk) {
		super(id, location);
		super.setWalks(autowalk);
	}

	/**
	 * Configures default boss data.
	 */
	public void configureBossData() {
		getProperties().setNPCWalkable(true);
		getProperties().setCombatTimeOut(2);
		if (!isAggressive()) {
			setAggressive(true);
			setDefaultBehavior();
		}
		if (getAggressiveHandler() != null) {
			getAggressiveHandler().setChanceRatio(6);
			getAggressiveHandler().setAllowTolerance(false);
			getAggressiveHandler().setTargetSwitching(true);
			getAggressiveHandler().setRadius(64);
		}
		walkRadius = 64;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (int id : getIds()) {
			if (mapping.containsKey(id)) {
				String name = mapping.get(id).getClass().getSimpleName();
				if (name != getClass().getSimpleName()) {
					SystemLogger.logErr("[" + getClass().getSimpleName() + "] - Warning: Mapping already contained NPC id " + id + "! (" + name + ")");
					continue;
				}
			}
			mapping.put(id, this);
		}
		return this;
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	/**
	 * Constructs a new instance of this abstract NPC.
	 * @param id The npc id.
	 * @param location The location.
	 * @param objects TODO
	 * @return The abstract npc instance.
	 */
	public abstract AbstractNPC construct(int id, Location location, Object... objects);

	/**
	 * Gets the NPC ids using this abstract NPC handler.
	 * @return The NPC ids.
	 */
	public abstract int[] getIds();

	/**
	 * Gets the abstract NPC object for this NPC id.
	 * @param npcId The NPC id.
	 * @return The abstract NPC "loader" object for the given NPC id.
	 */
	public static AbstractNPC forId(int npcId) {
		return mapping.get(npcId);
	}

}