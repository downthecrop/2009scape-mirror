package core.game.node.entity.player.link.request.trade;

import core.game.component.Component;
import core.game.container.Container;
import core.game.container.ContainerType;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.request.RequestModule;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.game.system.monitor.PlayerMonitor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the module use to handle a request between trading of two players.
 * @author Vexia
 * 
 */
public final class TradeModule implements RequestModule {

	/**
	 * Represents the overlay interface component.
	 */
	public static final Component OVERLAY_INTERFACE = new Component(336);

	/**
	 * Represents the main interface component.
	 */
	public static final Component MAIN_INTERFACE = new Component(335).setCloseEvent(new TradeCloseEvent());

	/**
	 * Represents the accept interface component.
	 */
	public static final Component ACCEPT_INTERFACE = new Component(334).setCloseEvent(new TradeCloseEvent());

	/**
	 * The inventory params for the runscript.
	 */
	public static final Object[] INVENTORY_PARAMS = new Object[] { "", "", "", "Lend", "Offer-X", "Offer-All", "Offer-10", "Offer-5", "Offer", -1, 0, 7, 4, 93, (336 << 16) };

	/**
	 * The trade params for the run script.
	 */
	public static final Object[] TRADE_PARAMS = new Object[] { "", "", "", "", "Remove-X", "Remove-All", "Remove-10", "Remove-5", "Remove", -1, 0, 7, 4, 90, (335 << 16) | 30 };

	/**
	 * The partener params for the run script.
	 */
	public static final Object[] PARTENER_PARAMS = new Object[] { "", "", "", "", "", "", "", "", "", -1, 0, 7, 4, 91, (335 << 16) | 32 };

	/**
	 * Represents the childs that should be hidden.
	 */
	public static final int[] HIDDEN_CHILDS = new int[] { 42, 43, 44, 42, 44, 40, 41 };

	/**
	 * If the container has been retained.
	 */
	private boolean retained;

	/**
	 * Represents the player instance.
	 */
	private Player player;

	/**
	 * Represents the target instance.
	 */
	private Player target;

	/**
	 * Represents the container of this session.
	 */
	private TradeContainer container;

	/**
	 * Represents if this session has accepted.
	 */
	private boolean accepted;

	/**
	 * Represents if the trade is modified.
	 */
	private boolean modified = false;

	/**
	 * Represents the stage of the trade(0=started, 1=second accept)
	 */
	private int stage;

	/**
	 * Constructs a new {@code TradeModule} {@code Object}.
	 * @param player the player.
	 * @param target the target.
	 */
	public TradeModule(final Player player, final Player target) {
		this.player = player;
		this.target = target;
		this.container = new TradeContainer(player);
	}

	/**
	 * Constructs a new {@code TradeModule} {@code Object}.
	 */
	public TradeModule() {
		/**
		 * empty.
		 */
	}

	@Override
	public void open(Player player, Player target) {
		extend(player, target);
		if (getExtension(target) == null || getExtension(player) == null) {
			return;
		}
		getExtension(player).openInterface(getInterface(stage)).display(stage);
		if (getExtension(target) == null || getExtension(player) == null) {
			return;
		}
		getExtension(target).openInterface(getInterface(stage)).display(stage);
		player.getDialogueInterpreter().close();
		target.getDialogueInterpreter().close();
	}

	/**
	 * Method used to update the trade interfaces.
	 */
	public void update() {
		display(stage);
		getExtension(target).display(stage);
	}

	/**
	 * Method used to extend the module for each participant.
	 * @param player the player.
	 * @param target the target.
	 */
	public static void extend(final Player player, final Player target) {
		player.addExtension(TradeModule.class, new TradeModule(player, target));
		target.addExtension(TradeModule.class, new TradeModule(target, player));
	}

	/**
	 * Method used to get the extension from the player.
	 * @param player the player.
	 * @return the module instance.
	 */
	public static TradeModule getExtension(final Player player) {
		return player.getExtension(TradeModule.class);
	}

