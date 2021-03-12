package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the hygd dialogue plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class HygdDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code HygdDialogue} {@code Object}.
	 */
	public HygdDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code HygdDialogue} {@code Object}.
	 * @param player the player.
	 */
	public HygdDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new HygdDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Hi!");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Hi!");
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
		return new int[] { 1088 };
	}
}
