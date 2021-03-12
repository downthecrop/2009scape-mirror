package core.game.system;

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
		SystemLogger.logInfo("[SystemShutdownHook] Terminating...");
		SystemManager.getTerminator().terminate();
	}
}