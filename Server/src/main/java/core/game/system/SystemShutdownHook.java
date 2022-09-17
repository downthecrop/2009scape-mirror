package core.game.system;

import rs09.game.system.SystemLogger;

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
		SystemLogger.logInfo(this.getClass(), "[SystemShutdownHook] Terminating...");
		SystemManager.flag(SystemState.TERMINATED);
	}
}