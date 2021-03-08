package core.game.content.dialogue;

import core.game.ge.GEGuidePrice;
import core.game.ge.GEGuidePrice.GuideType;
import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the bob barter npc dialogue plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class BobBarterDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code BobBarterDialogue} {@code Object}.
	 */
	public BobBarterDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code BobBarterDialogue} {@code Object}.
	 * @param player the player.
	 */
	public BobBarterDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new BobBarterDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		player("Hi.");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			npc("Hello, chum, fancy buyin' some designer", "jewellery? They've come all the way from", "Ardougne! most pukka!");
			stage = 1;
			break;
		case 1:
			player("Erm, no. I'm all set, thanks.");
			stage = 2;
			break;
		case 2:
			npc("Okay, chum. So what can I do for you? I can", "tell you the very latest herb prices.");
			stage = 3;
			break;
		case 3:
			interpreter.sendOptions("Select an Option", "Who are you?", "Can you help me out with the prices for herbs?", "Sorry, I've got to split.");
			stage = 4;
			break;
		case 4:
			switch (buttonId) {
			case 1:
				player("Who are you?");
				stage = 10;
				break;
			case 2:
				player("Can you help me out with the prices for herbs?");
				stage = 20;
				break;
			case 3:
				player("Sorry, I've got to split.");
				stage = 30;
				break;
			}
			break;
		case 30:
			end();
			break;
		case 10:
			npc("Why, I'm Bob! Your friendly seller of goods!");
			stage = 11;
			break;
		case 11:
			player("So what do you have to sell?");
			stage = 12;
			break;
		case 12:
			npc("Oh, not much at the moment. Cuz, ya know", "Business being so well and cushie.");
			stage = 13;
			break;
		case 13:
			player("You don't really look like you're being so", "successfull.");
			stage = 14;
			break;
		case 14:
			npc("You plonka! It's all a show, innit! If I let people", "knows I'm in good business they'll want a", "share of the moolah!");
			stage = 15;
			break;
		case 15:
			end();
			break;
		case 20:
			end();
			GEGuidePrice.open(player, GuideType.HERBS);
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 6524 };
	}
}
