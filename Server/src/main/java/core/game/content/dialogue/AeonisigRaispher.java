package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the npc Aeonisig Raispher dialogue plugin.
 * @author 'Vexia
 * @version 1.5
 * @note Found transcript on
 * (http://2009scape.wikia.com/wiki/Aeonisig_Raispher/dialogue)
 */
@Initializable
public final class AeonisigRaispher extends DialoguePlugin {

	/**
	 * Represents the public id of this dialogue.
	 */
	public static final int ID = 4710;

	/**
	 * Constructs a new {@code AeonisigRaispher} {@code Object}.
	 */
	public AeonisigRaispher() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code AeonisigRaispher} {@code Object}.
	 * @param player the player.
	 */
	public AeonisigRaispher(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new AeonisigRaispher(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		npc("Please only talk to the King if it's important. He has a", "heavy burden to bear with the running of his Kingdom.");
		stage = -1;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case -1:
			options("Who are you?", "What do you do here?", "Where did you come from?", "How did you come to be an advisor to King Roald?", "What is happening in the kingdom?");
			stage = -2;
			break;
		case -2:
			switch (buttonId) {
			case 1:
				player("Who are you?");
				stage = 1;
				break;
			case 2:
				player("What do you do here?");
				stage = 20;
				break;
			case 3:
				player("Where did you come from?");
				stage = 30;
				break;
			case 4:
				player("How did you come to be an advisor to King Roald?");
				stage = 40;
				break;
			case 5:
				player("What is happening in the kingdom?");
				stage = 50;
				break;
			}
			break;
		case 20:
			npc("My main function is to ensure that King Roald is apprised", "of all options, especially those that favour the", "righteous followers of Saradomin.");
			stage = 21;
			break;
		case 21:
			player("But surely the King should be able to make", "his own decisions on what's best for Misthalin?");
			stage = 22;
			break;
		case 22:
			npc("What an interesting perspective you have! Totally", "unworkable of course, but interesting nonetheless.");
			stage = 23;
			break;
		case 23:
			end();
			break;
		case 30:
			npc("I took my religious and combat training in several parts", "of the known world. I've also fought despicable beasts", "in the wilderness in Saradomin's name. Needless to say I", "have great experienc in the ways of the world and am an");
			stage = 29;
			break;
		case 29:
			npc("invaluable aid to his ordship's decision making process.");
			stage = 31;
			break;
		case 31:
			end();
			break;
		case 40:
			npc("The King of Misthalin, like any great leader, always", "requires the best advice and the best advisors. He very", "often summons occasional advisors to help him in", "certain situations, but it was felt by the Church");
			stage = 39;
			break;
		case 39:
			npc("of Saradomin that a full time advisor on religious matters", "was needed to ensure fair treatement", "of Saradomin's followers.");
			stage = 41;
			break;
		case 41:
			player("How come he doesn't have an advisor for", "any other religious denominations?");
			stage = 42;
			break;
		case 42:
			npc("Because I simply won't stand for it, that's why!", "Now, enough of your impertinent questions.", "I have work to do!");
			stage = 43;
			break;
		case 43:
			end();
			break;
		case 50:
			npc("Nothing out of the usual.");
			stage = 51;
			break;
		case 51:
			end();
			break;
		case 1:
			npc("Apologies! Allow me to introduce myself. My", "name is Aeonisig Raispher, special advisor", "to King Roald on spiritual matters.");
			stage = 2;
			break;
		case 2:
			npc("It means that some decisions the King has to make might", "have unforeseen repercussions on the nation's spiritual", "sensibilities. My duty is to ensure that Saradomin", "ideals are not stomped underfoot.");
			stage = 3;
			break;
		case 3:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { ID };
	}
}
