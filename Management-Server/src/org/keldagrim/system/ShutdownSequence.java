package org.keldagrim.system;


/**
 * The shutdown sequence used for safely turning off the Management server.
 * @author Emperor
 *
 */
public final class ShutdownSequence extends Thread {

	/**
	 * If the shutdown hook is active.
	 */
	private static boolean active = true;
	
	@Override
	public void run() {
		if (active) {
			shutdown();
		}
	}
	
	/**
	 * Safely shuts down the Management server.
	 */
	public static void shutdown() {
		System.out.println("Management server successfully shut down!");
		active = false;
		System.exit(0);
	}
}