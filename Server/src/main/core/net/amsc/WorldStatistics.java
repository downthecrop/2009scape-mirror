package core.net.amsc;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds information of a certain game world.
 * @author Emperor
 */
public final class WorldStatistics {

	/**
	 * The world id.
	 */
	private final int id;

	/**
	 * The list of players connected to this world.
	 */
	private final List<String> players = new ArrayList<>(20);

	/**
	 * Constructs a new {@code WorldStatistics} {@Code Object}
	 * @param id The world id.
	 */
	public WorldStatistics(int id) {
		this.id = id;
	}

	/**
	 * Gets the players.
	 * @return the players
	 */
	public List<String> getPlayers() {
		return players;
	}

	/**
	 * Gets the id.
	 * @return the id
	 */
	public int getId() {
		return id;
	}
}