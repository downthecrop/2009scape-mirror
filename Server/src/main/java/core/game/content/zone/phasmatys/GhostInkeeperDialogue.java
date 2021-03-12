package core.game.content.zone.phasmatys;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;

/**
 * Handles the ghost inkeeper dialogue.
 * @author Vexia
 */
public class GhostInkeeperDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code GhostInkeeperDialogue} {@code Object}.
	 * @param player the player.
	 */
	public GhostInkeeperDialogue(final Player player) {
		super(player);
	}

	/**
	 * Constructs a new {@code GhostInkeeperDialogue} {@code Object}.
	 */
	public GhostInkeeperDialogue() {
		/**
		 * empty.
		 */
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new GhostInkeeperDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		if (PhasmatysZone.hasAmulet(player)) {
			player("Hello there!");
		} else {
			npc("Woooo wooo wooooo woooo");
			stage = 10;
		}
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			npc("Greetings, traveller. Welcome to 'The Green Ghost'", "Tavern. What can I do you for?");
			stage++;
			break;
		case 1:
			options("Can I buy a beer?", "Can I hear some gossip?", "What happened to the folk of this town?", "Do you have any jobs I can do?", "Nothing, thanks.");
			stage++;
			break;
		case 2:
			switch (buttonId) {
			case 1:
				player("Can I buy a beer?");
				stage = 3;
				break;
			case 2:
				player("Can I hear some gossip?");
				stage = 20;
				break;
			case 3:
				player("What happened to the folk of this town?");
				stage = 30;
				break;
			case 4:
				player("Do you have any jobs I can do?");
				stage = 40;
				break;
			case 5:
				player("Nothing, thanks.");
				stage = 50;
				break;
			}
			break;
		case 3:
			npc("Sorry, but our pumps dried up over half a century", "ago. We of this village do not have much of a thirst", "these days.");
			stage++;
			break;
		case 4:
			end();
			break;
		case 20:
			npc("I suppose, as long as you keep it to yourself...");
			stage++;
			break;
		case 21:
			npc("You see Gravingas out there in the marketplace? He", "speaks for the silent majoirty of Port Phasmatys, for", "those of us who would prefer to pass over into the next", "world.");
			stage++;
			break;
		case 22:
			npc("But old Gravingas is far too obvious in his methods.", "Now Velorina, she's a ghost of a different colour", "altogether. If you feel like helping our cause at all, go", "speak to Velorina.");
			stage++;
			break;
		case 23:
			end();
			break;
		case 30:
			npc("That's a long story, my friend, but you look like you", "have the time...");
			stage++;
			break;
		case 31:
			player("Nope, you are right. I am very busy!");// .. I dont want
			// to do this
			// dialogue.
			stage++;
			break;
		case 32:
			end();
			break;
		case 40:
			npc("Yes, actually, I do. We have a very famous Master", "Bowman named Robin staying with us at the moment.", "Could you take him some clean bed linen for me?");
			stage++;
			break;
		case 41:
			npc("No, I didn't mean a job like that.");
			stage++;
			break;
		case 42:
			end();
			break;
		case 50:
			end();
			break;
		case 10:
			interpreter.sendDialogue("You cannot understand the ghost.");
			stage++;
			break;
		case 11:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 1700 };
	}

}
