package core.game.node.entity.lock;

import core.game.node.Node;
import rs09.game.world.GameWorld;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds the reward locks.
 * @author Emperor
 * @author Aero
 */
public final class ActionLocks {

	/**
	 * The movement lock.
	 */
	private Lock movementLock = new Lock();

	/**
	 * The teleporting lock.
	 */
	private Lock teleportLock = new Lock();

	/**
	 * The component lock.
	 */
	private Lock componentLock = new Lock();

	/**
	 * The interaction lock.
	 */
	private Lock interactionLock = new Lock();

	/**
	 * The equipment lock.
	 */
	private Lock equipmentLock = null;

	/**
	 * A mapping of custom locks (used for eg. food delay).
	 */
	private Map<String, Lock> customLocks;

	/**
	 * Constructs a new {@code ActionLocks} {@code Object}.
	 */
	public ActionLocks() {
		this.customLocks = new HashMap<>();
	}

	/**
	 * Locks all default actions (movement, teleport & interaction) for
	 * indefinite time.
	 */
	public void lock() {
		lock(Integer.MAX_VALUE - GameWorld.getTicks());
	}

	/**
	 * Locks all the default actions (movement, teleport & interaction).
	 * @param ticks The amount of ticks to lock for.
	 */
	public void lock(int ticks) {
		lockMovement(ticks);
		lockTeleport(ticks);
		lockInteractions(ticks);
	}

	/**
	 * Unlocks the default actions.
	 */
	public void unlock() {
		unlockMovement();
		unlockTeleport();
		unlockInteraction();
	}

	/**
	 * Locks the movement actions.
	 * @param ticks The amount of ticks to lock for.
	 */
	public void lockMovement(int ticks) {
		movementLock.lock(ticks);
	}

	/**
	 * Unlocks the movement lock.
	 */
	public void unlockMovement() {
		movementLock.unlock();
	}

	/**
	 * Checks if movement actions are locked.
	 * @return {@code True} if so.
	 */
	public boolean isMovementLocked() {
		return movementLock.isLocked();
	}

	/**
	 * Locks the teleport actions.
	 * @param ticks The amount of ticks to lock for.
	 */
	public void lockTeleport(int ticks) {
		teleportLock.lock(ticks);
	}

	/**
	 * Unlocks the teleport lock.
	 */
	public void unlockTeleport() {
		teleportLock.unlock();
	}

	/**
	 * Checks if teleport actions are locked.
	 * @return {@code True} if so.
	 */
	public boolean isTeleportLocked() {
		return teleportLock.isLocked();
	}

	/**
	 * Locks the component actions.
	 * @param ticks The amount of ticks to lock for.
	 */
	public void lockComponent(int ticks) {
		componentLock.lock(ticks);
	}

	/**
	 * Unlocks the component lock.
	 */
	public void unlockComponent() {
		componentLock.unlock();
	}

	/**
	 * Checks if component actions are locked.
	 * @return {@code True} if so.
	 */
	public boolean isComponentLocked() {
		return componentLock.isLocked();
	}

	/**
	 * Locks the interaction actions.
	 * @param ticks The amount of ticks to lock for.
	 */
	public void lockInteractions(int ticks) {
		interactionLock.lock(ticks);
	}

	/**
	 * Unlocks the interaction lock.
	 */
	public void unlockInteraction() {
		interactionLock.unlock();
	}

	/**
	 * Checks if interaction actions are locked.
	 * @return {@code True} if so.
	 */
	public boolean isInteractionLocked() {
		return interactionLock.isLocked();
	}

	/**
	 * Locks the lock for the given key.
	 * @param key The key.
	 * @param ticks The amount of ticks to lock for.
	 * @return The lock object.
	 */
	public Lock lock(String key, int ticks) {
		return lock(key, ticks, null);
	}

	/**
	 * Locks the lock for the given key.
	 * @param key The key.
	 * @param ticks The amount of ticks to lock for.
	 * @param elapse The elapse event.
	 * @return The lock object.
	 */
	public Lock lock(String key, int ticks, LockElapse elapse) {
		Lock lock = customLocks.get(key);
		if (lock == null) {
			customLocks.put(key, lock = new Lock());
		}
		lock.setElapse(elapse).lock(ticks);
		return lock;
	}

	/**
	 * Unlocks the lock for the given key and removes it from the cache.
	 * @param key The key.
	 * @param node The node.
	 */
	public void unlock(String key, Node node) {
		unlock(key, true, node);
	}

	/**
	 * Unlocks the lock for the given key.
	 * @param key The key.
	 * @param cacheRemove If the lock can be removed from the cache.
	 * @param node The node.
	 */
	public void unlock(String key, boolean cacheRemove, Node node) {
		Lock lock = customLocks.get(key);
		if (lock != null) {
			lock.unlock();
			if (lock.getElapseEvent() != null) {
				lock.getElapseEvent().elapse(node, lock);
			}
			if (cacheRemove) {
				customLocks.remove(key);
			}
		}
	}

	/**
	 * Checks if the lock for the given key is currently locked.
	 * @param key The key.
	 * @return {@code True} if so.
	 */
	public boolean isLocked(String key) {
		Lock lock = customLocks.get(key);
		return lock != null && lock.isLocked();
	}

	/**
	 * Gets the equipmentLock.
	 * @return The equipmentLock.
	 */
	public Lock getEquipmentLock() {
		return equipmentLock;
	}

	/**
	 * Sets the equipmentLock.
	 * @param equipmentLock The equipmentLock to set.
	 */
	public void setEquipmentLock(Lock equipmentLock) {
		this.equipmentLock = equipmentLock;
	}

}