package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.net.packet.Context;

/**
 * Handles the area position update packet context.
 * @author Emperor
 */
public final class AreaPositionContext implements Context {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The location.
	 */
	private final Location location;

	/**
	 * The offset x-coordinate.
	 */
	private final int offsetX;

	/**
	 * The offset y-coordinate.
	 */
	private final int offsetY;

	/**
	 * Constructs a new {@code AreaPositionContext} {@code Object}.
	 * @param player The player.
	 * @param location The location.
	 * @param offsetX The offset x-coordinate.
	 * @param offsetY The offset y-coordinate.
	 */
	public AreaPositionContext(Player player, Location location, int offsetX, int offsetY) {
		this.player = player;
		this.location = location;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
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
	 * Gets the offsetX.
	 * @return The offsetX.
	 */
	public int getOffsetX() {
		return offsetX;
	}

	/**
	 * Gets the offsetY.
	 * @return The offsetY.
	 */
	public int getOffsetY() {
		return offsetY;
	}

}