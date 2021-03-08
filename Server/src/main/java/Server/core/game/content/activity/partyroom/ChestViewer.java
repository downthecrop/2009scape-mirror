package core.game.content.activity.partyroom;

import core.game.component.CloseEvent;
import core.game.component.Component;
import core.game.container.Container;
import core.game.container.ContainerEvent;
import core.game.container.ContainerListener;
import core.game.container.ContainerType;
import core.game.container.SortType;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.net.packet.PacketRepository;
import core.net.packet.context.ContainerContext;
import core.net.packet.out.ContainerPacket;

/**
 * A chest viewer.
 * @author Vexia
 */
public final class ChestViewer {

	/**
	 * The objects for the chest queue interface.
	 */
	private static final Object[] BEING_DROPPED = new Object[] { "", "", "", "", "", "", "", "", "", -1, 0, 39, 6, 92, 647 << 16 | 27 };

	/**
	 * The objects for the dropping interface.
	 */
	private static final Object[] READY_TO_DROP = new Object[] { "", "", "", "", "", "", "", "", "", -1, 0, 39, 6, 91, 647 << 16 | 28 };

	/**
	 * The objects for the accept interface.
	 */
	private static final Object[] ACCEPT = new Object[] { "", "", "", "", "Withdraw-X", "Withdraw-All", "Withdraw-10", "Withdraw-5", "Withdraw-1", -1, 0, 4, 10, 90, 647 << 16 | 29 };

	/**
	 * The objects for the singletab.
	 */
	private final static Object[] INV_OPTIONS = new Object[] { "", "", "", "", "Deposit-X", "Deposit-All", "Deposit-10", "Deposit-5", "Deposit", -1, 0, 7, 4, 94, 648 << 16 };

	/**
	 * The player viewing the chest.
	 */
	private final Player player;

	/**
	 * The container.
	 */
	private final DepositContainer container;

	/**
	 * Constructs a new {@Code ChestViewer} {@Code Object}
	 * @param player the player.
	 */
	public ChestViewer(Player player) {
		this.player = player;
		this.container = new DepositContainer(player);
	}

	/**
	 * Views the chest.
	 */
	public ChestViewer view() {
		container.open();
		player.getInventory().refresh();
		player.addExtension(ChestViewer.class, this);
		player.getPacketDispatch().sendRunScript(150, "IviiiIsssssssss", ACCEPT);
		player.getPacketDispatch().sendRunScript(150, "IviiiIsssssssss", INV_OPTIONS);
		player.getInterfaceManager().openSingleTab(new Component(648));
		player.getInterfaceManager().open(new Component(647).setCloseEvent(new ChestCloseEvent()));
		player.getConfigManager().set(1135, 0);
		update(0, null);
		update(1, null);
		return this;
	}

