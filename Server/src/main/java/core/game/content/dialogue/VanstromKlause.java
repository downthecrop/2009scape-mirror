package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Handles the VanstromKlause dialogue.
 * @author 'Vexia
 */
@Initializable
public class VanstromKlause extends DialoguePlugin {

	public VanstromKlause() {

	}

	public VanstromKlause(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {

		return new VanstromKlause(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Hello there, how goes it stranger?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Quite well thanks for asking, how about you?");
			stage = 1;
			break;
		case 1:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Quite well my self.");
			stage = 2;
			break;
		case 2:
			end();// todo real dial at this part.
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 2020 };
	}
}
