package core.game.node.entity.skill.hunter;

import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.tools.RandomFunction;

/**
 * A trap location hook.
 * @author Vexia
 */
public class TrapHook {

	/**
	 * The wrapper of the hook.
	 */
	private final TrapWrapper wrapper;

	/**
	 * The locations for the trap to trigger.
	 */
	private final Location[] locations;

	/**
	 * Constructs a new {@code TrapHook} {@code Object}.
	 * @param wrapper the wrapper.
	 * @param locations the locations.
	 */
	public TrapHook(TrapWrapper wrapper, Location[] locations) {
		this.wrapper = wrapper;
		this.locations = locations;
	}

	/**
	 * Gets a location by chance for the npc to go to.
	 * @return the location.
	 */
	public Location getChanceLocation() {
		final double chance = wrapper.getChanceRate();
		final int roll = RandomFunction.random(99);
		final double successChance = (GameWorld.getSettings().isDevMode() ? 100 : 55.0) + chance;
		if (successChance > roll) {
			return RandomFunction.getRandomElement(locations);
		}
		return null;
	}

	/**
	 * Checks if the trap is hooked.
	 * @param location the location.
	 * @return {@code True} if hooked.
	 */
	public boolean isHooked(Location location) {
		for (Location l : locations) {
			if (l.equals(location)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the wrapper.
	 * @return The wrapper.
	 */
	public TrapWrapper getWrapper() {
		return wrapper;
	}

	/**
	 * Gets the locations.
	 * @return The locations.
	 */
	public Location[] getLocations() {
		return locations;
	}

}
