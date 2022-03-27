package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for barfy bill.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class BarfyBill extends DialoguePlugin {

	/**
	 * Constructs a new {@code BarfyBill} {@code Object}.
	 */
	public BarfyBill() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code BarfyBill} {@code Object}.
	 * @param player the player.
	 */
	public BarfyBill(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new BarfyBill(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		player("Hello there.");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			npc("Oh! Hello there.");
			stage = 1;
			break;
		case 1:
			interpreter.sendOptions("Select an Option", "Who are you?", "Can you teach me about Canoeing?");
			stage = 2;
			break;
		case 2:
			switch (buttonId) {
			case 1:
				player("Who are you?");
				stage = 1000;
				break;
			case 2:
				npc("It's really quite simple. Just walk down to that tree on", "the water bank and chop it down.");
				stage = 24;
				break;
			}
			break;
		case 24:
			npc("Then take your axe to it and shape it how you like!");
			stage = 26;
			break;
		case 26:
			npc("You look like you know your way around a tree,", "you can make a canoe.");
			stage = 27;
			break;
		case 27:
			end();
			break;
		case 1000:
			npc("My name is Ex Sea Captain Barfy Bill.");
			stage = 1001;
			break;
		case 1001:
			player("Ex sea captain?");
			stage = 1002;
			break;
		case 1002:
			npc("Yeah, I bought a lovely ship and was planning to make", "a fortune running her as a merchant vessel.");
			stage = 1003;
			break;
		case 1003:
			player("Why are you not still sailing?");
			stage = 1004;
			break;
		case 1004:
			npc("Chronic sea sickness. My first, and only, voyage was", "spent dry heaving over the rails.");
			stage = 1005;
			break;
		case 1005:
			npc("If I had known about the sea sickness I could have", "saved myself a lot of money.");
			stage = 1006;
			break;
		case 1006:
			player("What are you up to now then?");
			stage = 1007;
			break;
		case 1007:
			npc("Well my ship had a little fire related problem.", "Fortunately it was well insured.");
			stage = 1008;
			break;
		case 1008:
			npc("Anyways, I don't have to work anymore so I've taken to", "canoeing on the river.");
			stage = 1009;
			break;
		case 1009:
			npc("I don't get river sick!");
			stage = 69;
			break;
		case 69:
			npc("Would you like to know how to make a canoe?");
			stage = 6000;
			break;
		case 6000:
			interpreter.sendOptions("Select an Option", "Yes", "No");
			stage = 504;
			break;
		case 504:
			switch (buttonId) {
			case 1:
				player("Could you teach me about canoes?");
				stage = 560;
				break;
			case 2:
				end();
				break;
			}
			break;
		case 560:
			npc("It's really quite simple. Just walk down to that tree on", "the water bank and chop it down.");
			stage = 24;
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 3331 };
	}

}
