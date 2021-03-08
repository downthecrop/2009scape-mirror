package core.game.system;

/**
 * Represents the system states.
 * @author Emperor
 */
public enum SystemState {

	/**
	 * The system is currently active.
	 */
	ACTIVE,

	/**
	 * The system is being updated.
	 */
	UPDATING,

	/**
	 * The system is in development, thus only developers can connect.
	 */
	PRIVATE,

	/**
	 * The system has been terminated.
	 */
	TERMINATED;

}