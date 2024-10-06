package content.global.skill.summoning.familiar;

import core.game.dialogue.DialogueInterpreter;
import core.game.dialogue.DialoguePlugin;
import core.plugin.Initializable;
import content.global.skill.summoning.pet.Pet;
import core.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used to dismiss a follower.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class DismissDialoguePlugin extends DialoguePlugin {

	/**
	 * Constructs a new {@code DismissDialoguePlugin} {@code Object}.
	 */
	public DismissDialoguePlugin() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code DismissDialoguePlugin} {@code Object}.
	 * @param player the player.
	 */
	public DismissDialoguePlugin(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new DismissDialoguePlugin(player);
	}

	@Override
	public boolean open(Object... args) {
		if (player.getFamiliarManager().getFamiliar() instanceof Pet) {
			interpreter.sendOptions("Free pet", "Yes", "No");
		} else {
			interpreter.sendOptions("Dismiss Familiar", "Yes", "No");
		}
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			switch (buttonId) {
			case 1:
				if (player.getFamiliarManager().getFamiliar() instanceof Pet) {
					interpreter.sendDialogues(player, null, "Run along; I'm setting you free.");
					Pet pet = (Pet) player.getFamiliarManager().getFamiliar();
					player.getFamiliarManager().removeDetails(pet.getItemId());
				} else {
					end();
				}
				player.getFamiliarManager().dismiss();
				stage = 1;
				break;
			case 2:
				end();
				break;
			}
			break;
		case 1:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { DialogueInterpreter.getDialogueKey("dismiss_dial") };
	}
}
