package core.game.system.communication;

/**
 * Represents a contact.
 * @author Emperor
 */
public final class Contact {

	/**
	 * The username of the contact.
	 */
	private final String username;

	/**
	 * The world the contact is in.
	 */
	private int worldId;

	/**
	 * The contact's rank in the player's clan.
	 */
	private ClanRank rank = ClanRank.FRIEND;

	/**
	 * Constructs a new {@code Contact} {@code Object}
	 * @param username The username.
	 */
	public Contact(String username) {
		this.username = username;
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

	/**
	 * Gets the rank.
	 * @return the rank
	 */
	public ClanRank getRank() {
		return rank;
	}

	/**
	 * Sets the rank.
	 * @param rank the rank to set.
	 */
	public void setRank(ClanRank rank) {
		this.rank = rank;
	}

	/**
	 * Gets the username.
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

}