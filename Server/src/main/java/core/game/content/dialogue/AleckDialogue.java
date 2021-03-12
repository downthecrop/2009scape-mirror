package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the plugin dialogue used for the aleck npc.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class AleckDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code AleckDialogue} {@code Object}.
	 */
	public AleckDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code AleckDialogue} {@code Object}.
	 * @param player the player.
	 */
	public AleckDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new AleckDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		player("Hello.");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			npc("Hello, hello, and a most warm welcome to my Hunter", "Emporium. We have everything the discerning Hunter", "could need.");
			stage = 1;
			break;
		case 1:
			npc("Would you like me to show you our range of", "equipment? Or was there something specific you were", "after?");
			stage = 2;
			break;
		case 2:
			interpreter.sendOptions("Select an Option", "Ok, let's see what you've got.", "I'm not interested, thanks.", "Who's that guy over there?");
			stage = 3;
			break;
		case 3:
			switch (buttonId) {
			case 1:
				player("Ok, let's see what you've got!");
				stage = 10;
				break;
			case 2:
				player("I'm not interested, thanks.");
				stage = 20;
				break;
			case 3:
				player("Who's that guy over there?");
				stage = 30;
				break;

			}
			break;
		case 10:
			end();
			npc.openShop(player);
			break;
		case 20:
			end();
			break;
		case 30:
			npc("Him? I think he might be crazy. Either that or he's", "seeking attention.");
			stage = 31;
			break;
		case 31:
			npc("He keeps trying to sell me these barmy looking weapons", "he's invented. I can't see them working, personally.");
			stage = 32;
			break;
		case 32:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 5110 };
	}
}
