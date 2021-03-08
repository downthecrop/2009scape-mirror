package core.game.node.entity.player.link.appearance;

/**
 * Rerpresents a type of gender and it's relate base settings.
 * @author 'Vexia
 */
public enum Gender {
	MALE(new BodyPart[] { new BodyPart(0), new BodyPart(10), new BodyPart(18), new BodyPart(26), new BodyPart(33), new BodyPart(36), new BodyPart(42) }),
	FEMALE(new BodyPart[] { new BodyPart(45), new BodyPart(1000), new BodyPart(56), new BodyPart(61), new BodyPart(68), new BodyPart(70), new BodyPart(79) });

	/**
	 * Represents the default appearance of this gender.
	 */
	private final BodyPart[] appearanceCache;

	/**
	 * Constructs a new {@code Gender} {@code Object}.
	 */
	Gender(final BodyPart[] appearanceCache) {
		this.appearanceCache = appearanceCache;
	}

	/**
	 * Method used to generate a default appearance cache.
	 * @return the cache.
	 */
	public BodyPart[] generateCache() {
		final BodyPart[] cache = new BodyPart[appearanceCache.length];
		for (int i = 0; i < cache.length; i++) {
			cache[i] = new BodyPart(appearanceCache[i].getLook());
		}
		return cache;
	}

	/**
	 * Gets the appearance cache.
	 * @return The appearance cache.
	 */
	public BodyPart[] getAppearanceCache() {
		return appearanceCache;
	}

	/**
	 * Gets the representation as a byte.
	 * @return the byte.
	 */
	public byte toByte() {
		return (byte) (this == MALE ? 0 : 1);
	}

	/**
	 * Gets the gender from a byte.
	 * @param value the value.
	 * @return the gender.
	 */
	public Gender asByte(byte value) {
		return value == 0 ? MALE : FEMALE;
	}

}