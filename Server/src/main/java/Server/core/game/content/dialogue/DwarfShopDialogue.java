package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the dialouge plugin used for the dward shop.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class DwarfShopDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code DwarfShopDialogue} {@code Object}.
	 */
	public DwarfShopDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code DwarfShopDialogue} {@code Object}.
	 * @param player the player.
	 */
	public DwarfShopDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new DwarfShopDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Can I help you at all?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendOptions("Select an Option", "Yes please, what are you selling?", "No thanks.");
			stage = 1;
			break;
		case 1:
			switch (buttonId) {
			case 1:
				end();
				npc.openShop(player);
				break;
			case 2:
				end();
				break;
			}
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 582 };
	}
}
