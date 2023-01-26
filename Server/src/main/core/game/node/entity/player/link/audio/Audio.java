package core.game.node.entity.player.link.audio;

import core.game.node.entity.player.Player;
import core.game.world.map.Location;

import java.util.List;

/**
 * An audio piece to play.
 * @author Vexia
 */
public class Audio {

	/**
	 * The default volume.
	 */
	public static final int VOLUME = 10;

	/**
	 * The default delay.
	 */
	public static final int DELAY = 0;

    /**
     * The default radius in tiles of positional audio in [0,16).
     */
    public static final int RADIUS = 15;

	/**
	 * The id of the audio piece.
	 */
	private final int id;

	/**
	 * The volume.
	 */
	private final int volume;

	/**
	 * The delay.
	 */
	private final int delay;

    /**
     * The radius.
     */
    private final int radius;

	/**
	 * Constructs a new {@Code Audio} {@Code Object}
	 * @param id the id.
	 * @param volume the volume.
	 * @param delay the delay.
	 * @param radius the radius.
	 */
	public Audio(int id, int volume, int delay, int radius) {
		this.id = id;
		this.volume = volume;
		this.delay = delay;
		this.radius = radius;
	}

	/**
	 * Constructs a new {@Code Audio} {@Code Object}
	 * @param id the id.
	 * @param volume the volume.
	 * @param delay the delay.
	 */
	public Audio(int id, int volume, int delay) {
		this(id, volume, delay, RADIUS);
	}

	/**
	 * Constructs a new {@Code Audio} {@Code Object}
	 * @param id the id.
	 * @param volume the volume.
	 */
	public Audio(int id, int volume) {
		this(id, volume, DELAY);
	}

	/**
	 * Constructs a new {@Code Audio} {@Code Object}
	 * @param id the id.
	 */
	public Audio(int id) {
		this(id, VOLUME, 0);
	}

	/**
	 * Sends an audio through the manager.
	 * @param player the player.
	 * @param global the global.
	 */
	public void send(Player player, boolean global) {
        send(player, global, null);
    }

	public void send(Player player, boolean global, Location loc) {
		player.getAudioManager().send(this, global, loc);
	}

	/**
	 * Sends this audio.
	 * @param player the player.
	 */
	public void send(Player player) {
		send(player, false);
	}

	/**
	 * Sends the sound to a list of players.
	 * @param players the players.
	 */
	public void send(List<Player> players) {
		for (Player p : players) {
			if (p == null) {
				continue;
			}
			send(p, false);
		}
	}

	/**
	 * Gets the id.
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the volume.
	 * @return the volume
	 */
	public int getVolume() {
		return volume;
	}

	/**
	 * Gets the delay.
	 * @return the delay
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Gets the radius.
	 * @return the radius
	 */
	public int getRadius() {
		return radius;
	}
}
