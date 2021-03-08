package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Rerpesents the dialogue plugin used for the celburg npc.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class CeolburgDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code CeolburgDialogue} {@code Object}.
	 */
	public CeolburgDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code CeolburgDialogue} {@code Object}.
	 * @param player the player.
	 */
	public CeolburgDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new CeolburgDialogue(player);
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
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Trolls!");
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
		return new int[] { 1089 };
	}
}
