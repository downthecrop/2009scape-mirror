package plugin.dialogue;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.node.item.Item;

/**
 * Handles the chemist dialogue.
 * @author Vexia
 */
@InitializablePlugin
public final class ChemistDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code ChemistDialogue} {@code Object}.
	 */
	public ChemistDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code ChemistDialogue} {@code Object}.
	 * @param player the player.
	 */
	public ChemistDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new ChemistDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		interpreter.sendOptions("Talk about lamps?", "Yes.", "No.", "No, I'm more interested in impling jars.");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			switch (buttonId) {
			case 1:
				player("Hi, I need fuel for a lamp.");
				stage = 10;
				break;
			case 2:
				player("Hello.");
				stage = 20;
				break;
			case 3:
				player("I have a slightly odd question.");
				stage = 30;
				break;
			}
			break;
		case 10:
			npc("Hello there, the fuel you need is lamp oil, do you need", "help making it?");
			stage++;
			break;
		case 11:
			player("Yes, please.");
			stage++;
			break;
		case 12:
			npc("It's really quite simple. You use the small still in here.", "It's all set up, so there's no fiddling around with dials...");
			stage++;
			break;
		case 13:
			npc("Just put ordinary swamp tar in, and then use a lantern", "or lamp to get the oil out.");
			stage++;
			break;
		case 14:
			player("Thanks.");
			stage++;
			break;
		case 15:
			end();
			break;
		case 20:
			npc("Oh.. hello, how's it going?");
			stage++;
			break;
		case 21:
			player("Good thanks.");
			stage++;
			break;
		case 22:
			npc("Good to hear, sorry but I have a few things to do", "now.");
			stage++;
			break;
		case 23:
			end();
			break;
		case 30:
			npc("Jolly good, the odder the better. I like oddities.");
			stage++;
			break;
		case 31:
			player("Do you know how I might distil a mix of anchovy oil", "and flowers so that it forms a layer on the inside of a", "butterfly jar?");
			stage++;
			break;
		case 32:
			npc("That is an odd question. I commend you for it. Why", "would you want to do that?");
			stage++;
			break;
		case 33:
			player("Apparently, if I can make a jar like this it will be useful", "for storing implings in.");
			stage++;
			break;
		case 34:
			npc("My lamp oil still may be able to do what you want. Use the", "oil and flower mix on the still.");
			stage++;
			break;
		case 35:
			npc("Once that's done. Use one of those butterfly jars to", "collect the distillate.");
			stage++;
			break;
		case 36:
			player("Thanks!");
			stage++;
			break;
		case 37:
			options("So how do you make anchovy oil?", "Do you have a sieve I can use?", "I'd better go and get the repellent.");
			stage++;
			break;
		case 38:
			switch (buttonId) {
			case 1:
				player("So, how do you make anchovy oil?");
				stage = 100;
				break;
			case 2:
				player("Do you have a sieve I can use?");
				stage = 120;
				break;
			case 3:

				break;
			}
			break;
		case 100:
			npc("Anchovies are pretty oily fish. I'd have thought you", "could just grind them up and sieve out the bits. You'd", "probably want to remove any water first - Cooking", "should do that pretty well.");
			stage++;
			break;
		case 101:
			end();
			break;
		case 120:
			if (player.hasItem(new Item(6097))) {
				npc("Errm, yes. But you already have one. Two sieves is a ", "bit exessive, don't you think?");
				stage = 125;
				break;
			}
			npc("Errm, yes. Here have this one. It's only been used for", "sieving dead rats out of sewer water.");
			stage++;
			break;
		case 121:
			player("Err, why? Actually, on second thoughts I don't want to", "know.");
			;
			stage++;
			break;
		case 122:
			npc("Well, it would be ideally suited to your task.");
			stage++;
			break;
		case 123:
			player("Fair enough.");
			stage++;
			break;
		case 124:
			player.getInventory().add(new Item(6097), player);
			end();
			break;
		case 125:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 367 };
	}

}
