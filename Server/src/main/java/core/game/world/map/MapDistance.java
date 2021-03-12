package core.game.world.map;

/**
 * An enum holding commonly used distances (in tiles of the map).
 * @author Emperor
 */
public enum MapDistance {

	/**
	 * The rendering distance.
	 */
	RENDERING(15),

	/**
	 * The sound packet sending distance.
	 */
	SOUND(5), ;

	/**
	 * The distance.
	 */
	private final int distance;

	/**
	 * Constructs a new {@code MapDistance} {@code Object}.
	 * @param distance The distance (in tiles).
	 */
	private MapDistance(int distance) {
		this.distance = distance;
	}

	/**
	 * Gets the distance.
	 * @return the distance.
	 */
	public int getDistance() {
		return distance;
	}
}