	/**
	 * Method used to open an interface at a stage.
	 * @param component the component.
	 * @return the module instance for chaining.
	 */
	private TradeModule openInterface(final Component component) {
		return component == MAIN_INTERFACE ? openMain() : openSecond();
	}

	/**
	 * Gets the accepting message to display.
	 * @return the message.
	 */
	private String getAcceptMessage() {
		boolean otherAccept = TradeModule.getExtension(target).isAccepted();
		return !otherAccept && !accepted ? "" : otherAccept ? "Other player has accepted." : "Waiting for other player...";
	}

	/**
	 * Method used to display the interface display depending on stage.
	 * @param stage the stage.
	 * @return module the module instance for chaining.
	 */
	private TradeModule display(int stage) {
		switch (stage) {
		case 0:
			for (int i : HIDDEN_CHILDS) {
				player.getPacketDispatch().sendString("", MAIN_INTERFACE.getId(), i);
			}
			player.getPacketDispatch().sendString("Trading With: " + target.getUsername(), 335, 15);
			player.getPacketDispatch().sendString(target.getUsername() + " has " + (target.getInventory().freeSlots() == 0 ? "no" : (target.getInventory().freeSlots())) + " free inventory slots.", 335, 21);
			player.getPacketDispatch().sendString(getAcceptMessage(), 335, 36);
			break;
		case 1:
			player.getPacketDispatch().sendString("<col=00FFFF>Trading with:<br>" + "<col=00FFFF>" + target.getUsername().substring(0, 1).toUpperCase() + target.getUsername().substring(1), 334, 2);
			final String acceptMessage = getAcceptMessage();
			if (acceptMessage != "") {
				player.getPacketDispatch().sendString(acceptMessage, 334, 33);
			}
			player.getInterfaceManager().restoreTabs();
			break;
		}
		displayModified(stage);
		container.update(true);
		return this;
	}

	/**
	 * Gets the interface for the current stage.
	 * @param stage the stage.
	 * @return the component.
	 */
	private Component getInterface(int stage) {
		return stage == 0 ? MAIN_INTERFACE : ACCEPT_INTERFACE;
	}

	/**
	 * Method used to display if the trade is modified.
	 * @param stage the sytage.
	 */
	private void displayModified(int stage) {
		final boolean otherModified = TradeModule.getExtension(target).isModified();
		if (stage == 1) {
			player.getPacketDispatch().sendInterfaceConfig(334, 45, !modified);
			player.getPacketDispatch().sendInterfaceConfig(334, 46, !otherModified);
		} else {
			if (otherModified) {
				player.getConfigManager().set(1043, 1);
			}
			if (modified) {
				player.getConfigManager().set(1042, 1);
			}
		}
	}

	/**
	 * Method used to decline this offer.
	 */
	public void decline() {
		player.getInterfaceManager().close();
		target.getPacketDispatch().sendMessage("<col=FF0000>Other player has declined trade!</col>");
	}

	/**
	 * Method used to check if the next stage can proceed.
	 */
	private void nextStage() {
		if (accepted && TradeModule.getExtension(target).isAccepted()) {
			if (stage == 0) {
				if (!hasSpace()) {
					return;
				}
				incrementStage();
				openInterface(getInterface(stage));
				TradeModule.getExtension(target).incrementStage();
				TradeModule.getExtension(target).openInterface(getInterface(stage));
				TradeModule.getExtension(target).setAccepted(false, false);
				setAccepted(false, false);
			} else {
				giveContainers(this);
				incrementStage();
				TradeModule.getExtension(target).incrementStage();
				TradeModule.getExtension(target).setAccepted(false, false);
				setAccepted(false, false);
				player.getInterfaceManager().close();
				return;
			}
		}
		update();
	}

