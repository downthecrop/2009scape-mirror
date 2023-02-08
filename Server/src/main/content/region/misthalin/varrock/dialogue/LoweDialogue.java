package content.region.misthalin.varrock.dialogue;

import core.game.dialogue.DialoguePlugin;
import core.game.dialogue.FacialExpression;
import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for lowe.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class LoweDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code LoweDialogue} {@code Object}.
	 */
	public LoweDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code LoweDialogue} {@code Object}.
	 * @param player the player.
	 */
	public LoweDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new LoweDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(npc, FacialExpression.HAPPY, "Welcome to Lowe's Archery Emporium. Do you want", "to see my wares?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendOptions("Select an Option", "Yes, please.", "No, I prefer to bash things close up.");
			stage = 1;
			break;
		case 1:
			switch (buttonId) {
			case 1:
				npc.openShop(player);
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.EVIL_LAUGH, "No, I prefer to bash things close up.");
				stage = 3;
				break;
			}

			break;
		case 3:
			interpreter.sendDialogues(npc, FacialExpression.ANNOYED, "Humph, philistine.");
			stage = 4;
			break;
		case 4:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 550 };
	}

}
