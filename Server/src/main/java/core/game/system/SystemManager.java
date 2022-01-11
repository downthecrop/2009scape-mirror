package core.game.system;

import core.game.system.security.EncryptionManager;
import rs09.game.world.GameWorld;

/**
 * Manages the "game system" states, such as updating or terminating.
 * @author Emperor
 */
public final class SystemManager {

	/**
	 * The system state.
	 */
	private static SystemState state = SystemState.TERMINATED;

	/**
	 * The system update handler.
	 */
	private static final SystemUpdate UPDATER = new SystemUpdate();

	/**
	 * The system termination handler.
	 */
	private static final SystemTermination TERMINATOR = new SystemTermination();
	
	/**
	 * The system configurator.
	 */
	private static final SystemConfig SYSTEM_CONFIG = new SystemConfig();

	/**
	 * The encryption the server uses.
	 */
	private static final EncryptionManager ENCRYPTION = new EncryptionManager();

	/**
	 * Constructs a new {@code SystemManager} {@code Object}.
	 */
	private SystemManager() {
		/*
		 * empty.
		 */
	}

	/**
	 * Sets the current state and handles it accordingly.
	 * @param state The system state.
	 */
	public static void flag(SystemState state) {
		if (SystemManager.state == state) {
			return;
		}
		SystemManager.state = state;
		switch (state) {
		case ACTIVE:
		case PRIVATE:
			GameWorld.getMajorUpdateWorker().setStarted(false);
			GameWorld.getMajorUpdateWorker().start();
			break;
		case UPDATING:
			UPDATER.schedule();
			break;
		case TERMINATED:
			TERMINATOR.terminate();
			break;
		}
	}

	/**
	 * Checks if the system is still active (updating keeps the system active
	 * until termination).
	 * @return {@code True} if the state does not equal
	 * {@link SystemState#TERMINATED}.
	 */
	public static boolean isActive() {
		return state != SystemState.TERMINATED;
	}

	/**
	 * Checks if the system is being updated.
	 * @return {@code True} if so.
	 */
	public static boolean isUpdating() {
		return state == SystemState.UPDATING;
	}

	/**
	 * Checks if the system is private, so only developers can connect.
	 * @return {@code True} if so.
	 */
	public static boolean isPrivate() {
		return state == SystemState.PRIVATE;
	}

	/**
	 * Checks if the system has been terminated.
	 * @return {@code True} if so.
	 */
	public static boolean isTerminated() {
		return state == SystemState.TERMINATED;
	}

	/**
	 * Gets the current system state.
	 * @return The state.
	 */
	public static SystemState state() {
		return state;
	}

	/**
	 * Gets the updater.
	 * @return The updater.
	 */
	public static SystemUpdate getUpdater() {
		return UPDATER;
	}

	/**
	 * Gets the terminator.
	 * @return The terminator.
	 */
	public static SystemTermination getTerminator() {
		return TERMINATOR;
	}

	/**
	 * Gets the systemConfig.
	 * @return the systemConfig.
	 */
	public static SystemConfig getSystemConfig() {
		return SYSTEM_CONFIG;
	}

	/**
	 * Gets the encryption.
	 * @return the encryption.
	 */
	public static EncryptionManager getEncryption() {
		return ENCRYPTION;
	}
}