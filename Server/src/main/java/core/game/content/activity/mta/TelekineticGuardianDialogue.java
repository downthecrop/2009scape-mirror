package core.game.content.activity.mta;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;

/**
 * Handles the telekinetic guardian dialogue.
 * @author Vexia
 */
public class TelekineticGuardianDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code TelekineticGuardianDialogue} {@code Object}
	 */
	public TelekineticGuardianDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code TelekineticGuardianDialogue} {@code Object}
	 * @param player the player.
	 */
	public TelekineticGuardianDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new TelekineticGuardianDialogue(player);
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
			npc("In this room you will see a maze within which one of", "my fellow Guardians has been turned to stone for the", "purpose of this exercise. You must move the statue", "using your telekinetic grab spell to the exit square at");
			stage++;
			break;
		case 11:
			npc("the edge of the maze to bring the Guardian back to life.", "Simply stand on the side that you wish for the statue to", "travel towards and cast the spell on the statue. Once", "you have solved the maze, the statue will change back");
			stage++;
			break;
		case 12:
			npc("into the Guardian and he will award you with", "Telekinetic Pizazz Points and teleport you to the next", "maze. You can switch to a better view of the maze by", "selecting the 'Observe' option on the statue and return");
			stage = 13;
			break;
		case 13:
			npc("your view to normal by selecting the same option again.", "There is also a 'Reset' option on the statue just in case", "things aren't going too well.");
			stage = 0;
			break;
		case 20:
			npc("As well as the experience in casting magic, you will get", "Telekinetic Pizazz Points for each maze successfully", "solved and bonus points for completing five mazes in a", "row without returning to the entrance.");
			stage++;
			break;
		case 21:
			npc("should also note that you will occasionally be rewarded", "with items when you put one of the enchanted orbs in", "the floor.");
			stage = 0;
			break;
		case 30:
			npc("Have a good look at the maze before you try to solve it", "because this can save you time and runes required to", "navigate the maze. Although you will still be getting", "magic experience for moving the statue incorrectly, you");
			stage++;
			break;
		case 31:
			npc("won't be progressing towards collecting Telekinetic", "Pizazz Points. Lastly, all the mazes can be solved in ten", "moves or less.");
			stage++;
			break;
		case 32:
			player("I see.");
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
		return new int[] { 3098 };
	}

}
