package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Handles the cape merchant dialogue.
 * @author Vexia
 */
@Initializable
public class CapeMerchantDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code CapeMerchantDialogue} {@code Object}
	 * @param player the player.
	 */
	public CapeMerchantDialogue(Player player) {
		super(player);
	}

	/**
	 * Constructs a new {@code CapeMerchantDialogue} {@code Object}
	 */
	public CapeMerchantDialogue() {
		/**
		 * empty.
		 */
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new CapeMerchantDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		npc("Hello there, are you interested in buying one of my", "special capes?");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			options("What's so special about your capes?", "Yes please!", "No thanks.");
			stage++;
			break;
		case 1:
			switch (buttonId) {
			case 1:
				player("What's so special about your capes?");
				stage = 10;
				break;
			case 2:
				player("Yes please!");
				stage = 20;
				break;
			case 3:
				player("No thanks.");
				stage = 30;
				break;
			}
			break;
		case 10:
			npc("Ahh well they make it less likely that you'll accidently", "attack anyone wearing the same cape as you and easier", "to attack everyone else. They also make it easier to", "distinguish people who're wearing the same cape as you");
			stage++;
			break;
		case 11:
			npc("from everyone else. They're very useful when out in", "the wilderness with friends or anyone else you don't", "want to harm.");
			stage++;
			break;
		case 12:
			end();
			break;
		case 20:
			end();
			npc.openShop(player);
			break;
		case 30:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 1778, 1779, 1780, 1781, 1782, 1783, 1784, 1785, 1786, 1787 };
	}

}
