package content.region.misthalin.varrock.dialogue;

import core.game.dialogue.DialoguePlugin;
import core.game.dialogue.FacialExpression;
import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Kanel - Child in Gertrude's House
 */
@Initializable
public final class KanelDialogue extends DialoguePlugin {
	// https://www.youtube.com/watch?v=ANI7DaRVEbg
	/**
	 * Constructs a new {@code KanelDialogue} {@code Object}.
	 */
	public KanelDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code KanelDialogue} {@code Object}.
	 * @param player the player.
	 */
	public KanelDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new KanelDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(player, FacialExpression.FRIENDLY, "Hello there.");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {

		switch (stage) {
		case 0:
			interpreter.sendDialogues(npc, FacialExpression.CHILD_THINKING, "Hel-lo?");
			stage = 1;
			break;
		case 1:
			interpreter.sendDialogues(player, FacialExpression.NEUTRAL, "Right. Goodbye.");
			stage = 2;
			break;
		case 2:
			interpreter.sendDialogues(npc, FacialExpression.CHILD_THINKING, "Bye?");
			stage = 3;
			break;
		case 3:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 784 };
	}
}
