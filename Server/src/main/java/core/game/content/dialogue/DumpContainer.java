package core.game.content.dialogue;

import core.game.container.Container;
import core.game.container.impl.BankContainer;
import core.game.container.impl.EquipmentContainer;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.game.node.item.Item;
import core.plugin.Plugin;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents the dialogue plugin used to handle the deposit all/deposit inventory/etc button
 * @author Splinter
 */
@Initializable
public final class DumpContainer extends DialoguePlugin {

	/**
	 * Constructs a new {@code DumpContainer} {@code Object}.
	 */
	public DumpContainer() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code DumpBOBDialogue} {@code Object}.
	 * @param player the player.
	 */
	public DumpContainer(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new DumpContainer(player);
	}

	@Override
	public boolean open(Object... args) {
		options("Deposit Inventory", "Deposit Equipment", "Deposit Beast of Burden", "Cancel");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		if (stage == 0) {
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
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 628371 };
	}

	/**
	 * Dumps a container.
	 * @param inventory the player's inventory.
	 * @author ceik
	 */
	public void dump(Container inventory) {
		BankContainer bank = player.getBank();
		Arrays.stream(inventory.toArray()).filter(Objects::nonNull).forEach(item -> {
			if(!bank.hasSpaceFor(item)){
				player.getPacketDispatch().sendMessage("You have no more space in your bank.");
				return;
			}
			if(!bank.canAdd(item)){
				player.getPacketDispatch().sendMessage("A magical force prevents you from banking your " + item.getName() + ".");
			} else {
				if(inventory instanceof EquipmentContainer){
					Object pluginobj = item.getDefinition().getHandlers().get("equipment");
					if(pluginobj != null && pluginobj instanceof Plugin){
						Plugin<Item> plugin = (Plugin<Item>) pluginobj;
						plugin.fireEvent("unequip",player,item);
					}
				}
				inventory.remove(item);
				bank.add(item.getDefinition().isUnnoted() ? item : new Item(item.getNoteChange(), item.getAmount()));
			}
		});
		inventory.update();
		bank.update();
	}
}
