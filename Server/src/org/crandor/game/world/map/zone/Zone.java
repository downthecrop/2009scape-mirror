package org.crandor.game.world.map.zone;

import org.crandor.game.node.entity.Entity;

/**
 * Represents a zone.
 * @author Emperor
 */
public interface Zone {

	/**
	 * Checks if the entity can enter this map zone.
	 * @param e The entity.
	 * @return {@code True} if so.
	 */
	public boolean enter(Entity e);

	/**
	 * Called when the entity leaves this map zone.
	 * @param e The entity.
	 * @param logout If the entity is logging out.
	 * @return {@code True} if the entity can leave.
	 */
	public boolean leave(Entity e, boolean logout);

}