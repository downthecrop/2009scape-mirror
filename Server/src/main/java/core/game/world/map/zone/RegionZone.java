package core.game.world.map.zone;

import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RegionZone that = (RegionZone) o;
		return Objects.equals(zone, that.zone) && Objects.equals(borders, that.borders);
	}

	@Override
	public int hashCode() {
		return Objects.hash(zone, borders);
	}
}