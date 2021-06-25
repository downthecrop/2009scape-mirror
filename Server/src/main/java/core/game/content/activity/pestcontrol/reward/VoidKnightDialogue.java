package core.game.content.activity.pestcontrol.reward;

import core.plugin.Initializable;
import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;

/**
 * Represents the void knight dialogue.
 * 
 * @author 'Vexia
 */
@Initializable
public final class VoidKnightDialogue extends DialoguePlugin {

	/**
	 * Reward item
	 */
	Item reward = new Item();

	/**
	 * Repreents the void knight ids.
	 */
	public static final int IDS[] = new int[] { 3786, 3788, 3789, 5956 };

	/**
	 * Constructs a new {@code VoidKnightDialogue} {@code Object}.
	 */
	public VoidKnightDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code VoidKnightDialogue} {@code Object}.
	 * 
	 * @param player
	 *            the player.
	 */
	public VoidKnightDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new VoidKnightDialogue(player);
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
			options("Who are you?", "What is this place?", "I'm fine thanks.");
			stage = 1;
			break;
		case 1:
			switch (buttonId) {
			case 1:
				player("Who are you?");
				stage = 10;
				break;
			case 2:
				player("What is this place?");
				stage = 20;
				break;
			case 3:
				player("I'm fine thanks.");
				stage = 30;
				break;
			}
			break;
		case 10:
			npc("I'm a Void Knight, one of the order of Guthix. We are",
					"warriors of balance who do Guthix's work here in",
					"2009scape.");
			stage = 11;
			break;
		case 11:
			options("Wow, can I join?", "What kind of work?",
					"What's '2009scape'?", "Uh huh, sure.");
			stage = 12;
			break;
		case 12:
			switch (buttonId) {
			case 1:
				player("Wow, can I join?");
				stage = 180;
				break;
			case 2:
				player("What kind of work?");
				stage = 17;
				break;
			case 3:
				player("What's '2009scape'?");
				stage = 15;
				break;
			case 4:
				player("Uh huh, sure.");
				stage = 14;
				break;
			}
			break;
		case 14:
			end();
			break;
		case 15:
			npc("It is the name that Guthix gave to this world, so we",
					"honour him with its use.");
			stage = 16;
			break;
		case 16:
			end();
			break;
		case 17:
			npc("Ah well you see we try to keep 2009scape as Guthix",
					"intended, it's very challenging. Actually we've been",
					"having some problems recently, maybe you could help",
					"us?");
			stage = 18;
			break;
		case 18:
			options("Yeah ok, what's the problem?", "What's '2009scape'?",
					"I'd rather not, sorry.");
			stage = 19;
			break;
		case 19:
			switch (buttonId) {
			case 1:
				player("Yeah ok, what's the problem?");
				stage = 191;
				break;
			case 2:
				player("What's '2009scape'?");
				stage = 15;
				break;
			case 3:
				player("I'd rather not sorry.");
				stage = 190;
				break;
			}
			break;
		case 180:
			npc("Entry is strictly invite only, however we do need help",
					"continuing Guthix's work.");
			stage = 181;
			break;
		case 181:
			player("What kind of work?");
			stage = 17;
			break;
		case 190:
			end();
			break;
		case 191:
			npc("Well the order has become quite diminished over the",
					"years, it's a very long process to learn the skills of a",
					"Void Knight. Recently there have been breaches into",
					"our realm from somewhere else, and strange creatures");
			stage = 192;
			break;
		case 192:
			npc("have been pouring through. We can't let that happen,",
					"and we'd be very grateful if you'd help us.");
			stage = 193;
			break;
		case 193:
			options("How can I help?", "Sorry, but I can't.");
			stage = 194;
			break;
		case 194:
			switch (buttonId) {
			case 1:
				player("How can I help?");
				stage = 195;
				break;
			case 2:
				player("Sorry, but I can't.");
				stage = 190;
				break;
			}
			break;
		case 195:
			npc("We send launchers from our outpost to the nearby",
					"islands. If you go and wait in the lander there that'd",
					"really help.");
			stage = 196;
			break;
		case 196:
			end();
			break;
		case 20:
			npc("This is our outpost. From here we send launchers out to",
					"the nearby islands to beat back the invaders.");
			stage = 21;
			break;
		case 21:
			options("What invaders?", "How can I help?", "Good luck with that.");
			stage = 22;
			break;
		case 22:
			switch (buttonId) {
			case 1:
				player("What invaders?");
				stage = 24;
				break;
			case 2:
				player("How can I help?");
				stage = 195;
				break;
			case 3:
				player("Good luck with that.");
				stage = 23;
				break;
			}
			break;
		case 23:
			end();
			break;
		case 24:
			npc("Recently there have been breaches into our realm from",
					"somewhere else, and strange creatures have been",
					"pouring through. We can't let that happen, and we'd be",
					"very grateful if you'd help us.");
			stage = 193;
			break;
		case 30:
			end();
			break;
		case 43:
			if (player.getSavedData().getActivityData().getPestPoints() < 150) {
				player("I don't have that many points at the moment.");
				stage = 30;
			} else {
				player("Alright, here's one hundred and fifty points.");
				stage = 44;
			}
			break;
		case 44:
			player.getSavedData()
					.getActivityData()
					.setPestPoints(
							player.getSavedData().getActivityData()
									.getPestPoints() - 150);
			interpreter.sendItemMessage(reward, "The Void Knight hands over a "
					+ reward.getName() + ".");
			if (player.getInventory().freeSlots() < 1) {
				GroundItemManager.create(reward, player.getLocation());
			} else {
				player.getInventory().add(reward);
			}
			stage = 30;
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return IDS;
	}
}
