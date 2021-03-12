package core.game.content.dialogue;

import core.game.content.global.Skillcape;
import core.plugin.Initializable;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for sloane.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class SloaneDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code SloaneDialogue} {@code Object}.
	 */
	public SloaneDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code SloaneDialogue} {@code Object}.
	 * @param player the player.
	 */
	public SloaneDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new SloaneDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		if (Skillcape.isMaster(player, Skills.STRENGTH)) {
			options("Ask about Skillcape", "Something else");
			stage = 0;
		} else {
			npc("Ahhh, hello there, " + player.getUsername() + ".");
			stage = 10;
		}
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			switch (buttonId) {
			case 1:
				player("Can I buy a Skillcape of Strength?");
				stage = 100;
				break;
			case 2:
				npc("Ahhh, hello there, " + player.getUsername() + ".");
				stage = 10;
				break;
			}
			break;
		case 10:
			options("What can I do here?", "That's a big axe!", "May I claim my tokens please?", "Bye!");
			stage++;
			break;
		case 11:
			switch (buttonId) {
			case 1:
				player("What can I do here?");
				stage = 12;
				break;
			case 2:
				player("That's a big axe!");
				stage = 30;
				break;
			case 3:
				close();
				player.getDialogueInterpreter().open("wg:claim-tokens", npc.getId());
				break;
			case 4:
				player("Bye!");
				stage = 50;
				break;
			}
			break;
		case 12:
			npc("Ahh, the shot put is a great test of strength and can be", "quite rewarding. Mind you do it properly though, you", "might want to dust your hands with some powdery", "substance first. It'll give better grip.");
			stage++;
			break;
		case 13:
			end();
			break;
		case 30:
			npc("Yes indeed it is. Have to be mighty strong to wield it", "too.");
			stage++;
			break;
		case 31:
			player("But you don't look that strong!");
			stage++;
			break;
		case 32:
			npc("Maybe, maybe not, but I still had to beat a Barbarian", "to get it. Mind you, usually they don't part with them.", "This was an unusual circumstance.");
			stage++;
			break;
		case 33:
			end();
			break;
		case 50:
			npc("Be well, warrior " + player.getUsername() + ".");
			stage++;
			break;
		case 51:
			end();
			break;
		case 40:
			close();
			player.getDialogueInterpreter().open("wg:claim-tokens", npc.getId());
			break;
		case 100:
			npc("Certainly! Right when you give me 99000 coins.");
			stage = 101;
			break;
		case 101:
			options("Okay, here you go.", "No, thanks.");
			stage = 102;
			break;
		case 102:
			switch (buttonId) {
			case 1:
				player("Okay, here you go.");
				stage = 103;
				break;
			case 2:
				end();
				break;
			}
			break;
		case 103:
			if (Skillcape.purchase(player, Skills.STRENGTH)) {
				npc("There you go! Enjoy.");
			}
			stage = 104;
			break;
		case 104:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 4297 };
	}
}
