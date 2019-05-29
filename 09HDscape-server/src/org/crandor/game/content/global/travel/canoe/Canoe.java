package org.crandor.game.content.global.travel.canoe;

/**
 * Represents a canoe to craft.
 * @author 'Vexia
 * @date 09/11/2013
 */
public enum Canoe {
	LOG(12, 30, new int[] { 30 }), DUGOUT(27, 60, new int[] { 31 }), STABLE_DUGOUT(42, 90, new int[] { 32 }), WAKA(57, 150, new int[] { 33 });

	/**
	 * Constructs a new {@code Canoe.java} {@code Object}.
	 * @param level the level.
	 * @param experience the experience.
	 * @param configs the configs.
	 */
	Canoe(final int level, final double experience, final int... childs) {
		this.level = level;
		this.experience = experience;
		this.childs = childs;
	}

	/**
	 * Represents the level of the canoe.
	 */
	private final int level;

	/**
	 * Represents the experience receive.d
	 */
	private final double experience;

	/**
	 * Represents the child id on the interface.
	 */
	private final int childs[];

	/**
	 * Gets the level.
	 * @return The level.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Gets the experience.
	 * @return The experience.
	 */
	public double getExperience() {
		return experience;
	}

	/**
	 * Gets the child.
	 * @return The child.
	 */
	public int[] getChilds() {
		return childs;
	}

	/**
	 * Method used to get the canoe for the child.
	 * @param child the child.
	 * @return <code>True</code> if so
	 */
	public static Canoe forChild(final int child) {
		for (Canoe canoe : values()) {
			for (int childd : canoe.getChilds()) {
				if (childd == child) {
					return canoe;
				}
			}
		}
		return null;
	}
}