	/**
	 * Method used to open the main interface.
	 * @return the module instance for chaining.
	 */
	private TradeModule openMain() {
		player.getInterfaceManager().closeDefaultTabs();
		player.getInterfaceManager().open(MAIN_INTERFACE);
		player.getInterfaceManager().openSingleTab(OVERLAY_INTERFACE);
		player.getInventory().refresh();
		player.getPacketDispatch().sendAccessMask(1278, 30, 335, 0, 27);
		player.getPacketDispatch().sendAccessMask(1026, 32, 335, 0, 27);
		player.getPacketDispatch().sendAccessMask(1278, 0, 336, 0, 27);
		player.getPacketDispatch().sendAccessMask(2360446, 0, 335, 0, 27);
		player.getPacketDispatch().sendRunScript(150, "IviiiIsssssssss", INVENTORY_PARAMS);
		player.getPacketDispatch().sendRunScript(150, "IviiiIsssssssss", TRADE_PARAMS);
		player.getPacketDispatch().sendRunScript(695, "IviiiIsssssssss", PARTENER_PARAMS);
		return this;
	}

	/**
	 * Method used to open the second interface.
	 * @return the module instance for chaining.
	 */
	private TradeModule openSecond() {
		player.getInterfaceManager().open(ACCEPT_INTERFACE);
		player.getInterfaceManager().closeSingleTab();
		displayOffer(container, 37);
		displayOffer(TradeModule.getExtension(target).getContainer(), 41);
		return this;
	}

	/**
	 * Method used to set the value of accepted.
	 * @param accept the accept.
	 */
	public void setAccepted(final boolean accept, boolean update) {
		this.accepted = accept;
		if (update) {
			nextStage();
		}
	}

	/**
	 * Method used to alert the player the trade has been modified.
	 * @param slot the slot.
	 */
	public void alert(final int slot) {
		player.getPacketDispatch().sendRunScript(143, "Iiii", new Object[] { slot, 7, 4, 21954593 });
		target.getPacketDispatch().sendRunScript(143, "Iiii", new Object[] { slot, 7, 4, 21954594 });
	}

	/**
	 * Method used to display an offer.
	 * @param container the container.
	 */
	private void displayOffer(final Container container, int child) {
		boolean split = container.itemCount() > 14;
		if (split) {
			player.getPacketDispatch().sendInterfaceConfig(334, child + 1, false);
			player.getPacketDispatch().sendInterfaceConfig(334, child + 2, false);
			String[] messages = new String[] { getDisplayMessage(splitList(container.toArray(), 0, 14)), getDisplayMessage(splitList(container.toArray(), 14, container.toArray().length)) };
			player.getPacketDispatch().sendString(messages[0], 334, child + 1);
			player.getPacketDispatch().sendString(messages[1], 334, child + 2);
		} else {
			player.getPacketDispatch().sendInterfaceConfig(334, child, false);
			player.getPacketDispatch().sendString(getDisplayMessage(container.toArray()), 334, child);
		}
	}

	/**
	 * Method used to get the display message.
	 * @param items the items.
	 * @return the message.
	 */
	private String getDisplayMessage(Item[] items) {
		String message = "Absolutely nothing!";
		if (items.length > 0) {
			message = "";
			for (int i = 0; i < items.length; i++) {
				if (items[i] == null) {
					continue;
				}
				message = message + "<col=FF9040>" + items[i].getName();
				if (items[i].getAmount() > 1) {
					message = message + "<col=FFFFFF> x ";
					message = message + "<col=FFFF00>" + getFormattedNumber(items[i].getAmount()) + "<br>";
				} else {
					message = message + "<br>";
				}
			}
		}
		return message;
	}

	/**
	 * Method used to check if the players & targets have enough space.
	 * @return {@code True} if so.
	 */
	private boolean hasSpace() {
		boolean hasSpace = true;
		if (!player.getInventory().hasSpaceFor(TradeModule.getExtension(target).getContainer())) {
			target.getPacketDispatch().sendMessage("Other player doesn't have enough space in their inventory for this trade.");
			player.getPacketDispatch().sendMessage("You don't have enough inventory space for this.");
			hasSpace = false;
		} else if (!target.getInventory().hasSpaceFor(container)) {
			player.getPacketDispatch().sendMessage("Other player doesn't have enough space in their inventory for this trade.");
			target.getPacketDispatch().sendMessage("You don't have enough inventory space for this.");
			hasSpace = false;
		}
		if (!hasSpace) {
			setAccepted(false, true);
			TradeModule.getExtension(target).setAccepted(false, true);
		}
		return hasSpace;
	}

