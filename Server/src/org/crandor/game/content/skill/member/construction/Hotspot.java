package org.crandor.game.content.skill.member.construction;

/**
 * Represents a hotspot.
 * @author Emperor
 *
 */
public final class Hotspot {

	/**
	 * The hotspot.
	 */
	private final BuildHotspot hotspot;

	/**
	 * The chunk x coordinate.
	 */
	private int chunkX;

	/**
	 * The chunk y coordinate.
	 */
	private int chunkY;

	/**
	 * The current chunk x coordinate.
	 */
	private int currentX;

	/**
	 * The chunk y coordinate.
	 */
	private int currentY;

	/**
	 * The current decoration index.
	 */
	private int decorationIndex = -1;

	/**
	 * Constructs a new {@code Hotspot} {@code Object}.
	 * @param hotspot The hotspot.
	 * @param chunkX The chunk x-coordinate.
	 * @param chunkY The chunk y-coordinate.
	 */
	public Hotspot(BuildHotspot hotspot, int chunkX, int chunkY) {
		this.hotspot = hotspot;
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.currentX = chunkX;
		this.currentY = chunkY;
	}

	/**
	 * Copies the hotspot.
	 * @return The hotspot.
	 */
	public Hotspot copy() {
		return new Hotspot(hotspot, chunkX, chunkY);
	}

	/**
	 * Gets the hotspot.
	 * @return The hotspot.
	 */
	public BuildHotspot getHotspot() {
		return hotspot;
	}

	/**
	 * Gets the chunkX.
	 * @return The chunkX.
	 */
	public int getChunkX() {
		return chunkX;
	}

	/**
	 * Gets the chunkY.
	 * @return The chunkY.
	 */
	public int getChunkY() {
		return chunkY;
	}

	/**
	 * Gets the decorationIndex.
	 * @return The decorationIndex.
	 */
	public int getDecorationIndex() {
		return decorationIndex;
	}

	/**
	 * Sets the decorationIndex.
	 * @param decorationIndex The decorationIndex to set.
	 */
	public void setDecorationIndex(int decorationIndex) {
		this.decorationIndex = decorationIndex;
	}

	/**
	 * Gets the currentX value.
	 * @return The currentX.
	 */
	public int getCurrentX() {
		return currentX;
	}

	/**
	 * Sets the currentX value.
	 * @param currentX The currentX to set.
	 */
	public void setCurrentX(int currentX) {
		this.currentX = currentX;
	}

	/**
	 * Gets the currentY value.
	 * @return The currentY.
	 */
	public int getCurrentY() {
		return currentY;
	}

	/**
	 * Sets the currentY value.
	 * @param currentY The currentY to set.
	 */
	public void setCurrentY(int currentY) {
		this.currentY = currentY;
	}

}