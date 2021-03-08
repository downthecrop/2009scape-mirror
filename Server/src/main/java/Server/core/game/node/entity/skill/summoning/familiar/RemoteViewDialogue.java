package core.game.node.entity.skill.summoning.familiar;

import core.plugin.Initializable;
import core.game.content.dialogue.DialogueInterpreter;
import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.skill.summoning.familiar.RemoteViewer.ViewType;
import core.game.node.entity.player.Player;

/**
 * Handles the remote viewing dialogue.
 * @author Vexia
 */
@Initializable
public final class RemoteViewDialogue extends DialoguePlugin {

	/**
	 * The familiar instance.
	 */
	private Familiar familiar;

	/**
	 * Constructs a new {@code RemoteViewDialogue} {@code Object}.
	 */
	public RemoteViewDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code RemoteViewDialogue} {@code Object}.
	 * @param player the player.
	 */
	public RemoteViewDialogue(final Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new RemoteViewDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		familiar = (Familiar) args[0];
		options("North", "East", "Sotuh", "West", "Straight up");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		end();
		RemoteViewer.create(player, familiar, familiar.getViewAnimation(), ViewType.values()[-1 + buttonId]).startViewing();
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { DialogueInterpreter.getDialogueKey(RemoteViewer.DIALOGUE_NAME) };
	}

}
