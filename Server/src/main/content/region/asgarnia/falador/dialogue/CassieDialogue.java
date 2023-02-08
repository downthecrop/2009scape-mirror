package content.region.asgarnia.falador.dialogue;

import core.game.dialogue.DialoguePlugin;
import core.game.dialogue.FacialExpression;
import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for the cassie npc.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class CassieDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code CassieDialogue} {@code Object}.
	 */
	public CassieDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code CassieDialogue} {@code Object}.
	 * @param player the player.
	 */
	public CassieDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new CassieDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(npc, FacialExpression.HAPPY, "I buy and sell shields; do you want to trade?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendOptions("Select an Option", "Yes, please.", "No, thank you.");
			stage = 1;
			break;
		case 1:
			switch (buttonId) {
			case 1:
				end();
				npc.openShop(player);
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.HAPPY, "No, thank you.");
				stage = 20;
				break;
			}
			break;
		case 20:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 577 };
	}
}
