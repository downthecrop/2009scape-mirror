package core.game.world.map.zone;

/**
 * Represents a zone inside a single region of the world map.
 * @author Emperor
 */
public final class RegionZone {

	/**
	 * The map zone.
	 */
	private final MapZone zone;

	/**
	 * The borders.
	 */
	private final ZoneBorders borders;

	/**
	 * Constructs a new {@code RegionZone} {@code Object}.
	 * @param zone The map zone.
	 * @param borders The borders.
	 */
	public RegionZone(MapZone zone, ZoneBorders borders) {
		this.zone = zone;
		this.borders = borders;
	}

	/**
	 * Gets the borders.
	 * @return The borders.
	 */
	public ZoneBorders getBorders() {
		return borders;
	}

	/**
	 * Gets the zone.
	 * @return The zone.
	 */
	public MapZone getZone() {
		return zone;
	}

}