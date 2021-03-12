package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Handles the quarter master dialogue.
 * @author Vexia
 */
@Initializable
public final class QuarterMasterDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@Code QuarterMasterDialogue} {@Code
	 * Object}
	 * @param player the player.
	 */
	public QuarterMasterDialogue(Player player) {
		super(player);
	}

	/**
	 * Constructs a new {@Code QuarterMasterDialogue} {@Code
	 * Object}
	 */
	public QuarterMasterDialogue() {
		/**
		 * empty.
		 */
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new QuarterMasterDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		npc("Hi, would you like to see my wares?");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			player("Yes, please.");
			stage++;
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
		return new int[] { 1208 };
	}

}
