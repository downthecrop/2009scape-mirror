package core.game.node.entity.lock;

import core.game.world.GameWorld;

/**
 * Represents a lock.
 * @author Emperor
 * @author Aero
 */
public class Lock {

	/**
	 * The expiration of the lock.
	 */
	private int expiration;

	/**
	 * The custom lock elapse.
	 */
	private LockElapse elapse;

	/**
	 * The message to be sent when the lock is called upon.
	 */
	private String message;

	/**
	 * Constructs a new {@code Lock} {@code Object}.
	 */
	public Lock() {
		this(null);
	}

	/**
	 * Constructs a new {@code Lock} {@code Object}.
	 * @param message The message.
	 */
	public Lock(String message) {
		this.message = message;
	}

	/**
	 * Locks for an indefinite time.
	 */
	public void lock() {
		lock(Integer.MAX_VALUE - GameWorld.getTicks());
	}

	/**
	 * Locks this lock.
	 * @param ticks The amount of ticks to lock for.
	 */
	public void lock(int ticks) {
		if (ticks > expiration - GameWorld.getTicks()) {
			this.expiration = GameWorld.getTicks() + ticks;
		}
	}

	/**
	 * Unlocks the lock.
	 */
	public void unlock() {
		this.expiration = 0;
	}

	/**
	 * Checks if this lock is locked.
	 * @return {@code True} if so.
	 */
	public boolean isLocked() {
		return expiration > GameWorld.getTicks();
	}

	/**
	 * Sets the custom lock elapse.
	 * @param elapse The elapse.
	 * @return The lock instance for chaining.
	 */
	public Lock setElapse(LockElapse elapse) {
		this.elapse = elapse;
		return this;
	}

	/**
	 * Gets the custom lock elapse event.
	 * @return The custom lock elapse event.
	 */
	public LockElapse getElapseEvent() {
		return elapse;
	}

	/**
	 * Gets the message.
	 * @return The message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 * @param message The message to set.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}