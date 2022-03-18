package core.game.interaction.inter;

import static api.ContentAPIKt.*;
import core.cache.def.impl.CS2Mapping;
import core.cache.def.impl.ItemDefinition;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.ge.GEGuidePrice;
import core.game.ge.GEItemSet;
import core.game.ge.OfferState;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.audio.Audio;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import core.net.packet.PacketRepository;
import core.net.packet.context.ContainerContext;
import core.net.packet.out.ContainerPacket;
import core.plugin.Initializable;
import core.plugin.Plugin;
import kotlin.Unit;
import rs09.game.ge.GrandExchange;
import rs09.game.ge.GrandExchangeOffer;
import rs09.game.ge.GrandExchangeRecords;
import rs09.game.interaction.inter.ge.StockMarket;
import rs09.game.interaction.npc.BogrogPouchSwapper;
import rs09.game.world.GameWorld;

/**
 * Handles the Grand Exchange interface options.
 * @author Emperor
 * @version 1.0
 */
@Initializable
public class GrandExchangeInterface extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		//ComponentDefinition.put(105, this); // Main interface
		//ComponentDefinition.put(107, this); // Selling tab
		ComponentDefinition.put(109, this); // Collection interface
		ComponentDefinition.put(389, this); // Search interface
		ComponentDefinition.put(644, this); // Item sets inventory interface
		ComponentDefinition.put(645, this); // Item sets interface
		ComponentDefinition.put(642, this); // Guide Prices interface.
		return this;
	}

	@Override
	public boolean handle(final Player player, final Component component, final int opcode, final int button, final int slot, final int itemId) {
		GameWorld.getPulser().submit(new Pulse(1, player) {
			@Override
			public boolean pulse() {
				switch (component.getId()) {
					case 644:
					case 645:
						handleItemSet(player, component, opcode, button, slot, itemId);
						return true;
					case 389:
						handleSearchInterface(player, opcode, button, slot, itemId);
						return true;
					case 109:
						handleCollectionBox(player, opcode, button, slot, itemId);
						return true;
					case 642:
						handleGuidePrice(player, opcode, button, slot, itemId);
						return true;
				}
				return true;
			}
		});
		return true;
	}

	/**
	 * Handles the search interface options.
	 * @param player The player.
	 * @param opcode The packet opcode.
	 * @param button The button id.
	 * @param slot The slot.
	 * @param itemId The item id.
	 * @return {@code true} if the option got handled.
	 */
	public boolean handleSearchInterface(final Player player, int opcode, int button, int slot, int itemId) {
		switch (button) {
			case 10:
				player.getInterfaceManager().closeChatbox();
				return true;
		}
		return false;
	}

	/**
	 * Handles the selling tab interface options.
	 * @param player The player.
	 * @param opcode The packet opcode.
	 * @param button The button id.
	 * @param slot The slot.
	 * @param itemId The item id.
	 * @return {@code true} if the option got handled.
	 */
	public boolean handleCollectionBox(final Player player, int opcode, int button, int slot, int itemId) {
		int index = -1;
		switch (button) {
			case 18:
			case 23:
			case 28:
				index = (button - 18) >> 2;
				break;
			case 36:
			case 44:
			case 52:
				index = 3 + ((button - 36) >> 3);
				break;
		}
		GrandExchangeOffer offer;
		if (index > -1 && (offer = player.getExchangeRecords().getOffer(player.getExchangeRecords().getOfferRecords()[index])) != null) {
			StockMarket.withdraw(player, offer, slot >> 1);
		}
		return true;
	}

	/**
	 * Handles the item set.
	 * @param player The player.
	 * @param opcode The opcode.
	 * @param button The button.
	 * @param slot The slot.
	 * @param itemId The item id.
	 */
	private void handleItemSet(Player player, Component component, int opcode, int button, int slot, int itemId) {
		if (button != 16 && button != 0) {
			return;
		}
		boolean inventory = component.getId() == 644;
		if (slot < 0 || slot >= (inventory ? 28 : GEItemSet.values().length)) {
			return;
		}
		GEItemSet set = GEItemSet.values()[slot];
		Item item = inventory ? player.getInventory().get(slot) : new Item(set.getItemId());
		if (item == null) {
			return;
		}
		if (opcode != 127 && inventory && ((set = GEItemSet.forId(item.getId())) == null) && !BogrogPouchSwapper.handle(player,opcode,slot,itemId)) {
			player.getPacketDispatch().sendMessage("This isn't a set item.");
			return;
		}
		switch (opcode) {
			case 124:
				player.getPacketDispatch().sendMessage(item.getDefinition().getExamine());
				break;
			case 196:
				if (inventory) {
					if (player.getInventory().freeSlots() < set.getComponents().length - 1) {
						player.getPacketDispatch().sendMessage("You don't have enough inventory space for the component parts.");
						return;
					}
					if (!player.getInventory().remove(item, false)) {
						return;
					}
					for (int id : set.getComponents()) {
						player.getInventory().add(new Item(id, 1));
					}
					player.getInventory().refresh();
					player.getPacketDispatch().sendMessage("You successfully traded your set for its component items!");
				} else {
					if (!player.getInventory().containItems(set.getComponents())) {
						player.getPacketDispatch().sendMessage("You don't have the parts that make up this set.");
						break;
					}
					for (int id : set.getComponents()) {
						player.getInventory().remove(new Item(id, 1), false);
					}
					player.getInventory().add(item);
					player.getInventory().refresh();
					player.getPacketDispatch().sendMessage("You successfully traded your item components for a set!");
				}
				player.getAudioManager().send(new Audio(4044, 1, 1));
				PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, -2, player.getAttribute("container-key", 93), player.getInventory(), false));
				break;
			case 155:
				player.getPacketDispatch().sendMessage((String) CS2Mapping.forId(1089).getMap().get(set.getItemId()));
				break;
		}
	}

	/***
	 * Method used to handle the guide price opcode.
	 * @param player the player.
	 * @param opcode the opcode.
	 * @param buttonId the buttonId.
	 * @param slot the slot.
	 * @param itemId the itemId.
	 */
	private void handleGuidePrice(final Player player, final int opcode, final int buttonId, final int slot, final int itemId) {
		switch (opcode) {
			case 155:
				GEGuidePrice.GuideType type = player.getAttribute("guide-price", null);
				if (type == null) {
					return;
				}
				int subtract = 0;
				if (buttonId >= 15 && buttonId <= 23) {
					subtract = 15;
				}
				if (buttonId >= 43 && buttonId <= 57) {
					subtract = 43;
				}
				if (buttonId >= 89 && buttonId <= 103) {
					subtract = 89;
				}
				if (buttonId >= 135 && buttonId <= 144) {
					subtract = 135;
				}
				if (buttonId >= 167 && buttonId <= 182) {
					subtract = 167;
				}
				player.getPacketDispatch().sendMessage(ItemDefinition.forId(type.getItems()[buttonId - subtract].getItem()).getExamine());
				break;
		}
	}
}
