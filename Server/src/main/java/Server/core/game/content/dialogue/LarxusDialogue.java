package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for the larxus npc.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class LarxusDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code LarxusDialogue} {@code Object}.
	 */
	public LarxusDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code LarxusDialogue} {@code Object}.
	 * @param player the player.
	 */
	public LarxusDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new LarxusDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		if (args.length == 2) {
			npc("You need to arrange a challenge with me before you", "enter the arena.");
			stage = 30;
			return true;
		}
		npc = (NPC) args[0];
		npc("Is there something I can help you with?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			options("I was given a challenge, what now?", "What is this place?", "Nothing thanks.");
			stage = 1;
			break;
		case 1:
			switch (buttonId) {
			case 1:
				player("I was given a challenge, what now?");
				stage = 10;
				break;
			case 2:
				player("What is this place?");
				stage = 20;
				break;
			case 3:
				player("Nothing thanks.");
				stage = 30;
				break;
			}
			break;
		case 10:
			npc("Well pass it here and we'll get you started.");
			stage = 11;
			break;
		case 11:
			end();
			break;
		case 20:
			npc("This is the champions' arena, the champions of various", "races use it to duel those they deem worthy of the", "honour.");
			stage = 21;
			break;
		case 21:
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
		return new int[] { 3050 };
	}
}