	/**
	 * Gets the formatted number.
	 * @param amount the amount.
	 * @return the formatted number.
	 */
	private String getFormattedNumber(int amount) {
		return new DecimalFormat("#,###,##0").format(amount).toString();
	}

	/**
	 * Method used to give the containers to each participants.
	 * @param module the module.
	 */
	private void giveContainers(final TradeModule module) {
		final Container pContainer = module.getContainer();
		final Container oContainer = TradeModule.getExtension(module.getTarget()).getContainer();
		addContainer(module.getPlayer(), module.getTarget(), oContainer);
		addContainer(module.getTarget(), module.getPlayer(), pContainer);
		module.getTarget().getPacketDispatch().sendMessage("Accepted trade.");
		module.getPlayer().getPacketDispatch().sendMessage("Accepted trade.");
	}

	/**
	 * Method used to add a container for a player.
	 * @param player the player.
	 * @param target the target.
	 * @param container the container.
	 */
	private void addContainer(final Player player, Player target, final Container container) {
		Container c = new Container(container.itemCount(), ContainerType.ALWAYS_STACK);
		c.addAll(container);
		String log = "traded => " + target.getName() + " received => {";
		for (Item i : container.toArray()) {
			if (i == null) {
				continue;
			}
			if (i.getAmount() > player.getInventory().getMaximumAdd(i)) {
				i.setAmount(player.getInventory().getMaximumAdd(i));
			}
			if (!player.getInventory().add(i)) {
				GroundItemManager.create(i, player);
			}
		}
		for (Item i : c.toArray()) {
			if (i == null) {
				continue;
			}
			log += i.getName() + " x " + i.getAmount() + ",";
		}
		if (log.charAt(log.length() - 1) == ',') {
			log = log.substring(0, log.length() - 1);
		}
		log += "}";
		player.getMonitor().log(log, PlayerMonitor.TRADE_LOG);
	}

	/**
	 * Gets the split list as an array.
	 * @param items the items.
	 * @param min the min.
	 * @param max the max.
	 * @return the split item array.
	 */
	private Item[] splitList(Item[] items, int min, int max) {
		List<Item> list = new ArrayList<>();
		for (int i = min; i < max; i++) {
			if (items[i] == null) {
				continue;
			}
			list.add(items[i]);
		}
		Item[] array = new Item[list.size()];
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) == null) {
				continue;
			}
			array[i] = list.get(i);
		}
		return array;
	}

	/**
	 * Method used to increment the stage.
	 */
	public void incrementStage() {
		stage++;
	}

	/**
	 * Gets the stage.
	 * @return the stage.
	 */
	public int getStage() {
		return stage;
	}

	/**
	 * Gets the accepted.
	 * @return The accepted.
	 */
	public boolean isAccepted() {
		return accepted;
	}

	/**
	 * Gets the trade container.
	 * @return the container.
	 */
	public TradeContainer getContainer() {
		return container;
	}

	/**
	 * Gets the player.
	 * @return The player.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the target.
	 * @return The target.
	 */
	public Player getTarget() {
		return target;
	}

	/**
	 * Gets the modified.
	 * @return The modified.
	 */
	public boolean isModified() {
		return modified;
	}

	/**
	 * Sets the modified.
	 * @param modified The modified to set.
	 */
	public void setModified(boolean modified) {
		this.modified = modified;
	}

	/**
	 * Gets the retained.
	 * @return The retained.
	 */
	public boolean isRetained() {
		return retained;
	}

	/**
	 * Sets the retained.
	 * @param retained The retained to set.
	 */
	public void setRetained(boolean retained) {
		this.retained = retained;
	}

}