package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The packet context for the contact packets.
 * @author Emperor
 */
public final class ContactContext implements Context {

	/**
	 * The update communication server state type.
	 */
	public static final int UPDATE_STATE_TYPE = 0;

	/**
	 * The update friend type.
	 */
	public static final int UPDATE_FRIEND_TYPE = 1;

	/**
	 * The ignore list type.
	 */
	public static final int IGNORE_LIST_TYPE = 2;

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The contact packet type.
	 */
	private int type;

	/**
	 * The player name.
	 */
	private String name;

	/**
	 * The world id
	 */
	private int worldId;

	/**
	 * Constructs a new {@code ContactContext} {@code Object}.
	 * @param player The player.
	 * @param type The contact packet type.
	 */
	public ContactContext(Player player, int type) {
		this.player = player;
		this.type = type;
	}

	/**
	 * Constructs a new {@code ContactContext} {@code Object}.
	 * @param player The player.
	 * @param name The player name.
	 * @param worldId The world id (0 = offline).
	 */
	public ContactContext(Player player, String name, int worldId) {
		this.player = player;
		this.name = name;
		this.worldId = worldId;
		this.type = UPDATE_FRIEND_TYPE;
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the type.
	 * @return The type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * @param type The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Gets the name.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the online.
	 * @return The online.
	 */
	public boolean isOnline() {
		return worldId > 0;
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