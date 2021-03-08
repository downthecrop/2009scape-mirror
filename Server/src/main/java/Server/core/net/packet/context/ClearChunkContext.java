package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.game.world.map.RegionChunk;
import core.net.packet.Context;

/**
 * The packet context for the clear region chunk outgoing packet.
 * @author Emperor
 */
public final class ClearChunkContext implements Context {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The region chunk to clear.
	 */
	private final RegionChunk chunk;

	/**
	 * Constructs a new {@code ClearChunkContext} {@code Object}.
	 * @param player The player.
	 * @param chunk The chunk to clear.
	 */
	public ClearChunkContext(Player player, RegionChunk chunk) {
		this.player = player;
		this.chunk = chunk;
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the chunk.
	 * @return The chunk.
	 */
	public RegionChunk getChunk() {
		return chunk;
	}

}
