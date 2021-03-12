package core.game.content.dialogue;

import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the white knight dialogue.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class WhiteKnightDialoguePlugin extends DialoguePlugin {

	/**
	 * The NPC ids that use this dialogue plugin.
	 */
	private static final int[] NPC_IDS = { 660 };

	/**
	 * Constructs a new {@code WhiteKnightDialoguePlugin} {@code Object}.
	 */
	public WhiteKnightDialoguePlugin() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code WhiteKnightDialoguePlugin} {@code Object}.
	 * @param player the player.
	 */
	public WhiteKnightDialoguePlugin(Player player) {
		super(player);
	}

	@Override
	public boolean open(Object... args) {
		interpreter.sendDialogue("He is too busy dancing to talk!");
		return true;
	}

	@Override
	public int[] getIds() {
		return NPC_IDS;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			end();
			break;
		}
		return true;
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new WhiteKnightDialoguePlugin(player);
	}

}
