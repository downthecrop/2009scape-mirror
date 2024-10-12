package core.game.world.map.zone;

/**
 * The zone restrictions.
 * @author Emperor
 */
public enum ZoneRestriction {

	/**
	 * No followers allowed in this zone.
	 */
	FOLLOWERS,

	/**
	 * No random events allowed.
	 */
	RANDOM_EVENTS,

	/**
	 * No fires allowed.
	 */
	FIRES,

	/**
	 * Members only.
	 */
	MEMBERS,

	/**
	 * No cannons allowed.
	 */
	CANNON,
	/**
	 * Do not spawn a grave if a player dies here.
	 */
	GRAVES,

	/**
	 * No teleporting allowed.
	 */
	TELEPORT,

	/**
	 * This region is not a part of the normal overworld or cave system.
	 * Used for temporary areas that use the 'original-loc' attribute to teleport the player back when they are done in the area.
	 * Example: non-dynamic/non-instanced random-event areas (e.g. Damien's bootcamp)
	 * Dynamic regions are implicitly off-map and do not require this attribute.
	 */
	OFF_MAP,
	;

	/**
	 * Gets the restriction flag.
	 * @return The flag.
	 */
	public int getFlag() {
		return 1 << ordinal();
	}
}
