package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the fenitas dialogue plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class FenitasDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code FenitasDialogue} {@code Object}.
	 */
	public FenitasDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code FenitasDialogue} {@code Object}.
	 * @param player the player.
	 */
	public FenitasDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new FenitasDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Would you like to buy some cooking equipment?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Yes please.");
			stage = 1;
			break;
		case 1:
			end();
			npc.openShop(player);
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 593 };
	}
}
