package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the hooknodes jack dialogue plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class HooknosedJackDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code HooknosedJackDialogue} {@code Object}
	 */
	public HooknosedJackDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code HooknosedJackDialogue} {@code Object}.
	 * @param player the player.
	 */
	public HooknosedJackDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new HooknosedJackDialogue(player);
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
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "What?");
			stage = 1;
			break;
		case 1:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Actually I've got no time for this. I don't want to talk to", "you.");
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
		return new int[] { 2948 };
	}

}
