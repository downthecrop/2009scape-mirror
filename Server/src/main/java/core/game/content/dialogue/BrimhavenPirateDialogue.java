package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the brimhaven pirate dialogue.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class BrimhavenPirateDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code BrimhavenPirateDialogue} {@code Object}.
	 */
	public BrimhavenPirateDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code BrimhavenPirateDialogue} {@code Object}.
	 * @param player the player.
	 */
	public BrimhavenPirateDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new BrimhavenPirateDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Hello!");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Man overboard!");
			stage = 2;
			break;
		case 2:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 183, 6349, 6350, 6346, 6347, 6348, 799 };
	}
}
