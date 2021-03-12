package core.game.node.entity.player.link.music;

import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.world.map.zone.Zone;
import core.game.world.map.zone.ZoneBorders;

/**
 * Represents a music zone.
 * @author Emperor
 */
public final class MusicZone implements Zone {

	/**
	 * The music id.
	 */
	private final int musicId;

	/**
	 * The zone borders.
	 */
	private final ZoneBorders borders;

	/**
	 * Constructs a new {@code MusicZone} {@code Object}.
	 * @param musicId The music id.
	 */
	public MusicZone(int musicId, ZoneBorders borders) {
		this.musicId = musicId;
		this.borders = borders;
	}

	@Override
	public boolean enter(Entity e) {
		if (!(e instanceof Player)) {
			throw new IllegalStateException("Music is for players only!");
		}
		Player player = (Player) e;
		player.getMusicPlayer().unlock(musicId);
		return true;
	}

	@Override
	public boolean leave(Entity e, boolean logout) {
		return true;
	}

	/**
	 * Gets the musicId.
	 * @return The musicId.
	 */
	public int getMusicId() {
		return musicId;
	}

	/**
	 * Gets the borders.
	 * @return The borders.
	 */
	public ZoneBorders getBorders() {
		return borders;
	}

}