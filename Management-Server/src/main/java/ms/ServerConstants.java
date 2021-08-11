package ms;

import ms.system.OperatingSystem;

/**
 * Holds constants for the management server.
 * @author v4rg
 *
 */
public final class ServerConstants {
	
	/**
	 * The maximum amount of worlds.
	 */
	public static final int WORLD_LIMIT = 10;
	
	/**
	 * The world switching delay in milliseconds.
	 */
	public static final long WORLD_SWITCH_DELAY = 20_000L;
	
	/**
	 * The address of the Management server.
	 */
	public static final String HOST_ADDRESS = "127.0.0.1";
	
	/**
	 * The operating system of the management server
	 */
	public static final OperatingSystem OS = System.getProperty("os.name").toUpperCase().contains("WIN") ? OperatingSystem.WINDOWS : OperatingSystem.UNIX;
	
	/**
	 * Stops from instantiating.
	 */
	private ServerConstants() {
		/*
		 * empty.
		 */
	}
	
	/**
	 * Fixes a path to a specified operating system
	 * @param operatingSystem The os type.
	 * @param path The path.
	 * @return The fixed path.
	 */
    public static String fixPath(OperatingSystem operatingSystem, String path) {
    	if (operatingSystem == null)
    		operatingSystem = OS;
    	return operatingSystem == OperatingSystem.WINDOWS ? path.replace("/","\\") : path.replace("\\","/");
    }
}