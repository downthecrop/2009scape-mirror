package core.game.container.impl;

import core.api.IfaceSettingsBuilder;
import core.game.container.access.InterfaceContainer;
import kotlin.Unit;
import kotlin.ranges.IntRange;
import org.rs09.consts.Vars;
import core.ServerConstants;
import core.game.component.Component;
import core.game.container.*;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.IronmanMode;
import core.game.node.item.Item;
import core.game.system.config.ItemConfigParser;
import core.game.world.GameWorld;
import core.net.packet.PacketRepository;
import core.net.packet.context.ContainerContext;
import core.net.packet.out.ContainerPacket;

import java.nio.ByteBuffer;

import static core.api.ContentAPIKt.*;


/**
 * Represents the bank container.
 * @author Emperor
 */
public final class BankContainer extends Container {

	/**
	 * The bank container size.
	 */
	public static final int SIZE = ServerConstants.BANK_SIZE;
	
	/**
	 * The maximum amount of bank tabs
	 */
	public static final int TAB_SIZE = 11;
	
	/**
	 * The player reference.
	 */
	private Player player;

	/**
	 * The bank listener.
	 */
	private final BankListener listener;

	/**
	 * If the bank is open.
	 */
	private boolean open;
	
	/**
	 * The last x-amount entered.
	 */
	private int lastAmountX = 50;
	
	/**
	 * The current tab index.
	 */
	private int tabIndex = 10;
	
	/**
	 * The tab start indexes.
	 */
	private final int[] tabStartSlot = new int[TAB_SIZE];

	/**
	 * Construct a new {@code BankContainer} {@code Object}.
	 * @param player The player reference.
	 */
	public BankContainer(Player player) {
		super(SIZE, ContainerType.ALWAYS_STACK, SortType.HASH);
		super.register(listener = new BankListener(player));
		this.player = player;
	}

	/**
	 * Method used to open the deposit box.
	 */
	public void openDepositBox() {
		player.getInterfaceManager().open(new Component(11)).setCloseEvent((player, c) -> {
			player.getInterfaceManager().openDefaultTabs();
			return true;
		});
		player.getInterfaceManager().removeTabs(0, 1, 2, 3, 4, 5, 6);
		refreshDepositBoxInterface();
	}

	/**
	 * Invalidates the visual state of deposit box interface
	 * forcing the client to re-draw the items
	 */
	public void refreshDepositBoxInterface()
	{
		InterfaceContainer.generateItems(
			player,
			player.getInventory().toArray(),
			new String[] {
				"Examine",
				"Deposit-X",
				"Deposit-All",
				"Deposit-10",
				"Deposit-5",
				"Deposit-1"
			}, 11, 15, 5, 7
		);
	}

	/**
	 * Open the bank.
	 */
	public void open() {
		if (open) {
			return;
		}
		if (player.getIronmanManager().checkRestriction(IronmanMode.ULTIMATE)) {
			return;
		}
		if (!player.getBankPinManager().isUnlocked() && !GameWorld.getSettings().isDevMode()) {
			player.getBankPinManager().openType(1);
			return;
		}
		player.getInterfaceManager().openComponent(762).setCloseEvent((player, c) -> {
			BankContainer.this.close();
			return true;
		});
		player.getInterfaceManager().openSingleTab(new Component(763));
		super.refresh();
		player.getInventory().refresh();
		player.getInventory().getListeners().add(listener);
		setVarp(player, 1249, lastAmountX);
		int settings = new IfaceSettingsBuilder().enableOptions(new IntRange(0, 5)).enableExamine().enableSlotSwitch().build();
		player.getPacketDispatch().sendIfaceSettings(settings, 0, 763, 0, 27);
		open = true;
	}
	
	public void open(Player player) {
		if (open) {
			return;
		}
		if (player.getIronmanManager().checkRestriction(IronmanMode.ULTIMATE)) {
			return;
		}
		if (!player.getBankPinManager().isUnlocked() && !GameWorld.getSettings().isDevMode()) {
			player.getBankPinManager().openType(1);
			return;
		}
		player.getInterfaceManager().openComponent(762).setCloseEvent((player1, c) -> {
			BankContainer.this.close();
			return true;
		});
		refresh(listener);
		player.getInterfaceManager().openSingleTab(new Component(763));
		player.getInventory().getListeners().add(player.getBank().listener);
		player.getInventory().refresh();
                setVarp(player, 1249, lastAmountX);
		player.getPacketDispatch().sendIfaceSettings(1278, 73, 762, 0, SIZE);
		int settings = new IfaceSettingsBuilder().enableOptions(new IntRange(0,5)).enableExamine().enableSlotSwitch().build();
		player.getPacketDispatch().sendIfaceSettings(settings, 0, 763, 0, 27);
		player.getPacketDispatch().sendRunScript(1451, "");
		open = true;

	}

