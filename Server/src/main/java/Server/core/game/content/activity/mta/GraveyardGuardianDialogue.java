package core.game.content.activity.mta;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;

/**
 * Handles the graveyard guardian dialogue.
 * @author Vexia
 */
public class GraveyardGuardianDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@Code GraveyardGuardianDialogue} {@Code
	 * Object}
	 */
	public GraveyardGuardianDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@Code GraveyardGuardianDialogue} {@Code
	 * Object}
	 * @param player the player.
	 */
	public GraveyardGuardianDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new GraveyardGuardianDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		player("Hello.");
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
			npc("Have you noticed all the bones around the room? These", "are teleported here from all over Keldagrim to help", "clean up the landscape of countless bones left behind", "from combat. What better use for these bones than to");
			stage++;
			break;
		case 11:
			npc("convert them to nutritious fruit to be eaten by you", "mortals? You have to use your Bones to Bannas spell", "to convert the bones and then  place them in the holes", "on the walls to earn Graveyard Pizazz Points.");
			stage++;
			break;
		case 12:
			npc("Unluckily for you, your health will constantly decrease", "from getting hit by these dropping bones so you will", "probably want to eat some of the bananas yourself to", "increase your stay here.");
			stage = 0;
			break;
		case 20:
			npc("You will get experience from casting your bones to", "bananas spell and you will get Graveyard Pizazz Points", "when you put the bananas though the wall.", "Occasionally you will also be credited with a runestone");
			stage++;
			break;
		case 21:
			npc("to help you in your future spell casting.");
			stage = 0;
			break;
		case 30:
			npc("Different bones will provide you with different numbers", "of bananas, so try to find the best type. Collect enough", "points and you will be able to buy a 'Bones to Peaches'", "spell from my fellow guardian above the entrance hall.");
			stage++;
			break;
		case 31:
			npc("This spell can be used here just like the bones to", "bananas spell, except this spell will give you even more", "experience and the peaches will restore more health!");
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
		return new int[] { 3101 };
	}

}
