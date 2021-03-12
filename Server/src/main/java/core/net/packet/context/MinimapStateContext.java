package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * Represents the context used for the Minimap State packet.
 * @author Emperor
 */
public class MinimapStateContext implements Context {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The minimap state to set.
	 */
	private final int state;

	/**
	 * Constructs a new {@code MinimapStateContext} {@code Object}.
	 * @param player The player.
	 * @param state The minimap state to set.
	 */
	public MinimapStateContext(Player player, int state) {
		this.player = player;
		this.state = state;
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the state.
	 * @return The state.
	 */
	public int getState() {
		return state;
	}

}