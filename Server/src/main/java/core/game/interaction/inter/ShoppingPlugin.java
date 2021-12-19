package core.game.interaction.inter;

import static api.ContentAPIKt.*;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.container.Container;
import core.game.content.global.shop.ShopViewer;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.RunScript;
import core.game.node.item.Item;
import core.net.packet.in.ExaminePacket;
import core.plugin.Initializable;
import core.plugin.Plugin;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * Represents the plugin used to handle the shopping interface.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class ShoppingPlugin extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.forId(620).setPlugin(this);
		ComponentDefinition.forId(621).setPlugin(this);
		return this;
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		final ShopViewer viewer = player.getExtension(ShopViewer.class);
		if (player.getAttributes().containsKey("spawning_items")) {
			switch (opcode) {
				case 155:
					switch (button) {
						case 23:
						case 24:
						case 0:
							viewer.getShop().give(player, slot, 1, viewer.getTabIndex());
							break;
					}
					break;
			}
			return true;
		}
		if (viewer == null) {
			System.out.println("shop is null");
			return true;
		}
		final Container container = button == 0 ? player.getInventory() : viewer.getShop().getContainer(viewer.getTabIndex());
		switch (opcode) {
		case 155:
			switch (button) {
			case 23:
			case 24:
			case 0:
				value(player, viewer, container.get(slot), component.getId() == 621);
				break;
			case 25:
			case 26:
				viewer.showStock(button == 26 ? 1 : 0);
				break;
			}
			break;
		case 9:
			int id = container.getId(slot);
			if (id == -1) {
				return true;
			}
			player.getPacketDispatch().sendMessage(ExaminePacket.getItemExamine(id));
			break;
		case 196:
		case 199:
		case 124:
		case 234:
			final int amount = getAmount(opcode);
			if (player.getAttribute("shop-delay", 0L) > System.currentTimeMillis()) {
				return true;
			}
			player.setAttribute("shop-delay", System.currentTimeMillis() + 500);
			if (amount == -1) {
				player.setAttribute("runscript", getRunScript(viewer, slot, component.getId()));
				player.getDialogueInterpreter().sendInput(false, "Enter the amount:");
				break;
			}
			if (component.getId() == 620) {
				viewer.getShop().buy(player, slot, amount, viewer.getTabIndex());
			} else {
				viewer.getShop().sell(player, slot, amount, viewer.getTabIndex());
			}
			break;
		}
		return true;
	}

	/**
	 * Method used to value an item.
	 * @param player the player.
	 * @param viewer the viewer.
	 * @param item the item.
	 * @param sell the sell.
	 */
	private void value(final Player player, final ShopViewer viewer, final Item item, final boolean sell) {
		if (item == null) {
			return;
		}
		viewer.getShop().value(player, viewer, item, sell);
	}

	/**
	 * Gets the run script for selling an item.
	 * @return the script.
	 * @param slot the slot.
	 */
	private Function1 getRunScript(final ShopViewer viewer, final int slot, final int componentId) {
		return (value) -> {
			switch (componentId){
				case 620:
					viewer.getShop().buy(viewer.getPlayer(), slot, (int) value, viewer.getTabIndex());
					break;
				case 621:
					viewer.getShop().sell(viewer.getPlayer(), slot, (int) value, viewer.getTabIndex());
					break;
			}
			return Unit.INSTANCE;
		};
	}

	/**
	 * Gets the amount by the opcode.
	 * @param opcode the opcode.
	 * @return the amount.
	 */
	private int getAmount(int opcode) {
		return opcode == 196 ? 1 : opcode == 124 ? 5 : opcode == 199 ? 10 : -1;
	}
}