	/**
	 * Closes the bank.
	 */
	public void close() {
		open = false;
		player.getInventory().getListeners().remove(listener);
		player.getInterfaceManager().closeSingleTab();
		player.removeAttribute("search");
		player.getPacketDispatch().sendRunScript(571, "");
	}

	/**
	 * Adds an item to the bank container.
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

		if (!item.getDefinition().getConfiguration(ItemConfigParser.BANKABLE, true)) {
			player.sendMessage("A magical force prevents you from banking this item");
			return;
		}

		int maximum = player.getInventory().getAmount(item);
		if (amount > maximum) {
			amount = maximum;
		}

		item = new Item(item.getId(), amount, item.getCharge());
		boolean unnote = !item.getDefinition().isUnnoted();

		Item add = unnote ? new Item(item.getDefinition().getNoteId(), amount, item.getCharge()) : item;
		if (unnote && !add.getDefinition().isUnnoted()) {
			add = item;
		}

		int maxCount = super.getMaximumAdd(add);
		if (amount > maxCount) {
			add.setAmount(maxCount);
			item.setAmount(maxCount);
			if (maxCount < 1) {
				player.getPacketDispatch().sendMessage("There is not enough space left in your bank.");
				return;
			}
		}

		if (player.getInventory().remove(item, slot, false)) {
			int preferredSlot = -1;
			if (tabIndex != 0 && tabIndex != 10 && !super.contains(add.getId(), 1)) {
				preferredSlot = tabStartSlot[tabIndex] + getItemsInTab(tabIndex);
				insert(freeSlot(), preferredSlot, false);
				increaseTabStartSlots(tabIndex);
			}
			super.add(add, true, preferredSlot);
			player.getInventory().update();
		}
	}

	/**
	 * Takes a item from the bank container and adds one to the inventory
	 * container.
	 * @param slot The slot.
	 * @param amount The amount.
	 */
	public void takeItem(int slot, int amount) {
		if (slot < 0 || slot > super.capacity() || amount <= 0) {
			return;
		}
		Item item = get(slot);
		if (item == null) {
			return;
		}
		if (amount > item.getAmount()) {
			amount = item.getAmount(); // It always stacks in the bank.
		}
		item = new Item(item.getId(), amount, item.getCharge());
		int noteId = item.getDefinition().getNoteId();
		Item add = isNoteItems() && noteId > 0 ? new Item(noteId, amount, item.getCharge()) : item;
		int maxCount = player.getInventory().getMaximumAdd(add);
		if (amount > maxCount) {
			item.setAmount(maxCount);
			add.setAmount(maxCount);
			if (maxCount < 1) {
				player.getPacketDispatch().sendMessage("Not enough space in your inventory.");
				return;
			}
		}
		if (isNoteItems() && noteId < 0) {
			player.getPacketDispatch().sendMessage("This item can't be withdrawn as a note.");
			add = item;
		}
		if (super.remove(item, slot, false)) {
			player.getInventory().add(add, false);
		}
		if (get(slot) == null) {
			int tabId = getTabByItemSlot(slot);
			decreaseTabStartSlots(tabId);
			shift();
		} else update();
		player.getInventory().update();
	}

	/**
	 * Updates the last x-amount entered.
	 * @param amount The amount to set.
	 */
	public void updateLastAmountX(int amount) {
		this.lastAmountX = amount;
                setVarp(player, 1249, amount);
	}
	
	/**
	 * Gets the tab the item slot is in.
	 * @param itemSlot The item slot.
	 * @return The tab index.
	 */
	public int getTabByItemSlot(int itemSlot) {
		int tabId = 0;
		for (int i = 0; i < tabStartSlot.length; i++) {
			if (itemSlot >= tabStartSlot[i]) {
				tabId = i;
			}
		}
		return tabId;
	}
	
	/**
	 * Increases a tab's start slot.
	 * @param startId The start id.
	 */
	public void increaseTabStartSlots(int startId) {
		for (int i = startId + 1; i < tabStartSlot.length; i++) {
			tabStartSlot[i]++;
		}
	}

	/**
	 * Decreases a tab's start slot.
	 * @param startId The start id.
	 */
	public void decreaseTabStartSlots(int startId) {
		if (startId == 10) {
			return;
		}
		for (int i = startId + 1; i < tabStartSlot.length; i++) {
			tabStartSlot[i]--;
		}
		if (getItemsInTab(startId) == 0) {
			collapseTab(startId);
		}
	}
	
	/**
	 * Gets the array index for a tab.
	 * @param tabId The tab id.
	 * @return The array index.
	 */
	public static int getArrayIndex(int tabId) {
		if (tabId == 41 || tabId == 74) {
			return 10;
		}
		int base = 39;
		for (int i = 1; i < 10; i++) {
			if (tabId == base) {
				return i;
			}
			base -= 2;
		}
		return -1;
	}
	
