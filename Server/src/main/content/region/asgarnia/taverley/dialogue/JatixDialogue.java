package content.region.asgarnia.taverley.dialogue;

import core.game.dialogue.DialoguePlugin;
import core.game.dialogue.FacialExpression;
import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for the jatix npc.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class JatixDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code JatixDialogue} {@code Object}.
	 */
	public JatixDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code JatixDialogue} {@code Object}.
	 * @param player the player.
	 */
	public JatixDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new JatixDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(npc, FacialExpression.HAPPY, "Hello, adventurer.");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendDialogues(player, FacialExpression.FRIENDLY, "Hello.");
			stage = 1;
			break;
		case 1:
			interpreter.sendDialogues(player, FacialExpression.FRIENDLY, "What are you selling?");
			stage = 2;
			break;
		case 2:
			end();
			npc.openShop(player);
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 587 };
	}
}
