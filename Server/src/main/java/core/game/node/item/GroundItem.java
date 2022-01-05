package core.game.node.item;

import core.game.node.entity.player.Player;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;

/**
 * Represents a ground item.
 * @author Emperor
 */
public class GroundItem extends Item {

	/**
	 * The player who dropped this item.
	 */
	private Player dropper;

	/**
	 * The amount of ticks.
	 */
	private int ticks;

	/**
	 * The amount of ticks when to remove this item.
	 */
	private int decayTime;

	/**
	 * If the item should remain private.
	 */
	private boolean remainPrivate;

	/**
	 * If the ground item has been removed.
	 */
	private boolean removed;

	/**
	 * Constructs a new {@code GroundItem} {@code Object}.
	 * @param item The item
	 */
	public GroundItem(Item item) {
		this(item, null, null);
	}

	/**
	 * Constructs a new {@code GroundItem} {@code Object}.
	 * @param item The item.
	 * @param location The location.
	 */
	public GroundItem(Item item, Location location) {
		this(item, location, 200, null);
	}

	/**
	 * Constructs a new {@code GroundItem} {@code Object}.
	 * @param item The item.
	 * @param location The location.
	 * @param player The player who dropped this item.
	 */
	public GroundItem(Item item, Location location, Player player) {
		this(item, location, 200, player);
	}

	/**
	 * Constructs a new {@code GroundItem} {@code Object}.
	 * @param item The item.
	 * @param location The location.
	 * @param player The player who dropped this item.
	 */
	public GroundItem(Item item, Location location, int decay, Player player) {
		super(item.getId(), item.getAmount(), item.getCharge());
		super.location = location;
		super.index = -1;
		super.interaction.setDefault();
		this.dropper = player;
		this.ticks = GameWorld.getTicks();
		this.decayTime = ticks + decay;
	}

	/**
	 * Used to respawn the ground item.
	 */
	public void respawn() {

	}

	/**
	 * Checks if the ground item is private.
	 * @return {@code True} if so.
	 */
	public boolean isPrivate() {
		return remainPrivate || (decayTime - GameWorld.getTicks() > 100);
	}

	@Override
	public boolean isActive() {
		return removed || GameWorld.getTicks() < decayTime;
	}

	/**
	 * Checks if this item was dropped by the player.
	 * @param p The player.
	 * @return {@code True} if so.
	 */
	public boolean droppedBy(Player p) {
		if (dropper != null && p.getDetails().getUid() == dropper.getDetails().getUid()) {
			dropper = p;
			return true;
		}
		return false;
	}

	/**
	 * Gets the dropper.
	 * @return The dropper.
	 */
	public Player getDropper() {
		return dropper;
	}

	/**
	 * Sets the dropper.
	 * @param player The player who dropped this item.
	 */
	public void setDropper(Player player) {
		this.dropper = player;
	}

	/**
	 * Gets the ticks.
	 * @return The ticks.
	 */
	public int getTicks() {
		return ticks;
	}

	/**
	 * Gets the decayTime.
	 * @return The decayTime.
	 */
	public int getDecayTime() {
		return decayTime;
	}

	/**
	 * Sets the decayTime.
	 * @param decayTime The decayTime to set.
	 */
	public void setDecayTime(int decayTime) {
		this.decayTime = GameWorld.getTicks() + decayTime;
	}

	/**
	 * Gets the autoSpawn.
	 * @return The autoSpawn.
	 */
	public boolean isAutoSpawn() {
		return false;
	}

	/**
	 * Gets the remainPrivate.
	 * @return The remainPrivate.
	 */
	public boolean isRemainPrivate() {
		return remainPrivate;
	}

	/**
	 * Sets the remainPrivate.
	 * @param remainPrivate The remainPrivate to set.
	 */
	public void setRemainPrivate(boolean remainPrivate) {
		this.remainPrivate = remainPrivate;
	}

	/**
	 * Gets the removed.
	 * @return The removed.
	 */
	public boolean isRemoved() {
		return removed;
	}

	/**
	 * Sets the removed.
	 * @param removed The removed to set.
	 */
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	@Override
	public String toString() {
		return "GroundItem [dropper=" + (dropper != null ? dropper.getUsername() : dropper) + ", ticks=" + ticks + ", decayTime=" + decayTime + ", remainPrivate=" + remainPrivate + ", removed=" + removed + "]";
	}
}