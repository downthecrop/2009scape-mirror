package plugin.dialogue;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.node.entity.player.Player;

/**
 * Handles Eblis's dialogue.
 * @author Aero
 * @version 1.0
 */
@InitializablePlugin
public class EblisDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code EblisDialogue} {@code Object}.
	 */
	public EblisDialogue() {
		/**
		 * Empty to stop instantiation.
		 */
	}

	/**
	 * Constructs a new {@code EblisDialogue} {@code Object}.
	 * @param player The player to construct the class for.
	 */
	public EblisDialogue(final Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new EblisDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
	
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 1923 };
	}

}
