package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the falador gardener dialogue plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class FaladorGardenerDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code FaladorGardenerDialogue} {@code Object}.
	 */
	public FaladorGardenerDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code FaladorGardenerDialogue} {@code Object}.
	 * @param player the player.
	 */
	public FaladorGardenerDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new FaladorGardenerDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Hello.");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Oi'm busy. If tha' wants owt, tha' can go find Wyson.", "He's ta boss 'round here. And,", "KEEP YE' TRAMPIN' FEET OFF MA'FLOWERS!");
			stage = 1;
			break;
		case 1:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Right...");
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
		return new int[] { 1217, 3234 };
	}
}
