package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Handles the TakiDwarfDialogue dialogue.
 * @author 'Vexia
 */
@Initializable
public class TakiDwarfDialogue extends DialoguePlugin {

	public TakiDwarfDialogue() {

	}

	public TakiDwarfDialogue(Player player) {
		super(player);
	}

	@Override
	public int[] getIds() {
		return new int[] { 7115 };
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {

		switch (stage) {
		case 0:
			interpreter.sendDialogues(player, FacialExpression.OLD_NORMAL, "Hi little fellow.");
			stage = 1;
			break;
		case 1:
			interpreter.sendDialogues(npc, FacialExpression.OLD_NORMAL, "What did you just say to me!?");
			stage = 2;
			break;
		case 2:
			interpreter.sendDialogues(player, FacialExpression.OLD_NORMAL, "Arrr! nothing, nothing at all..");
			stage = 3;
			break;
		case 3:
			end();
			break;
		}

		return true;
	}

	@Override
	public DialoguePlugin newInstance(Player player) {

		return new TakiDwarfDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(npc, FacialExpression.OLD_NORMAL, "Arrr!");
		stage = 0;
		return true;
	}
}
