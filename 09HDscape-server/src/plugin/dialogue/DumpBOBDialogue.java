package plugin.dialogue;

import org.crandor.game.container.Container;
import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.node.item.Item;

/**
 * Represents the dialogue plugin used to handle the DUMP BOB button
 * @author Splinter
 */
@InitializablePlugin
public final class DumpBOBDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code DumpBOBDialogue} {@code Object}.
	 */
	public DumpBOBDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code DumpBOBDialogue} {@code Object}.
	 * @param player the player.
	 */
	public DumpBOBDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new DumpBOBDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		options("Deposit Inventory", "Deposit Equipment", "Deposit Beast of Burden", "Cancel");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			switch (buttonId) {
			case 1:
				if (player.getInventory().isEmpty()) {
					player.getPacketDispatch().sendMessage("You have nothing to deposit.");
					return true;
				} else {
					dump(player.getInventory());
				}
				end();
				break;
			case 2:
				if (player.getEquipment().isEmpty()) {
					player.getPacketDispatch().sendMessage("You have nothing to deposit.");
					return true;
				} else {
					dump(player.getEquipment());
				}
				end();
				break;
			case 3:
				player.getFamiliarManager().dumpBob();
				end();
				break;
			case 4:
				end();
				break;
			}
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 628371 };
	}

	/**
	 * Dumps a container.
	 * @param container the container.
	 */
	public void dump(Container container) {
		for (Item i : container.toArray()) {
			if (i == null) {
				continue;
			}
			if (!player.getBank().canAdd(i)) {
				player.getPacketDispatch().sendMessage("A magical force prevents you from banking your equipment.");
				return;
			}
			if (!player.getBank().hasSpaceFor(i)) {
				player.getPacketDispatch().sendMessage("There is not enough space left in your bank.");
				return;
			}
			if (container.remove(i)) {
				player.getBank().add(i.getDefinition().isUnnoted() ? i : new Item(i.getNoteChange(), i.getAmount()));
			}
		}
	}
}
