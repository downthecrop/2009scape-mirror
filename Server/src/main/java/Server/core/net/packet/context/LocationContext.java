package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.net.packet.Context;

/**
 * Packet context used for location based packets.
 * @author Emperor
 */
public final class LocationContext implements Context {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The location.
	 */
	private final Location location;

	/**
	 * If the location update is flagged as a teleport.
	 */
	private final boolean teleport;

	/**
	 * Constructs a new {@code LocationContext} {@code Object}.
	 * @param player The player.
	 * @param location The location.
	 * @param teleport If the location update is flagged as a teleport.
	 */
	public LocationContext(Player player, Location location, boolean teleport) {
		this.player = player;
		this.location = location;
		this.teleport = teleport;
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the location.
	 * @return The location.
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Gets the teleport.
	 * @return The teleport.
	 */
	public boolean isTeleport() {
		return teleport;
	}

}