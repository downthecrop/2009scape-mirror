package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Handles the RimmingtonShopKeeperDialogue dialogue.
 * @author 'Vexia
 */
@Initializable
public class RimmingtonShopKeeperDialogue extends DialoguePlugin {

	public RimmingtonShopKeeperDialogue() {

	}

	public RimmingtonShopKeeperDialogue(Player player) {
		super(player);
	}

	@Override
	public int[] getIds() {
		return new int[] { 531, 530 };
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendOptions("Select an Option", "Yes, please. What are you selling?", "How should I use your shop?", "No, thanks.");
			stage = 1;
			break;
		case 1:

			switch (buttonId) {
			case 1:
				end();
				npc.openShop(player);
				break;
			case 2:
				interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "I'm glas you ask! You can buy as many of the items", "stocked as you wish. You can also sell most items to the", "shop.");
				stage = 10;
				break;
			case 3:
				end();
				break;
			}

			break;
		case 10:
			end();
			break;
		}

		return true;
	}

	@Override
	public DialoguePlugin newInstance(Player player) {

		return new RimmingtonShopKeeperDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Can I help you at all?");
		stage = 0;
		return true;
	}
}
