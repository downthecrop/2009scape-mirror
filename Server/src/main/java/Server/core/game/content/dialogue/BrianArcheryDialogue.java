package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used to handle the brain archery npc.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class BrianArcheryDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code BrianArcheryDialogue} {@code Object}.
	 */
	public BrianArcheryDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code BrianArcheryDialogue} {@code Object}.
	 * @param player the player.
	 */
	public BrianArcheryDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new BrianArcheryDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Would you like to buy some archery equipment?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendOptions("Select an Option", "No thanks, I've got all the archery equipment I need.", "Let's see what you've got, then.");
			stage = 1;
			break;
		case 1:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "No, thanks, I've got all the archery equipment I need.");
				stage = 10;
				break;
			case 2:
				end();
				npc.openShop(player);
				break;
			}
			break;
		case 10:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Okay. Fare well on your travels.");
			stage = 11;
			break;
		case 11:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 1860 };
	}
}
