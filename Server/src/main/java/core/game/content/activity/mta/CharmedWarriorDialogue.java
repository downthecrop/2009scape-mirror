package core.game.content.activity.mta;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;

/**
 * Handles the charmed warrior dialogue.
 * @author Vexia
 */
public class CharmedWarriorDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@Code CharmedWarriorDialogue} {@Code
	 * Object}
	 */
	public CharmedWarriorDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@Code CharmedWarriorDialogue} {@Code
	 * Object}
	 * @param player the player.
	 */
	public CharmedWarriorDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new CharmedWarriorDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		switch (npc.getId()) {
		case 3105:
		case 3104:
			player("Is there anybody there?");
			break;
		default:
			player("Hello?");
			break;
		}
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (npc.getId()) {
		case 3107:
			switch (stage) {
			case 0:
				npc("Can't you see I'm busy?");
				stage++;
				break;
			case 1:
				player("Well, I can't really see YOU.");
				stage++;
				break;
			case 2:
				end();
				break;
			}
			break;
		case 3106:
			switch (stage) {
			case 0:
				npc("Hey! You haven't paid for your Magic Training Arena", "Membership money!");
				stage++;
				break;
			case 1:
				player("You're lying. I can see right through you!");
				stage++;
				break;
			case 2:
				npc("Oh, HA HA, very funny.");
				stage++;
				break;
			case 3:
				end();
				break;
			}
			break;
		case 3105:
			switch (stage) {
			case 0:
				npc("Wooo wooo! Be afraid for I'm a scary ghost. Wooo!");
				stage++;
				break;
			case 1:
				player("Er, whatever.");
				stage++;
				break;
			case 2:
				end();
				break;
			}
			break;
		case 3104:
			switch (stage) {
			case 0:
				npc("What do you think?");
				stage++;
				break;
			case 1:
				end();
				break;
			}
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 3106, 3107, 3104, 3105 };
	}
}
