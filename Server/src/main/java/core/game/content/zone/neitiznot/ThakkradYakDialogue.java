package core.game.content.zone.neitiznot;

import core.game.content.dialogue.DialogueInterpreter;
import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;

/**
 * Handles the thakkrad yak dialogue.
 * @author Vexia
 */
public class ThakkradYakDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code ThakkradYakDialogue} {@code Object}
	 */
	public ThakkradYakDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code ThakkradYakDialogue} {@code Object}
	 * @param player the player.
	 */
	public ThakkradYakDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new ThakkradYakDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		options("Cure my yak-hide, please.", "Nothing, thanks.");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			switch (buttonId) {
			case 1:
				player("Cure my yak-hide, please.");
				stage = 4;
				break;
			case 2:
				player("Nothing, thanks.");
				stage++;
				break;
			}
			break;
		case 1:
			interpreter.sendDialogues(5506, null, "See you later.");
			stage++;
			break;
		case 2:
			interpreter.sendDialogues(5506, null, "You won't find anyone else who can cure yak-hide.");
			stage++;
			break;
		case 3:
			end();
			break;
		case 4:
			interpreter.sendDialogues(5506, null, "I will cure yak-hide for a fee of 5 gp per hide.");
			stage++;
			break;
		case 5:
			if (!player.getInventory().contains(10818, 1)) {
				interpreter.sendDialogues(5506, null, "You have no yak-hide to cure.");
				stage = 7;
				break;
			}
			if (!player.getInventory().contains(995, 5)) {
				interpreter.sendDialogues(5506, null, "You don't have enough gold to pay me!");
				stage = 7;
				break;
			}
			options("Cure all my hides.", "Cure one hide.", "Cure no nide.", "Can you cure any other type of leather?");
			stage++;
			break;
		case 6:
			switch (buttonId) {
			case 1:
			case 2:
				cure(player, buttonId == 2 ? 1 : player.getInventory().getAmount(10818));
				stage = 8;
				break;
			case 3:
				interpreter.sendDialogues(5506, null, "Bye!");
				stage = 7;
				break;
			case 4:
				interpreter.sendDialogues(5506, null, "Other types of leather? Why would you need any other", "type of leather?");
				stage = 40;
				break;
			}
			break;
		case 7:
			end();
			break;
		case 40:
			player("I'll take that as a no then.");
			stage = 7;
			break;
		case 8:
			end();
			break;
		}
		return true;
	}

	/**
	 * Cures the yak hide.
	 * @param player the player.
	 * @param amount the amount.
	 * @return {@code True} if cured.
	 */
	private boolean cure(Player player, int amount) {
		if (!player.getInventory().contains(995, 5 * amount)) {
			interpreter.sendDialogues(5506, null, "You don't have enough gold to pay me!");
			return false;
		}
		if (player.getInventory().remove(new Item(995, 5 * amount))) {
			for (int i = 0; i < amount; i++) {
				if (player.getInventory().remove(new Item(10818))) {
					player.getInventory().add(new Item(10820));
				}
			}
		}
		player("There you go!");
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { DialogueInterpreter.getDialogueKey("thakkrad-yak") };
	}

}
