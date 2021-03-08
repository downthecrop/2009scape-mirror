package core.game.content.dialogue;

import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Handles the key master dialogue.
 * @author Empathy
 *
 */
@Initializable
public class KeyMasterDialoguePlugin extends DialoguePlugin {

	/**
	 * Constructs a new @{Code KeyMasterDialoguePlugin} object.
	 */
	public KeyMasterDialoguePlugin() {
		/**
		 * empty
		 */
	}
	
	/**
	 * Constructs a new @{Code KeyMasterDialoguePlugin} object.
	 * @param player
	 */
	public KeyMasterDialoguePlugin(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new KeyMasterDialoguePlugin(player);
	}

	@Override
	public boolean open(Object... args) {
		player("Hello.");
		stage = 1;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch(stage) {
		case 1:
			npc("Who goes there?");
			stage++;
			break;
		case 2:
			npc("This is no place for a human. You need to leave.");
			stage++;
			break;
		case 3:
			player("Why?");
			stage++;
			break;
		case 4:
			npc("The voices! The voices in my head!");
			stage++;
			break;
		case 5:
			player("You're starting to scare me man...");
			stage++;
			break;
		case 6:
			npc("I am no man! They changed me and cursed me to", "remain here...");
			stage++;
			break;
		case 7:
			options("What do you mean they changed you?", "What was the curse?", "Goodbye");
			stage++;
			break;
		case 8:
			switch(buttonId) {
			case 1:
				npc("I was once a free man, powerful and wealthy. I owned", "several apothecaries across Zeah and sold the tastiest", "potions in the land.");
				stage = 20;
				break;
			case 2:
				npc("I have been charged to stay here to prevent the", "Monstrosity from escaping. Those gates and the", "winches that operate them are the only thing that stops", "it from breaking free.");
				stage = 40;
				break;
			case 3:
				player("Goodbye.");
				stage = 50;
				break;
			}
			break;
		case 20:
			player("What happened?");
			stage++;
			break;
		case 21:
			npc("One of my greatest inventions, it was going so well. I", "spent days finding the right herbs, I travelled across all", "of Zeah to find the most exotic weeds. Once I had", "gathered them all, I put them in a potion and mixed in");
			stage++;
			break;
		case 22:
			npc("the final ingredient.");
			stage++;
			break;
		case 23:
			player("Zeah? Interesting...");
			stage++;
			break;
		case 24:
			npc("Yes, yes... The potion tasted delicious but it was missing", "a tiny something so I added Magic roots to the potion.", "It started to pulsate and glow! I took a sip and I felt", "like a million gold! A few seconds later my eye sight");
			stage++;
			break;
		case 25:
			npc("began to blur, my brain was throbbing. I fell and hit", "my head on my worktop, then it all went black.");
			stage++;
			break;
		case 26:
			player("Ouch! But, you still haven't said who changed you?");
			stage++;
			break;
		case 27:
			npc("Will you let me finish??");
			stage++;
			break;
		case 28:
			npc("I woke up screaming in pain. Blue foam streaming out", "of my mouth, my eye sight worse than before, the only", "things I could make out were 3 tall figures with green", "banners - they were of the Archeus Elders. They");
			stage++;
			break;
		case 29:
			npc("muttered to each other in Archaic Language. After", "trying to get up several times, I lost hope and stared at", "the sky. I had given up when the tallest of the figures", "bent over and brought his face right to mine and spoke");
			stage++;
			break;
		case 30:
			npc("very softly, 'We can save you, but it will come at a", "cost'.");
			stage++;
			break;
		case 31:
			player("So they saved you?");
			stage++;
			break;
		case 32:
			npc("Yes of course they saved me but look at the cost! I", "have horns coming out of my head and I'm still blind", "and stuck here... forever alone.");
			stage = 7;
			break;
		case 40:
			player("What monstrosity? What's behind those gates?");
			stage++;
			break;
		case 41:
			npc("You really do not want to know. Leave this place", "human.");
			stage++;
			break;
		case 42:
			player("Hey now, I'm no wimp. What's to stop me from just", "turning the winch to open the gate and going in?");
			stage++;
			break;
		case 43:
			npc("Me. And of course your quick demise at the mercy of", "Cerberus, guardian of the river of souls.");
			stage++;
			break;
		case 44:
			player("But I can do it!");
			stage++;
			break;
		case 45:
			npc("You are obviously passionate at trying. But only those", "with great skill at slaying these types of beast may", "enter.");
			stage++;
			break;
		case 46:
			npc("THE VOICES! AAAHHHHH! Leave this place human!");
			stage = 7;
			break;
		case 50:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 8673 };
	}

}
