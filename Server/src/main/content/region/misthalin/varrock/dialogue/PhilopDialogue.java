package content.region.misthalin.varrock.dialogue;

import core.game.dialogue.DialoguePlugin;
import core.game.dialogue.FacialExpression;
import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Handles the PhilopDialogue dialogue.
 * @author 'Vexia
 */
@Initializable
public class PhilopDialogue extends DialoguePlugin {

	public PhilopDialogue() {

	}

	public PhilopDialogue(Player player) {
		super(player);
	}

	@Override
	public int[] getIds() {
		return new int[] { 782 };
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {

		switch (stage) {
		case 0:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Gwwrr!");
			stage = 1;
			break;
		case 1:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Err, hello there. What's that you have there?");
			stage = 2;
			break;
		case 2:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Gwwwrrr! Dwa-gon Gwwwwrrrr!");
			stage = 3;
			break;
		case 3:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Enjoy playing with your dragon, then.");
			stage = 4;
			break;
		case 4:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Gwwwrrr!");
			stage = 5;
			break;
		case 5:
			end();
			break;
		}

		return true;
	}

	@Override
	public DialoguePlugin newInstance(Player player) {

		return new PhilopDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Hello, what's your name?");
		stage = 0;
		return true;
	}
}
