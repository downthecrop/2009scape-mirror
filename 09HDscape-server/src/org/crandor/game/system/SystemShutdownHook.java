package org.crandor.game.system;

import org.crandor.game.events.GlobalEventManager;

/**
 * Handles the shutdown hook.
 * @author Emperor
 */
public final class SystemShutdownHook implements Runnable {

	@Override
	public void run() {
		if (SystemManager.isTerminated()) {
			return;
		}
		SystemLogger.log("[SystemShutdownHook] Terminating...");
		GlobalEventManager.get().save();
		SystemManager.getTerminator().terminate();
	}
}