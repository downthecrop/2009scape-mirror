package org.crandor.net.packet.context;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.object.GameObject;
import org.crandor.net.packet.Context;

/**
 * Represents the build object packet context, <br> which is used for
 * construct/clear object outgoing packet.
 * @author Emperor
 */
public final class BuildObjectContext implements Context {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The list of game objects to send.
	 */
	private final GameObject gameObject;

	/**
	 * Constructs a new {@code BuildObjectContext} {@code Object}.
	 * @param player The player
	 * @param gameObjects The list of game objects to send.
	 */
	public BuildObjectContext(Player player, GameObject gameObject) {
		this.player = player;
		this.gameObject = gameObject;
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the gameObject.
	 * @return The gameObject.
	 */
	public GameObject getGameObject() {
		return gameObject;
	}

}