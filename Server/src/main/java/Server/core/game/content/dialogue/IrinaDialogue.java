package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for the irina npc.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class IrinaDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code IrinaDialogue} {@code Object}.
	 */
	public IrinaDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code IrinaDialogue} {@code Object}.
	 * @param player the player.
	 */
	public IrinaDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new IrinaDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Out of my way, punk.");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 6035 };
	}
}
