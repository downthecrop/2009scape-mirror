package core.game.content.dialogue;

import core.game.node.entity.player.Player;

/**
 * A dialogue reward.
 * @author Vexia
 */
public interface DialogueAction {

	/**
	 * Handles a dialogue click.
	 * @param player the player.
	 * @param buttonId the buttonId.
	 */
	public void handle(Player player, int buttonId);

}
