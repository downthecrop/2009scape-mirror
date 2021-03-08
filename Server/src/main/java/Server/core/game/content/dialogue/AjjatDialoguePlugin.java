package core.game.content.dialogue;

import core.game.content.global.Skillcape;
import core.plugin.Initializable;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for the ajjat npc.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class AjjatDialoguePlugin extends DialoguePlugin {

	/**
	 * Constructs a new {@code AjjatDialoguePlugin} {@code Object}.
	 */
	public AjjatDialoguePlugin() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code AjjatDialoguePlugin} {@code Object}.
	 * @param player the player.
	 */
	public AjjatDialoguePlugin(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new AjjatDialoguePlugin(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		npc("Greetings, fellow warrior. I am Ajjat, former Black Knight", "and now traning officer here in the Warriors' Guild.");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendOptions("Select an Option", "Can you tell me about skillcapes, please?", "Black knight? Why are you here?", "What's the dummy room all about?", "May I claim my tokens please?", "Bye!");
			stage = 1;
			break;
		case 1:
			if (buttonId == 1) {
				player("Can you tell me about skillcapes, please?");
				stage = 2;
			} else if (buttonId == 2) {
				player("Black knight? Why are you here?");
				stage = 4;
			} else if (buttonId == 3) {
				player("What's the dummy room all about?");
				stage = 6;
			} else if (buttonId == 4) {
				end();
				player.getDialogueInterpreter().open("wg:claim-tokens", npc.getId());
			} else if (buttonId == 5) {
				player("Bye!");
				stage = 12;
			}
			break;
		case 2:
			npc("Skillcapes, also knows as Capes of Accomplishment, are", "reserved for the elite of the elite. Only a person who has", "truly mastered a skill can buy one, even then a", "Skillcape can only be bought from one who is recognised as");
			stage = 3;
			break;
		case 3:
			npc("the highest skilled in the land at any particular skill. I", "have the privilege of being the person that controls", "acces to the Skillcape of Attack. Is there anything else I", "can help you with?");
			stage = 600;
			break;
		case 4:
			npc("Indeed I was however, their... methods did not match", "with my ideals, so I left. Harrallak, recognsing my talent as", "a warrior, took me in and offered me a job here.");
			stage = 5;
			break;
		case 5:
			player("Hmm...well, if Harrallak trusts you, I guess I can.");
			stage = 0;
			break;
		case 6:
			npc("Ahh yes, the dummies. Another ingenious invention of the", "noble dwarf, Gamfred. They're mechanical, you see, and", "pop up out of the floor. You have to hit them with the", "correct attack mode before they dissapear again.");
			stage = 7;
			break;
		case 7:
			player("Do, how do I tell wich one is wich?");
			stage = 8;
			break;
		case 8:
			npc("here are two different ways. One indication is their", "colour, the other is the pose and weapons they are", "holding, for instance, the one holding daggers you will", "to hit with a piercing attack.");
			stage = 9;
			break;
		case 9:
			npc("In the room, you will find a poster on the wall to", " help you recognize each different dummy.");
			stage = 10;
			break;
		case 10:
			player("That sounds ingenious!");
			stage = 11;
			break;
		case 11:
			npc("Indeed, you may find that you need several weapons to", "be successfull all of the time, but keep trying", "The weapons Shop upstairs may help you there.");
			stage = 0;
			break;
		case 12:
			npc("Farewell, warrior. Stay away from the dark side.");
			stage = 13;
			break;
		case 13:
			end();
			break;
		case 600:
			if (player.getSkills().getStaticLevel(Skills.ATTACK) == 99) {
				player("I'd like to buy a skillcape of Attack.");
				stage = 601;
			} else {
				end();
			}
			break;
		case 601:
			npc("Okay, it will cost you 99000 coins", "is that ok?");
			stage = 602;
			break;
		case 602:
			options("Yes.", "No.");
			stage = 603;
			break;
		case 603:
			switch (buttonId) {
			case 1:
				player("Yes.");
				stage = 604;
				break;
			case 2:
				end();
				break;
			}
			break;
		case 604:
			if (Skillcape.purchase(player, Skills.ATTACK)) {
				npc("There you go! Enjoy.");
			}
			stage = 605;
			break;
		case 605:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 4288 };
	}
}
