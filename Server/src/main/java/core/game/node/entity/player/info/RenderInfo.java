package core.game.node.entity.player.info;

import core.ServerConstants;
import core.game.node.entity.Entity;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;

import java.util.LinkedList;
import java.util.List;

/**
 * Holds a player's render information.
 * @author Emperor
 */
public final class RenderInfo {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The list of local players.
	 */
	private List<Player> localPlayers = new LinkedList<Player>();

	/**
	 * The list of local NPCs.
	 */
	private List<NPC> localNpcs = new LinkedList<NPC>();

	/**
	 * The appearance time stamps (in millisecond).
	 */
	private final long[] appearanceStamps = new long[ServerConstants.MAX_PLAYERS];

	/**
	 * The entities requiring a mask update.
	 */
	private Entity[] maskUpdates = new Entity[256];

	/**
	 * The mask update count.
	 */
	private int maskUpdateCount;

	/**
	 * The last location of this player.
	 */
	private Location lastLocation;

	/**
	 * If the player has just logged in.
	 */
	private boolean onFirstCycle = true;

	/**
	 * If the player has prepared appearance data this cycle.
	 */
	private boolean preparedAppearance;

	/**
	 * Constructs a new {@code RenderInfo} {@code Object}.
	 * @param player The player.
	 */
	public RenderInfo(Player player) {
		this.player = player;
	}

	/**
	 * Updates the player rendering information.
	 */
	public void updateInformation() {
		onFirstCycle = false;
		lastLocation = player.getLocation();
		preparedAppearance = false;
	}

	/**
	 * Registers an entity requiring a mask update.
	 * @param entity The entity.
	 */
	public void registerMaskUpdate(Entity entity) {
		maskUpdates[maskUpdateCount++] = entity;
	}

	/**
	 * Gets the localNpcs.
	 * @return The localNpcs.
	 */
	public List<NPC> getLocalNpcs() {
		return localNpcs;
	}

	/**
	 * Sets the localNpcs.
	 * @param localNpcs The localNpcs to set.
	 */
	public void setLocalNpcs(List<NPC> localNpcs) {
		this.localNpcs = localNpcs;
	}

	/**
	 * Gets the onFirstCycle.
	 * @return The onFirstCycle.
	 */
	public boolean isOnFirstCycle() {
		return onFirstCycle;
	}

	/**
	 * Sets the onFirstCycle.
	 * @param onFirstCycle The onFirstCycle to set.
	 */
	public void setOnFirstCycle(boolean onFirstCycle) {
		this.onFirstCycle = onFirstCycle;
	}

	/**
	 * Gets the lastLocation.
	 * @return The lastLocation.
	 */
	public Location getLastLocation() {
		return lastLocation;
	}

	/**
	 * Sets the lastLocation.
	 * @param lastLocation The lastLocation to set.
	 */
	public void setLastLocation(Location lastLocation) {
		this.lastLocation = lastLocation;
	}

	/**
	 * Gets the localPlayers.
	 * @return The localPlayers.
	 */
	public List<Player> getLocalPlayers() {
		return localPlayers;
	}

	/**
	 * Sets the localPlayers.
	 * @param localPlayers The localPlayers to set.
	 */
	public void setLocalPlayers(List<Player> localPlayers) {
		this.localPlayers = localPlayers;
	}

	/**
	 * Gets the appearanceStamps.
	 * @return The appearanceStamps.
	 */
	public long[] getAppearanceStamps() {
		return appearanceStamps;
	}

	/**
	 * Gets the maskUpdateCount.
	 * @return The maskUpdateCount.
	 */
	public int getMaskUpdateCount() {
		return maskUpdateCount;
	}

	/**
	 * Sets the maskUpdateCount.
	 * @param maskUpdateCount The maskUpdateCount to set.
	 */
	public void setMaskUpdateCount(int maskUpdateCount) {
		this.maskUpdateCount = maskUpdateCount;
	}

	/**
	 * Gets the maskUpdates.
	 * @return The maskUpdates.
	 */
	public Entity[] getMaskUpdates() {
		return maskUpdates;
	}

	/**
	 * Sets the maskUpdates.
	 * @param maskUpdates The maskUpdates to set.
	 */
	public void setMaskUpdates(Entity[] maskUpdates) {
		this.maskUpdates = maskUpdates;
	}

	/**
	 * Sets the prepared appearance flag.
	 * @param prepared If the player has prepared appearance setting this cycle.
	 */
	public void setPreparedAppearance(boolean prepared) {
		this.preparedAppearance = prepared;
	}

	/**
	 * Checks if the player has prepared appearance data this cycle.
	 * @return {@code True} if so.
	 */
	public boolean preparedAppearance() {
		return preparedAppearance;
	}
}