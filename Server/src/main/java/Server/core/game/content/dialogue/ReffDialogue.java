package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * The ref dialogue.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class ReffDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code ReffDialogue} {@code Object}.
	 */
	public ReffDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code ReffDialogue} {@code Object}.
	 * @param player the player.
	 */
	public ReffDialogue(final Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new ReffDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		npc("Greetings " + (player.getAppearance().isMale() ? "Sir" : "Madam") + ".");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			options("Tell me about the Shot Put area.", "May I claim my tokens please?", "Do you have any tips for me?", "Bye!");
			stage++;
			break;
		case 1:
			switch (buttonId) {
			case 1:
				player("Tell me about the Shot Put area.");
				stage = 10;
				break;
			case 2:
				end();
				player.getDialogueInterpreter().open("wg:claim-tokens", npc.getId());
				break;
			case 3:
				player("Do you have any tips for me?");
				stage = 30;
				break;
			case 4:
				player("Bye!");
				stage = 40;
				break;
			}
			break;
		case 10:
			npc("Of course " + (player.getAppearance().isMale() ? "Sir" : "Madam") + ". There are two different weights of", "shot...");
			stage++;
			break;
		case 11:
			player("Shot?");
			stage++;
			break;
		case 12:
			npc("Yes Madam. Shot. The iron spheres that are propelled", "by chemical energy stored in your body.");
			stage++;
			break;
		case 13:
			player("The... what?");
			stage++;
			break;
		case 14:
			npc("Your strength " + (player.getAppearance().isMale() ? "Sir" : "Madam") + ". The stronger you are, the", "further you can throw the shot, but there are other", "factors of course, like your technique.");
			stage++;
			break;
		case 15:
			player("What's that then?");
			stage++;
			break;
		case 16:
			npc("The style of the shot " + (player.getAppearance().isMale() ? "Sir" : "Madam") + ".");
			stage++;
			break;
		case 17:
			player("Iron has style??");
			stage++;
			break;
		case 18:
			interpreter.sendDialogue("The Referee sighs, rolls his eyes and continues....");
			stage++;
			break;
		case 19:
			npc((player.getAppearance().isMale() ? "Sir" : "Madam") + ", the style in which you throw the shot, not the", "style of iron.");
			stage = -1;
			break;
		case -1:
			player("OH! You mean the spinny round thing or the chuck it", "straight?");
			stage = -2;
			break;
		case -2:
			npc("Crudely put " + (player.getAppearance().isMale() ? "Sir" : "Madam") + ", but yes. Some are more difficult", "than others. Experiment and see which you prefer.");
			stage = -3;
			break;
		case -3:
			player("Thanks for the help!");
			stage = -4;
			break;
		case -4:
			npc("You are welcome " + (player.getAppearance().isMale() ? "Sir" : "Madam") + ".");
			stage = -5;
			break;
		case -5:
			end();
			break;
		case 30:
			npc("Tips " + (player.getAppearance().isMale() ? "Sir" : "Madam") + "?");
			stage++;
			break;
		case 31:
			player("Yes, like how can I do better than everyone else.");
			stage++;
			break;
		case 32:
			npc((player.getAppearance().isMale() ? "Sir" : "Madam") + " may find that a fine powder applied to the", "hands may give one an advantage when putting the", "shot.");
			stage++;
			break;
		case 33:
			player("You mean if I grind something up and put dust on my", "hands I'll chuck the ball further?");
			stage++;
			break;
		case 34:
			npc("Yes " + (player.getAppearance().isMale() ? "Sir" : "Madam") + ".");
			stage++;
			break;
		case 35:
			player("Thanks!");
			stage = -4;
			break;
		case 40:
			npc("Good luck " + (player.getAppearance().isMale() ? "Sir" : "Madam") + ".");
			stage++;
			break;
		case 41:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 4299, 4300 };
	}

}
