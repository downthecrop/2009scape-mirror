package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Handles the SmithingApparenticeDialogue dialogue.
 * @author 'Vexia
 */
@Initializable
public class SmithingApparenticeDialogue extends DialoguePlugin {

	public SmithingApparenticeDialogue() {

	}

	public SmithingApparenticeDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {

		return new SmithingApparenticeDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Can you teach me the basics of smelting please?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "You'll need to have mined some ore to smelt first. Go", "see the mining tutor to the south if you're not sure", "how to do this.");
			stage = 1;
			break;
		case 1:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 7959,4904 };
	}
}
