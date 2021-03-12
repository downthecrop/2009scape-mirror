package core.game.content.zone.phasmatys;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;

/**
 * Handles the dialogue used for necrovarus.
 * @author Vexia
 */
public final class NecrovarusDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code NecrovarusDialogue} {@code Object}.
	 */
	public NecrovarusDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code NecrovarusDialogue} {@code Object}.
	 * @param player the player.
	 */
	public NecrovarusDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new NecrovarusDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		options("What is this place?", "What happened to everyone here?", "How do I get into the town?");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			switch (buttonId) {
			case 1:
				player("What is this place?");
				stage = 10;
				break;
			case 2:
				player("What happened to everyone here?");
				stage = 20;
				break;
			case 3:
				player("How do I get into the town?");
				stage = 30;
				break;
			}
			break;
		case 10:
			npc("Speak to me again and I will rend the soul from your", "flesh.");
			stage++;
			break;
		case 11:
			end();
			break;
		case 20:
			npc("You dare to speak to me??? Have you lost your", "wits????");
			stage++;
			break;
		case 21:
			end();
			break;
		case 30:
			npc("I do not answer questions, mortal fool!");
			stage++;
			break;
		case 31:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 1684 };
	}

}