	/**
	 * Sends the bank space values on the interface.
	 */
	public void sendBankSpace() {
		setVarc(player, 192, capacity() - freeSlots());
	}
	
	/**
	 * Collapses a tab.
	 * @param tabId The tab index.
	 */
	public void collapseTab(int tabId) {
		int size = getItemsInTab(tabId);
		Item[] tempTabItems = new Item[size];
		for (int i = 0; i < size; i++) {
			tempTabItems[i] = get(tabStartSlot[tabId] + i);
			replace(null, tabStartSlot[tabId] + i, false);
		}
		shift();
		for (int i = tabId; i < tabStartSlot.length - 1; i++) {
			tabStartSlot[i] = tabStartSlot[i + 1] - size;
		}
		tabStartSlot[10] = tabStartSlot[10] - size;
		for (int i = 0; i < size; i++) {
			int slot = freeSlot();
			replace(tempTabItems[i], slot, false);
		}
		refresh(); //We only refresh once.
	}
	
	/**
	 * Sets the tab configs.
	 */
	public void setTabConfigurations() {
		for (int i = 0; i < 8; i++) {
			setVarbit(player, 4885 + i, getItemsInTab(i + 1));
		}
	}

	/**
	 * Gets the amount of items in one tab.
	 * @param tabId The tab index.
	 * @return The amount of items in this tab.
	 */
	public int getItemsInTab(int tabId) {
		return tabStartSlot[tabId + 1] - tabStartSlot[tabId];
	}

	/**
	 * Checks if the item can be added.
	 * @param item the item.
	 * @return {@code True} if so.
	 */
	public boolean canAdd(Item item) {
		return item.getDefinition().getConfiguration(ItemConfigParser.BANKABLE, true);
	}

	/**
	 * Gets the last x-amount.
	 * @return The last x-amount.
	 */
	public int getLastAmountX() {
		return lastAmountX;
	}

	/**
	 * If items have to be noted.
	 * @return If items have to be noted {@code true}.
	 */
	public boolean isNoteItems() {
                return getVarbit(player, Vars.VARBIT_IFACE_BANK_NOTE_MODE) == 1;
	}

	/**
	 * Set if items have to be noted.
	 * @param noteItems If items have to be noted {@code true}.
	 */
	public void setNoteItems(boolean noteItems) {
                setVarbit(player, Vars.VARBIT_IFACE_BANK_NOTE_MODE, noteItems ? 1 : 0, true);
	}

	/**
	 * Gets the tabStartSlot value.
	 * @return The tabStartSlot.
	 */
	public int[] getTabStartSlot() {
		return tabStartSlot;
	}

	/**
	 * Gets the tabIndex value.
	 * @return The tabIndex.
	 */
	public int getTabIndex() {
		return tabIndex;
	}

	/**
	 * Sets the tabIndex value.
	 * @param tabIndex The tabIndex to set.
	 */
	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex == 0 ? 10 : tabIndex;
		setVarbit(player, 4893, tabIndex + 1);
		setAttribute(player, "bank:lasttab", tabIndex);
	}

	/**
	 * Sets the insert items value.
	 * @param insertItems The insert items value.
	 */
	public void setInsertItems(boolean insertItems) {
                setVarbit(player, Vars.VARBIT_IFACE_BANK_INSERT_MODE, insertItems ? 1 : 0, true);
	}
	
	/**
	 * Gets the insert items value.
	 * @return {@code True} if inserting items mode is enabled.
	 */
	public boolean isInsertItems() {
                return getVarbit(player, Vars.VARBIT_IFACE_BANK_INSERT_MODE) == 1;
	}
	
	/**
	 * Checks if the bank is opened.
	 * @return {@code True} if so.
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * Listens to the bank container.
	 * @author Emperor
	 */
	private static class BankListener implements ContainerListener {

		/**
		 * The player reference.
		 */
		private Player player;

		/**
		 * Construct a new {@code BankListener} {@code Object}.
		 * @param player The player reference.
		 */
		public BankListener(Player player) {
			this.player = player;
		}

		@Override
		public void update(Container c, ContainerEvent event) {
			if (c instanceof BankContainer) {
				PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 762, 64000, 95, event.getItems(), false, event.getSlots()));
			} else {
				PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 763, 64000, 93, event.getItems(), false, event.getSlots()));
			}
			player.getBank().setTabConfigurations();
			player.getBank().sendBankSpace();
		}

		@Override
		public void refresh(Container c) {
			if (c instanceof BankContainer) {
				PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 762, 64000, 95, c.toArray(), c.capacity(), false));
			} else {
				PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 763, 64000, 93, c.toArray(), 28, false));
			}
			player.getBank().setTabConfigurations();
			player.getBank().sendBankSpace();
		}
	}
}
