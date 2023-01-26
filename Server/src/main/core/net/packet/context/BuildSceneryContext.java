package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.net.packet.Context;

/**
 * Represents the build object packet context, <br> which is used for
 * construct/clear object outgoing packet.
 * @author Emperor
 */
public final class BuildSceneryContext implements Context {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The list of scenerys to send.
	 */
	private final Scenery scenery;

	/**
	 * Constructs a new {@code BuildObjectContext} {@code Object}.
	 * @param player The player
	 * @param scenery the scenery to send.
	 */
	public BuildSceneryContext(Player player, Scenery scenery) {
		this.player = player;
		this.scenery = scenery;
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the gameObject.
	 * @return The gameObject.
	 */
	public Scenery getScenery() {
		return scenery;
	}

}