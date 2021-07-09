package core.game.content.zone.rellekka;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import rs09.game.util.region.rellekka.RellekkaDestination;
import rs09.game.util.region.rellekka.RellekkaUtils;

/**
 * Handles the maria gunnars dialogue.
 * @author Vexia
 */
public class MariaGunnarsDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code MariaGunnarsDialogue} {@code Object}
	 */
	public MariaGunnarsDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code MariaGunnarsDialogue} {@code Object}
	 * @param player the player.
	 */
	public MariaGunnarsDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new MariaGunnarsDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		npc("Welcome, Talvald. Do you have any questions?");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			options("Can you ferry me to " + (npc.getId() == 5508 ? "Neitiznot?" : "Relleka") + "?", "I just stopped to say 'hello'.");
			stage++;
			break;
		case 1:
			if (buttonId == 1) {
				player("Can you ferry me to " + (npc.getId() == 5508 ? "Neitiznot?" : "Relleka") + "?");
				stage++;
			} else {
				player("I just stopped to say 'hello'.");
				stage = 4;
			}
			break;
		case 2:
			npc("Let's set sail then.");
			stage++;
			break;
		case 3:
			if (npc.getId() == 5508) {
				RellekkaUtils.sail(player, RellekkaDestination.RELLEKKA_TO_NEITIZNOT);
			} else {
				RellekkaUtils.sail(player, RellekkaDestination.NEITIZNOT_TO_RELLEKKA);
			}
			end();
			break;
		case 4:
			npc("Thanks!");
			stage++;
			break;
		case 5:
			player("I may be back later.");
			stage++;
			break;
		case 6:
			end();
			npc.sendChat("Bye");
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 5508, 5507 };
	}

}
