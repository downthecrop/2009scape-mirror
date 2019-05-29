package org.crandor.game.node.entity.lock;

import org.crandor.game.node.Node;

/**
 * Called after the expiration of a custom reward lock.
 * @author Aero
 */
public interface LockElapse {

	/**
	 * Called when a custom reward lock has elapsed.
	 * @param node The node.
	 * @param lock The custom reward lock.
	 */
	public void elapse(Node node, Lock lock);

}