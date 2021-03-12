package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * Represents the set walk-to option context.
 * @author Emperor
 */
public final class WalkOptionContext implements Context {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The option.
	 */
	private final String option;

	/**
	 * Constructs a new {@code WalkOptionContext} {@code Object}.
	 * @param player The player.
	 * @param option The option name.
	 */
	public WalkOptionContext(Player player, String option) {
		this.player = player;
		this.option = option;
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the option.
	 * @return The option.
	 */
	public String getOption() {
		return option;
	}

}