package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The context implementation used for the InteractionOption outgoing packet.
 * @author Emperor
 */
public final class InteractionOptionContext implements Context {

	/**
	 * The player reference.
	 */
	private final Player player;

	/**
	 * The index.
	 */
	private final int index;

	private boolean remove = false;

	/**
	 * The name.
	 */
	private final String name;

	/**
	 * Constructs a new {@code InteractionOptionContext} {@code Object}.
	 * @param player The player.
	 * @param index The index.
	 * @param name The option name.
	 */
	public InteractionOptionContext(Player player, int index, String name) {
		this.player = player;
		this.index = index;
		this.name = name;
		this.remove = false;
	}

	public InteractionOptionContext(Player player, int index, String name, boolean remove){
		this.player = player;
		this.index = index;
		this.name = name;
		this.remove = remove;
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	public boolean isRemove() {
		return remove;
	}

	/**
	 * Gets the index.
	 * @return The index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets the name.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

}