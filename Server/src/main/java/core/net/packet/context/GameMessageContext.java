package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The game message packet context.
 * @author Emperor
 */
public final class GameMessageContext implements Context {

	/**
	 * The player reference.
	 */
	private Player player;

	/**
	 * The game message.
	 */
	private String message;

	/**
	 * Construct a new {@code GameMessageContext} {@code Object}.
	 * @param player The player.
	 * @param message The game message.
	 */
	public GameMessageContext(Player player, String message) {
		this.player = player;
		this.message = message;
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	/**
	 * Get the game message.
	 * @return The game message.
	 */
	public String getMessage() {
		return message;
	}
}
