package org.crandor.game.system.task;

import org.crandor.game.node.entity.Entity;
import org.crandor.game.world.map.Location;

/**
 * An interface used for hooking to the
 *.world.map.MovementUpdate} class.
 * @author Emperor
 */
public interface MovementHook {

	/**
	 * Called when an entity has moved.
	 * @param e The moving entity.
	 * @param l The location.
	 * @return {@code True} if no further hooks should be called.
	 */
	boolean handle(Entity e, Location l);

}