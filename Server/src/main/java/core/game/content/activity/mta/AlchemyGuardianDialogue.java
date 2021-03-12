package core.game.content.activity.mta;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;

/**
 * Handles the alchemy guardian dialogue.
 * @author Vexia
 */
public class AlchemyGuardianDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code AlchemyGuardianDialogue} {@code Object}
	 */
	public AlchemyGuardianDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code AlchemyGuardianDialogue} {@code Object}
	 * @param player the player.
	 */
	public AlchemyGuardianDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new AlchemyGuardianDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		player("Hi.");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			options("What do I have to do in this room?", "What are the rewards?", "Got any tips that may help me?", "Thanks, bye!");
			stage++;
			break;
		case 1:
			switch (buttonId) {
			case 1:
				player("What do I have to do in this room?");
				stage = 10;
				break;
			case 2:
				player("What are the rewards?");
				stage = 20;
				break;
			case 3:
				player("Got any tips that may help me?");
				stage = 30;
				break;
			case 4:
				player("Thanks, bye!");
				stage = 40;
				break;
			}
			break;
		case 10:
			npc("In this room you will see various cupboards. It is your", "task to search the cupboards to find items to turn into", "gold using your low or high alchemy spells. You must", "deposit the money in the receptacle at the end of the");
			stage++;
			break;
		case 11:
			npc("hall in order to receive your Alchemist Pizazz Points,", "otherwise the money will be taken from you as you", "leave through the portal. This money is used for the", "upkeep of the training arena as well as magic shops all");
			stage++;
			break;
		case 12:
			npc("around Keldagrim. Keep an eye on the cost of each", "items as these will change from time-to-time, as will the", "location of the items. Occasionally one of the items will", "be indicated as costing no runestones to convert to");
			stage++;
			break;
		case 13:
			npc("money.");
			stage = 0;
			break;
		case 20:
			npc("You will get experience from casting the alchemist", "spells, as well as 1 Alchemist Pizazz Point for every 100", "coins you deposit, and 10% of the coins you deposit will", "be given to you as you leave. Keep in mind that you");
			stage++;
			break;
		case 21:
			npc("will not be able to take more than 1000 coins back out", "with you.");
			stage = 0;
			break;
		case 30:
			npc("You must remember to keep ane eye on the various", "costs of the itmes. If you watch the movements of the", "other players, you might be able to guess which are the", "best places to visit. You will get 1 Pizazz Point for");
			stage++;
			break;
		case 31:
			npc("every 100 coins, so if you have 190 coins, why not get", "an extra 10?");
			stage++;
			break;
		case 32:
			player("I see.");
			stage++;
			break;
		case 33:
			npc("Oh, and a word of warning: should you decide to leave", "this room by a method other than the exit portals, you", "will be teleported to the entrance and have any items", "that you picked up in the room removed.");
			stage = 0;
			break;
		case 34:
			end();
			break;
		case 40:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 3099 };
	}

}
