package core.game.content.dialogue;

import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.game.node.item.Item;

/**
 * Handles the velorina dialogue.
 * @author Vexia
 */
@Initializable
public class VelorinaDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code VelorinaDialogue} {@code Object}
	 */
	public VelorinaDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code VelorinaDialogue} {@code Object}
	 * @param player the player.
	 */
	public VelorinaDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new VelorinaDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		player("Can I have another ectophial?");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			if (player.hasItem(new Item(4251)) || player.hasItem(new Item(4252))) {
				npc("You already have an ectophial.");
				stage = 1;
				return true;
			}
			npc("Of course you can, you have helped us more than we", "could ever have hoped.");
			stage = 2;
			break;
		case 1:
			end();
			break;
		case 2:
			interpreter.sendItemMessage(4251, "Velorina gives you a vial of bright green ectoplasm.");
			stage++;
			break;
		case 3:
			player.getInventory().add(new Item(4251), player);
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 1683 };
	}

}