	/**
	 * Updates the chest viewer.
	 * @param type the type.
	 * @param event the event.
	 */
	public void update(int type, ContainerEvent event) {
		if (event == null) {
			player.getPacketDispatch().sendAccessMask(1278, 27, 647, 0, 10);
			player.getPacketDispatch().sendAccessMask(1278, 28, 647, 0, 10);
			player.getPacketDispatch().sendAccessMask(1278, 29, 647, 0, 10);
			switch (type) {
			case 0:
				player.getPacketDispatch().sendRunScript(150, "IviiiIsssssssss", BEING_DROPPED);
				PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, -2, 92, PartyRoomPlugin.getChestQueue().toArray(), 10, false));
				break;
			case 1:
				player.getPacketDispatch().sendRunScript(150, "IviiiIsssssssss", READY_TO_DROP);
				PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, -2, 91, PartyRoomPlugin.getPartyChest().toArray(), 10, false));
				break;
			case 2:
				player.getPacketDispatch().sendRunScript(150, "IviiiIsssssssss", ACCEPT);
				PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, -2, 90, new Item[] { new Item(4151, 2), new Item(11694, 2), new Item(4012, 2000) }, 10, false));
				break;
			}
		}
	}

	/**
	 * Accepts the container.
	 */
	public void accept() {
		if (PartyRoomPlugin.getChestQueue().itemCount() + getContainer().itemCount() > 215) {
			player.sendMessage("The chest is full.");
			return;
		}
		PartyRoomPlugin.getChestQueue().addAll(getContainer());
		getContainer().clear();
		PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, -2, 90, new Item[] {}, 10, false));
		PartyRoomPlugin.update(0, null);
		PartyRoomPlugin.update(1, null);
	}

	/**
	 * Gets the container.
	 * @return the container
	 */
	public DepositContainer getContainer() {
		return container;
	}

	/**
	 * Gets the player.
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * The close event for the chest interface.
	 * @author Vexia
	 */
	public class ChestCloseEvent implements CloseEvent {

		/**
		 * If the container has given back the items.
		 */
		private boolean given = false;

		@Override
		public boolean close(Player player, Component c) {
			if (!given) {
				given = true;
				player.getInventory().add(container.toArray());
				container.clear();
				player.removeExtension(ChestViewer.class);
				player.getInterfaceManager().closeSingleTab();
				PartyRoomPlugin.getViewers().remove(player.getName());
			}
			return true;
		}

	}

	/**
	 * The container used when depositng items.
	 * @author Vexia
	 */
	public class DepositContainer extends Container {

		/**
		 * The player.
		 */
		private final Player player;

		/**
		 * The party deposit listener.
		 */
		private final PartyDepositListener listener;

		/**
		 * Constructs a new {@Code DepositContainer} {@Code
		 * Object}
		 * @param player the player.
		 */
		public DepositContainer(Player player) {
			super(8, ContainerType.DEFAULT, SortType.ID);
			super.getListeners().add(listener = new PartyDepositListener(player));
			this.player = player;
		}

		/**
		 * Opens the container.
		 */
		public DepositContainer open() {
			super.refresh();
			player.getInventory().getListeners().add(listener);
			player.getPacketDispatch().sendRunScript(150, "IviiiIsssssssss", ACCEPT);
			player.getPacketDispatch().sendRunScript(150, "IviiiIsssssssss", INV_OPTIONS);
			PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, -2, 90, new Item[] {}, 10, false));
			return this;
		}

		/**
		 * Adds an item to the container.
		 * @param slot The item slot.
		 * @param amount The amount.
		 */
		public void addItem(int slot, int amount) {
			if (slot < 0 || slot > player.getInventory().capacity() || amount < 1) {
				return;
			}
			Item item = player.getInventory().get(slot);
			if (item == null) {
				return;
			}
			int maximum = player.getInventory().getAmount(item);
			if (amount > maximum) {
				amount = maximum;
			}
			int maxCount = super.getMaximumAdd(item);
			if (amount > maxCount) {
				amount = maxCount;
				if (amount < 1) {
					player.getPacketDispatch().sendMessage("You must accept the current items before adding any more.");
					return;
				}
			}
			item = new Item(item.getId(), amount);
			if (!item.getDefinition().isTradeable()) {
				player.getPacketDispatch().sendMessage("You can't add that item to the chest.");
				return;
			}
			if (super.add(item) && player.getInventory().remove(item)) {
				listener.update(this, null);
				player.getInventory().update();
				PartyRoomPlugin.update(0, null);
				PartyRoomPlugin.update(1, null);
			}
		}

		/**
		 * Takes a item from the container and adds one to the inventory
		 * container.
		 * @param slot The slot.
		 * @param amount The amount.
		 */
		public void takeItem(int slot, int amount) {
			if (slot < 0 || slot > super.capacity() || amount <= 0) {
				return;
			}
			Item item = super.get(slot);
			if (item == null) {
				return;
			}
			int realAmt = item.getAmount();
			if (!item.getDefinition().isStackable()) {
				for (Item i : container.toArray()) {
					if (i == null) {
						continue;
					}
					if (i != item && i.getId() == item.getId()) {
						realAmt++;
					}
				}
			}
			if (amount > realAmt) {
				amount = realAmt;
			}
			item = new Item(item.getId(), amount);
			Item add = item;
			int maxCount = player.getInventory().getMaximumAdd(add);
			if (amount > maxCount) {
				item.setAmount(maxCount);
				add.setAmount(maxCount);
				if (maxCount < 1) {
					player.getPacketDispatch().sendMessage("Not enough space in your inventory.");
					return;
				}
			}
			if (super.remove(item)) {
				player.getInventory().add(add);
				PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, -2, 94, player.getInventory(), false));
			}
			PartyRoomPlugin.update(0, null);
			PartyRoomPlugin.update(1, null);
		}

		/**
		 * Listeners to the part deposit container.
		 * @author Vexia
		 */
		private class PartyDepositListener implements ContainerListener {

			/**
			 * The player reference.
			 */
			private Player player;

			/**
			 * Constructs a new {@code PartyDepsositContainer.java}
			 * {@code Object}.
			 * @param player
			 */
			public PartyDepositListener(Player player) {
				this.player = player;
			}

			@Override
			public void update(Container c, ContainerEvent event) {
				if (c instanceof DepositContainer) {
					player.getPacketDispatch().sendRunScript(150, "IviiiIsssssssss", ACCEPT);
					player.getPacketDispatch().sendRunScript(150, "IviiiIsssssssss", INV_OPTIONS);
					PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, -2, 90, c.toArray(), 10, false));
					PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, -2, 94, player.getInventory(), false));
					player.getPacketDispatch().sendAccessMask(1278, 0, 648, 0, 28);
				}
			}

			@Override
			public void refresh(Container c) {
				if (c instanceof DepositContainer) {
					player.getPacketDispatch().sendAccessMask(1278, 0, 648, 0, 28);
					player.getPacketDispatch().sendRunScript(150, "IviiiIsssssssss", ACCEPT);
					player.getPacketDispatch().sendRunScript(150, "IviiiIsssssssss", INV_OPTIONS);
					PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, -2, 94, player.getInventory(), false));
				}

			}
		}
	}

}
