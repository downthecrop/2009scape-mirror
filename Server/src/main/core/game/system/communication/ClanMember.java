package core.game.system.communication;

/**
 * Represents a clan member.
 * @author Emperor
 */
public final class ClanMember {

	/**
	 * Represents the rank of the player.
	 */
	private ClanRank rank = ClanRank.FRIEND;

	/**
	 * Constructs a new {@code ClanMember} {@code Object}.
	 * @param rank The clan rank.
	 */
	public ClanMember(ClanRank rank) {
		this.rank = rank;
	}

	/**
	 * Gets the rank.
	 * @return The rank.
	 */
	public ClanRank getRank() {
		return rank;
	}

	/**
	 * Sets the rank.
	 * @param rank The rank to set.
	 */
	public void setRank(ClanRank rank) {
		this.rank = rank;
	}

}