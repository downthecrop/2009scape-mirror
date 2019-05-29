package org.crandor.game.content.skill.member.farming.wrapper.handler;

import org.crandor.game.content.skill.member.farming.wrapper.PatchCycle;

/**
 * Represents the class used to handle watering effects.
 * @author 'Vexia
 * @version 1.0
 */
public final class WaterHandler {

	/**
	 * Represents the cycle.
	 */
	private final PatchCycle cycle;

	/**
	 * Constructs a new {@code WaterHandler} {@code Object}.
	 * @param cycle the cycle.
	 */
	public WaterHandler(final PatchCycle cycle) {
		this.cycle = cycle;
	}

	/**
	 * Method used to remove the water config.
	 */
	public void removeWater() {
		if (isWatered()) {
			cycle.addConfigValue(cycle.getNode().getBase() + cycle.getState() - getWateredBase());
		}
	}

	/**
	 * Sets the patch as watered.
	 */
	public void setWatered() {
		cycle.addConfigValue(getWateredBase() + (cycle.getState() - cycle.getNode().getBase()));
	}

	/**
	 * Gets the watered base config.
	 * @return the wateredbase config.
	 */
	public int getWateredBase() {
		return (cycle.getNode().getBase() + cycle.getNode().getWaterBase());
	}

	/**
	 * Checks if the patch is watered.
	 * @return {@code True} if so.
	 */
	public boolean isWatered() {
		if (cycle.getNode() == null) {
			return false;
		}
		int state = cycle.getState();
		for (int i = 0; i <= cycle.getNode().getGrowthCycles() - 1; i++) {
			if (state == getWateredBase() + i) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the cycle.
	 * @return The cycle.
	 */
	public PatchCycle getCycle() {
		return cycle;
	}

}
