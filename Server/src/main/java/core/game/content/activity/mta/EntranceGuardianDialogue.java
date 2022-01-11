package core.game.content.activity.mta;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;
import rs09.game.world.GameWorld;

/**
 * Handles the entrance guardian dialogue.
 * @author Vexia
 */
public class EntranceGuardianDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@Code EntranceGuardianDialogue} {@Code
	 * Object}
	 */
	public EntranceGuardianDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@Code EntranceGuardianDialogue} {@Code
	 * Object}
	 * @param player the player.
	 */
	public EntranceGuardianDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new EntranceGuardianDialogue(player);
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
			npc("Greetings. What wisdom do you seek?");
			stage++;
			if (player.getSavedData().getActivityData().isStartedMta()) {
				stage = 30;
			}
			break;
		case 1:
			options("I'm new to this place. Where am I?", "None, I don't really care.");
			stage++;
			break;
		case 2:
			if (buttonId == 1) {
				player("I'm new to this place. Where am I?");
				stage++;
				break;
			}
			end();
			break;
		case 3:
			npc("Well young one, you have entered the Magic Training", "Arena. It was built at the start of the Fifth Age, when", "runestones were first discovered. It was made because", "of the many pointless accidents caused by inexperienced");
			stage++;
			break;
		case 4:
			npc("mages.");
			stage++;
			break;
		case 5:
			player("Who created it?");
			stage++;
			break;
		case 6:
			npc("Good question. It was originally made by the ancestors", "of the wizards in the Wizards Tower. However, it was", "destroyed by melee and ranged warriors who took", "offence at the use of this new 'Magic Art'. Recently");
			stage++;
			break;
		case 7:
			npc("the current denizens of the Wizards Tower have", "resurrected the arena including various Guardians you", "will see as you look around. We are here to help and to", "ensure things run smoothly.");
			stage++;
			break;
		case 8:
			player("Interesting. So what can I do here?");
			stage++;
			break;
		case 9:
			npc("You may train up your skills in the magic arts by", "travelling through one of the portals at the back of this", "entrance hall. By training up in one of these areas you", "will be awarded special Pizazz Points unique to each.");
			stage++;
			break;
		case 10:
			npc("room. With these points you may claim a variety of", "items from my fellow guardian up the stairs.");
			stage++;
			break;
		case 11:
			player("How do you record the points I have earned?");
			stage++;
			break;
		case 12:
			if (player.getSavedData().getActivityData().isStartedMta()) {
				npc("With the Pizazz Progress Hat I gave you, of course.");
				stage = 40;
				break;
			}
			npc("You really are full of questions! You will need a special", "Pizzaz Progress Hat! I can give you one if you so", "wish to train here.");
			stage++;
			break;
		case 13:
			player("Yes Please!");
			stage++;
			break;
		case 14:
			player.getSavedData().getActivityData().setStartedMta(true);
			player.getInventory().add(MageTrainingArenaPlugin.PROGRESS_HAT, player);
			npc("Here you go. Talk to the hat to find out your current", "Pizzaz Points totals.");
			stage++;
			break;
		case 15:
			player("Talk to it?");
			stage++;
			break;
		case 16:
			npc("Well of course, it's a magic Pizazz Progress Hat! Mind", "your manners though, hats have feelings too!");
			stage++;
			break;
		case 17:
			player("Er... if you insist.");
			stage++;
			break;
		case 18:
			npc("Oh, and a word of warning: should you decide to leave", "the rooms by any method other than the exit portals,", "you will be teleported to the entrance and have any", "items that you picked up in the room removed.");
			stage++;
			break;
		case 19:
			player("Okay. Thanks!");
			stage++;
			break;
		case 20:
			end();
			break;
		case 30:
			options("Can you tell me about this place again?", "Can you explain the different portals?", "About the progress hat...", "Thanks, bye!");
			stage++;
			break;
		case 31:
			switch (buttonId) {
			case 1:
				player("Can you tell me about this place again?");
				stage = 3;
				break;
			case 2:
				player("Can you explain the different portals?");
				stage = 50;
				break;
			case 3:
				player("About the progress hat...");
				stage = 100;
				break;
			case 4:
				end();
				break;
			}
			break;
		case 100:
			if (!player.hasItem(MageTrainingArenaPlugin.PROGRESS_HAT)) {
				npc("You want another one don't you.");
				stage = 110;
				break;
			}
			npc("Which you have stored somewhere I'm sure.");
			stage++;
			break;
		case 101:
			player("Yes. What's it for?");
			stage++;
			break;
		case 102:
			npc("Collect Pizazz Points from the various areas and the", "hat will remember your totals. Talk to the hat at any", "time to find out what points you have. Go upstairs and", "talk to the Rewards Guardian to claim items for the");
			stage++;
			break;
		case 103:
			npc("points.");
			stage = 30;
			break;
		case 110:
			player("Sorry, can I?");
			stage++;
			break;
		case 111:
			player.getInventory().add(MageTrainingArenaPlugin.PROGRESS_HAT, player);
			npc("Here you go. Talk to the hat to find out your current", "Pizazz Point totals.");
			stage = 30;
			break;
		case 40:
			player("OK.");
			stage++;
			break;
		case 41:
			npc("Oh, and a word of warning: should you decide to log", "out whilst in any of the rooms in the arena, you will be", "teleported to the entrance and have any items that you", "picked up in the room removed.");
			stage = 30;
			break;
		case 50:
			npc("They lead to four areas to train your magic: The", "Telekinetic Theatre, The Alchemists' Playground, The", "Enchanting Chamber, and The Creature Graveyard.");
			stage++;
			break;
		case 51:
			options("What's the Telekinetic Theatre?", "What's the Alchemists' Playground?", "What's the Enchanting CHamber?", "What's the Creature Gaveyard?", "Thanks, bye!");
			stage++;
			break;
		case 52:
			switch (buttonId) {
			case 1:
				player("What's the Telekinetic Theatre?");
				stage = 60;
				break;
			case 2:
				player("What's the Alchemists' Playground?");
				stage = 70;
				break;
			case 3:
				player("What's the Enchanting Chamber?");
				stage = 80;
				break;
			case 4:
				player("What's the Creature Gaveyard?");
				stage = 90;
				break;
			case 5:
				end();
				break;
			}
			break;
		case 60:
			npc("In there you can earn Telekinetic Pizazz Points for", "trde upstairs.");
			stage++;
			break;
		case 61:
			player("What will I be doing in there?");
			stage++;
			break;
		case 62:
			npc("That depends on how much of a time-waster you are!", "You are required to use the Telekinetic Grab spell in", "order to move a statue throuhh a maze. Casting the", "spell will move the statue towards you until it reaches a");
			stage++;
			break;
		case 63:
			npc("wall. So by standing on the different sides of the maze", "you can move the statue Nort, East, South or West.", "You will be rewarded Pizazz Points for each maze", "successfully solved.");
			stage = 51;
			break;
		case 70:
			npc("In the playground you can earn Alchemist Pizazz", "Points for trading with the rewards guardian upstairs.");
			stage++;
			break;
		case 71:
			player("What's in there?");
			stage++;
			break;
		case 72:
			npc("You'll find eight cupboards containing items you can", "turn to gold using the low and high alchemy spells. The", "money you earn will be taken from you upon leaving", "the playground to pay for the upkeep of this training");
			stage++;
			break;
		case 73:
			npc("arena and to help fund magic shops around " + GameWorld.getSettings().getName() + ".", "You will be rewarded with 1 Pizazz Point for every", "100 coins deposited and a percentage of the money you", "create. Keep in mind that you will not be able to take");
			stage++;
			break;
		case 74:
			npc("more than 1000 coins back out with you.");
			stage++;
			break;
		case 75:
			player("Sounds simple.");
			stage = 51;
			break;
		case 80:
			npc("In here you will be able to earn Enchantment Pizazz", "Points for trade upstairs.");
			stage++;
			break;
		case 81:
			player("What will I have to do?");
			stage++;
			break;
		case 82:
			npc("You will find yourself amongst various piles of shapes.", "You can pick up these shapes and use on of your", "enchanting spells upon it to morph it into an orb. You'll", "also see a hole in the centre of the room; put the orbs");
			stage++;
			break;
		case 83:
			npc("in here and for every 20 we will credit you with a gift,", "You will get Pizazz Points for every 10 shapes that", "you convert and the amount of points depends on the", "spell you use.");
			stage = 51;
			break;
		case 90:
			npc("In here you will be able to earn Graveyard Pizazz", "Points for trade upstairs.");
			stage++;
			break;
		case 91:
			player("But what do I have to do in there?");
			stage++;
			break;
		case 92:
			npc("Patience young one. A great many creatures die in", "this world and much of their remainds can cause clutter.", "We have taken it upon ourselves to teleport many", "bones from these creatures into this graveyard for a");
			stage++;
			break;
		case 93:
			npc("use. It's up to you to practice your bones to bannanas", "spell in order to put these bones into immediate use -", "nutritious food for monsters! Just convert the bones", "and put them in the holes in the walls. There is a");
			stage++;
			break;
		case 94:
			npc("drawback to this room however, the bones often fall on", "people so you may want to eat some of those bannas", "to stay alive. When people die we confiscate Pizazz", "Points for the effort of teleporting incompetent mages.");
			stage = 51;
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 3097 };
	}

}
