package core.game.system.communication;

import core.game.node.entity.player.Player;
import rs09.game.world.GameWorld;

/**
 * Represents a player in a clan chat.
 * @author Emperor
 */
public class ClanEntry {

	/**
	 * The name.
	 */
	private final String name;

	/**
	 * The player.
	 */
	private Player player;

	/**
	 * The world id.
	 */
	private int worldId;

	/**
	 * Constructs a new {@code ClanEntry} {@code Object}
	 * @param player The player.
	 */
	public ClanEntry(Player player) {
		this.player = player;
		this.name = player.getName();
		this.worldId = GameWorld.getSettings().getWorldId();
	}

	/**
	 * Constructs a new {@code ClanEntry} {@code Object}
	 * @param name The player's name.
	 * @param worldId The world id.
	 */
	public ClanEntry(String name, int worldId) {
		this.name = name;
		this.worldId = worldId;
	}

	@Override
	public boolean equals(Object o) {
		if(o == null) return false;
		ClanEntry e = (ClanEntry) o;
		if (name != null && !name.equals(e.name)) {
			return false;
		}
		return e.player == player;
	}

	/**
	 * Gets the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the player.
	 * @param player The player.
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Gets the player.
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the worldId.
	 * @return the worldId
	 */
	public int getWorldId() {
		return worldId;
	}

	/**
	 * Sets the worldId.
	 * @param worldId the worldId to set.
	 */
	public void setWorldId(int worldId) {
		this.worldId = worldId;
	}
}