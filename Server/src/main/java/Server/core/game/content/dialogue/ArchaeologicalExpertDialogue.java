package core.game.content.dialogue;

import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Handles the archaeologicals expert dialogue.
 * @author Aero
 * @version 1.0
 */
@Initializable
public class ArchaeologicalExpertDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code ArchaeologicalExpertDialogue} {@code Object}.
	 */
	public ArchaeologicalExpertDialogue() {
		/**
		 * Empty to stop instantiation.
		 */
	}

	/**
	 * Constructs a new {@code ArchaeologicalExpertDialogue} {@code Object}.
	 * @param player The player to construct the class for.
	 */
	public ArchaeologicalExpertDialogue(final Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new ArchaeologicalExpertDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		return false;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 619 };
	}

}
