package core.game.world.update.flag;

import core.game.world.map.Location;
import core.game.world.map.RegionChunk;
import core.game.world.map.Viewport;

/**
 * A class holding a player's updating flags.
 * @author Emperor
 */
public final class PlayerFlags {

	/**
	 * If the scene graph has been updated during this tick.
	 */
	private boolean updateSceneGraph;

	/**
	 * The last viewport.
	 */
	private RegionChunk[][] lastViewport = new RegionChunk[Viewport.CHUNK_SIZE][Viewport.CHUNK_SIZE];

	/**
	 * The location the player was standing on when last scene graph update
	 * occured.
	 */
	private Location lastSceneGraph;

	/**
	 * Constructs a new {@code PlayerFlags} {@code Object}.
	 */
	public PlayerFlags() {
		/*
		 * empty.
		 */
	}

	/**
	 * Gets the updateSceneGraph.
	 * @return The updateSceneGraph.
	 */
	public boolean isUpdateSceneGraph() {
		return updateSceneGraph;
	}

	/**
	 * Sets the updateSceneGraph.
	 * @param updateSceneGraph The updateSceneGraph to set.
	 */
	public void setUpdateSceneGraph(boolean updateSceneGraph) {
		this.updateSceneGraph = updateSceneGraph;
	}

	/**
	 * Gets the lastSceneGraph.
	 * @return The lastSceneGraph.
	 */
	public Location getLastSceneGraph() {
		return lastSceneGraph;
	}

	/**
	 * Sets the lastSceneGraph.
	 * @param lastSceneGraph The lastSceneGraph to set.
	 */
	public void setLastSceneGraph(Location lastSceneGraph) {
		this.lastSceneGraph = lastSceneGraph;
	}

	/**
	 * Gets the lastViewport.
	 * @return The lastViewport.
	 */
	public RegionChunk[][] getLastViewport() {
		return lastViewport;
	}

	/**
	 * Sets the lastViewport.
	 * @param lastViewport The lastViewport to set.
	 */
	public void setLastViewport(RegionChunk[][] lastViewport) {
		this.lastViewport = lastViewport;
	}

}