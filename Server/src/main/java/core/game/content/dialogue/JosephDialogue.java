package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for the joseph npc.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class JosephDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code JosephDialogue} {@code Object}.
	 */
	public JosephDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code JosephDialogue} {@code Object}.
	 * @param player the player.
	 */
	public JosephDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new JosephDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "I have no interest in talking to a pathetic meat bag like", "yourself.");
		stage = 0;
		return true;
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
	public int[] getIds() {
		return new int[] { 6029 };
	}
}
