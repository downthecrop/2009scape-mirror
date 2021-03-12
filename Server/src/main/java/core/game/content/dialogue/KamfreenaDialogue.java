package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Handles the KamfreenaDialogue dialogue.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class KamfreenaDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code KamfreenaDialogue} {@code Object}.
	 */
	public KamfreenaDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code KamfreenaDialogue} {@code Object}.
	 * @param player the player.
	 */
	public KamfreenaDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new KamfreenaDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(npc, null, "Why hello there! I'm Kamfreena. Like the look of my pets?", "I think they're eyeing you up.");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendDialogues(player, null, "That was a really bad pun.");
			stage = 1;
			break;
		case 1:
			interpreter.sendDialogues(npc, null, "Sorry...I don't get to see the rest of the guild much", "stuck up here. The cyclopes don't talk much you see.");
			stage = 2;
			break;
		case 2:
			interpreter.sendDialogues(player, null, "Shouldn't that be cyclopses?");
			stage = 3;
			break;
		case 3:
			interpreter.sendDialogues(npc, null, "Nope! Cyclopes is the plural of cyclops. One cyclops,", "many cyclopes.");
			stage = 4;
			break;
		case 4:
			interpreter.sendDialogues(player, null, "Oh, right, thanks.");
			stage = 5;
			break;
		case 5:
			interpreter.sendOptions("Select an Option", "Where are they from?", "How did they get here?", "Why are they here?", "Bye!");
			stage = 6;
			break;
		case 6:
			if (buttonId == 1) {
				interpreter.sendDialogues(player, null, "Where are they from?");
				stage = 7;
			} else if (buttonId == 2) {
				interpreter.sendDialogues(player, null, "How did they get here?");
				stage = 8;
			} else if (buttonId == 3) {
				interpreter.sendDialogues(player, null, "Why are they here?");
				stage = 9;
			} else if (buttonId == 4) {
				interpreter.sendDialogues(player, null, "Bye!");
				stage = 20;
			}
			break;
		case 7:
			interpreter.sendDialogues(npc, null, "They're from the far east lands.");
			stage = 5;
			break;
		case 8:
			interpreter.sendDialogues(npc, null, "Ahh.. our guildmaster, Harrallak, went on an expedition", "there. He brought them back with him.");
			stage = 5;
			break;
		case 9:
			interpreter.sendDialogues(npc, null, "For the warriors to train on of course! They also drop a", "rather nice blade.");
			stage = 10;
			break;
		case 10:
			interpreter.sendDialogues(player, null, "Oh? What would that be?");
			stage = 11;
			break;
		case 11:
			interpreter.sendDialogues(npc, null, "Defenders.");
			stage = 12;
			break;
		case 12:
			interpreter.sendDialogues(player, null, "Err what are they?");
			stage = 13;
			break;
		case 13:
			interpreter.sendDialogues(npc, null, "It's a blade you can defend with using your shield hand,", "like I have.");
			stage = 14;
			break;
		case 14:
			interpreter.sendDialogues(player, null, "Wow!");
			stage = 15;
			break;
		case 15:
			interpreter.sendDialogues(npc, null, "For every 10 tokens you collect around the guild, you", "can spend one minute in with my pets. As you get", "defenders you can show them to me to earn even", "better ones... but remember if you lose them you'll have");
			stage = 16;
			break;
		case 16:
			interpreter.sendDialogues(npc, null, "to start at bronze again. I'd advise keeping a spare in", "your bank.");
			stage = 17;
			break;
		case 17:
			interpreter.sendDialogues(player, null, "Ok!");
			stage = 18;
			break;
		case 18:
			interpreter.sendDialogues(npc, null, "Oh, by the way, you need to earn 100 tokens", "before I'll let you in!");
			stage = 19;
			break;
		case 19:
			interpreter.sendDialogues(player, null, "Right, I'd better go play some games then!");
			stage = 5;
			break;
		case 20:
			interpreter.sendDialogues(npc, null, "See you back here soon I hope!");
			stage = 21;
			break;
		case 21:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 4289 };
	}
}
