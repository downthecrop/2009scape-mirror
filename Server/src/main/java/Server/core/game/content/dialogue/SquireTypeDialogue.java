package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the different tier of squire dialouges.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class SquireTypeDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code SquireTypeDialogue} {@code Object}.
	 */
	public SquireTypeDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code SquireTypeDialogue} {@code Object}.
	 * @param player the player.
	 */
	public SquireTypeDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new SquireTypeDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		npc("Hi, how can I help you?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			options("Who are you?", "What's going on here?", "I'm fine thanks.");
			stage = 1;
			break;
		case 1:
			switch (buttonId) {
			case 1:
				player("Who are you?");
				stage = 10;
				break;
			case 2:
				player("What's going on here?");
				stage = 20;
				break;
			case 3:
				player("I'm fine thanks.");
				stage = 30;
				break;
			}
			break;
		case 10:
			npc("I'm a squire for the Void Knights.");
			stage = 11;
			break;
		case 11:
			player("The who?");
			stage = 12;
			break;
		case 12:
			npc("The Void Knights, they are great warriors of balance", "who do Guthix's work here in 2009scape.");
			stage = 13;
			break;
		case 13:
			end();
			break;
		case 20:
			npc("This is where we launch our landers to combat the", "invasion of the nearby islands. You'll see three landers - ", "one for novice, intermediate and veteran adventurers.", "Each has a different difficulty, but they therefore offer");
			stage = 21;
			break;
		case 21:
			npc("varying rewards.");
			stage = 22;
			break;
		case 22:
			player("And this lander is...?");
			stage = 23;
			break;
		case 23:
			npc("The " + (npc.getId() == 3802 ? "novice" : npc.getId() == 6140 ? "intermediate" : "veteran") + ".");
			stage = 24;
			break;
		case 24:
			player("So how do they work?");
			stage = 25;
			break;
		case 25:
			npc("Simple. We send each lander out every five minutes,", "however we need at least five volunteers or it's a suicide", "mission. The lander can only hold a maximum of", "twenty five people though, so we do send them out");
			stage = 26;
			break;
		case 26:
			npc("early if they get full. If you'd be willing to help us then", "just wait in the lander and we'll launch it as soon as it's", "ready. However you will need a combat level of " + (npc.getId() == 3802 ? "40" : npc.getId() == 6140 ? "70" : "100") + " to", "use this lander.");
			stage = 27;
			break;
		case 27:
			end();
			break;
		case 30:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 3802, 6140, 6141 };
	}
}
