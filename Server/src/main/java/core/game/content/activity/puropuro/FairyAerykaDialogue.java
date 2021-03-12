package core.game.content.activity.puropuro;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;

/**
 * Handles the fairy aeryka dialogue.
 * @author Vexia
 */
public final class FairyAerykaDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code FairyAerykaDialogue} {@code Object}.
	 */
	public FairyAerykaDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code FairyAerykaDialogue} {@code Object}.
	 * @param player the player.
	 */
	public FairyAerykaDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new FairyAerykaDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc("It's still here.");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			player("Pardon?");
			stage++;
			break;
		case 1:
			npc("It's still here. The crop circle is still here.");
			stage++;
			break;
		case 2:
			player("Oh yes, thanks Aery. It didn't go anywhere in the", "meantime, then?");
			stage++;
			break;
		case 3:
			npc("Nope. It just sat there.");
			stage++;
			break;
		case 4:
			player("Jolly good. I can come back and visit Puro-Puro", "whenever I want then. Brilliant!");
			stage++;
			break;
		case 5:
			options("What's in Puro-Puro?", "So what are these implings then?", "I've heard I may find dragon equipment in Puro-Puro?", "No, bye!");
			stage++;
			break;
		case 6:
			switch (buttonId) {
			case 1:
				player("What's in Puro-Puro?");
				stage = 10;
				break;
			case 2:
				player("So what are these implings then?");
				stage = 20;
				break;
			case 3:
				player("I've heard I may find dragon equipment in Puro-Puro?");
				stage = 30;
				break;
			case 4:
				player("No, bye!");
				stage = 40;
				break;
			}
			break;
		case 10:
			npc("Implings...and wheat.");
			stage = 99;
			break;
		case 20:
			npc("Well, no-one know for sure. The mischievous little", "creatures are probably related to imps. And they fly as", "well.");
			stage++;
			break;
		case 21:
			npc("Also, like imps, they love collecting things. I'm not sure", "why, though. They also seem to like being chased.");
			stage++;
			break;
		case 22:
			player("So how would I get hold of what they are carrying,", "then?");
			stage++;
			break;
		case 23:
			npc("Catch them, I suppose I don't know really. Why would", "you want to?");
			stage++;
			break;
		case 24:
			end();
			break;
		case 30:
			npc("Really? You humans like that stuff a lot, don't you? I", "don't like really old stuff myself.");
			stage++;
			break;
		case 31:
			end();
			break;
		case 40:
			npc("See you around!");
			stage = 99;
			break;
		case 99:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 6072 };
	}

}
