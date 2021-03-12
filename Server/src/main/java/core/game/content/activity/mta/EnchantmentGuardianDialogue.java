package core.game.content.activity.mta;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;

/**
 * Handles the enchantment guardian dialogue.
 * @author Vexia
 */
public class EnchantmentGuardianDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@Code EnchantmentGuardianDialogue} {@Code
	 *  Object}
	 */
	public EnchantmentGuardianDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@Code EnchantmentGuardianDialogue} {@Code
	 *  Object}
	 * @param player the player.
	 */
	public EnchantmentGuardianDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new EnchantmentGuardianDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc("Greetings young one. How can I enlighten you?");
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
			npc("In this chamber you will see various piles of shapes. You", "can pick up these shapes and enchant them using the", "enchant jewelry spells. By enchanting these shapes,", "you'll be converting them into orbs which you can be");
			stage++;
			break;
		case 11:
			npc("placed in the hole in the centre. You will get", "Enchantment Pizazz Points for every ten shapes that", "you convert and the points you get depends on the", "level of enchantment spell you cast. You will get a");
			stage++;
			break;
		case 12:
			npc("bonus Pizazz Point for enchanting a certain shape at a", "certain time, which I will periodically shout out. You will", "also be rewarded with an item for every so many orbs", "you deposit into the hole.");
			stage = 0;
			break;
		case 20:
			npc("As well as the magic experience from casting your", "enchantment spells, you will get Enchantment Pizazz", "Points for converting so many shapes, plus a bonus", "point for converting shape of the correct type. You");
			stage++;
			break;
		case 21:
			npc("should also note that you will occasionally be rewarded", "with items when you put one of the enchanted orbs in", "the floor.");
			stage = 0;
			break;
		case 30:
			npc("Try to guess or keep track of the time between the", "change of the best shape to enchant. This means you", "can be ready to run to a different shape when you", "know it is about to change. Look out for the dragon");
			stage++;
			break;
		case 31:
			npc("gems, as these will get you more Pizazz Points!");
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
		return new int[] { 3100 };
	}

}
