package core.game.system.task;

import core.game.node.entity.player.Player;
import core.game.world.map.Location;

/**
 * Sets the player's location on logout.
 * @author Emperor
 */
public final class LocationLogoutTask extends LogoutTask {

	/**
	 * The location to set.
	 */
	private final Location location;

	/**
	 * Constructs a new {@code LocationLogoutTask} {@code Object}.
	 * @param ticks The amount of ticks this task is valid for.
	 * @param location The location to set.
	 */
	public LocationLogoutTask(int ticks, Location location) {
		super(ticks);
		this.location = location;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run(Player player) {
		player.setLocation(location);
	}

}