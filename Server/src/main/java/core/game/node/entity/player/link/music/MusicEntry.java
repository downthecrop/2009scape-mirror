package core.game.node.entity.player.link.music;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a song.
 * @author Emperor
 */
public final class MusicEntry {

	/**
	 * The songs mapping.
	 */
	private static final Map<Integer, MusicEntry> SONGS = new HashMap<>();

	/**
	 * The music id.
	 */
	private final int id;

	/**
	 * The song name.
	 */
	private final String name;

	/**
	 * The index in the list.
	 */
	private final int index;

	/**
	 * Constructs a new {@code MusicEntry} {@code Object}.
	 * @param id the music id.
	 * @param name The name.
	 * @param index The list index.
	 */
	public MusicEntry(int id, String name, int index) {
		this.id = id;
		this.name = name;
		this.index = index;
	}

	/**
	 * Gets the song for the given music id.
	 * @param id The music id.
	 * @return The song.
	 */
	public static MusicEntry forId(int id) {
		return SONGS.get(id);
	}

	/**
	 * Gets the id.
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the name.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the index.
	 * @return The index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets the songs.
	 * @return The songs.
	 */
	public static Map<Integer, MusicEntry> getSongs() {
		return SONGS;
	}
}