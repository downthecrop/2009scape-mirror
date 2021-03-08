package core.game.node.entity.player.link.audio;

import core.game.node.entity.player.Player;

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
	 * Constructs a new {@Code Audio} {@Code Object}
	 * @param id the id.
	 * @param volume the volume.
	 * @param delay the delay.
	 */
	public Audio(int id, int volume, int delay) {
		this.id = id;
		this.volume = volume;
		this.delay = delay;
	}

	/**
	 * Constructs a new {@Code Audio} {@Code Object}
	 * @param id the id.
	 * @param volume the volume.
	 */
	public Audio(int id, int volume) {
		this(id, volume, 0);
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
		player.getAudioManager().send(this, global);
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

}
