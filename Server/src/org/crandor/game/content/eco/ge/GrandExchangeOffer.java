package org.crandor.game.content.eco.ge;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.net.packet.PacketRepository;
import org.crandor.net.packet.context.ContainerContext;
import org.crandor.net.packet.out.ContainerPacket;

import java.util.Arrays;

/**
 * Represents a Grand Exchange offer.
 * @author Emperor
 */
public final class GrandExchangeOffer {

	/**
	 * The item id.
	 */
	private final int itemId;

	/**
	 * The amount.
	 */
	private int amount;

	/**
	 * The completed amount.
	 */
	private int completedAmount;

	/**
	 * The offered value per item.
	 */
	private int offeredValue;

	/**
	 * The index of this offer.
	 */
	private int index;

	/**
	 * If the player is selling.
	 */
	private boolean sell;

	/**
	 * The current state of this offer.
	 */
	private OfferState state = OfferState.PENDING;

	/**
	 * The unique id of this offer.
	 */
	private long uid;

	/**
	 * The time stamp of when this offer was entered.
	 */
	private long timeStamp;

	/**
	 * The items to withdraw from this offer.
	 */
	private Item[] withdraw = new Item[2];

	/**
	 * The total amount of coins that have been exchanged.
	 */
	private int totalCoinExchange;

	/**
	 * The player.
	 */
	private Player player;

	/**
	 * The player UID.
	 */
	private int playerUID;

	/**
	 * If the offer is limited due to buying limitation.
	 */
	private boolean limitation;

	/**
	 * The grand exchange entry.
	 */
	private GrandExchangeEntry entry;

	/**
	 * Constructs a new {@code GrandExchangeOffer} {@code Object}.
	 * @param itemId The item id.
	 * @param sell If the offer is a selling offer.
	 */
	public GrandExchangeOffer(int itemId, boolean sell) {
		this.itemId = itemId;
		this.sell = sell;
		this.entry = GrandExchangeDatabase.getDatabase().get(itemId);
	}

	/**
	 * Checks if this offer is still active for dispatching.
	 * @return {@code True} if so.
	 */
	public boolean isActive() {
		return state != OfferState.ABORTED && state != OfferState.PENDING && state != OfferState.COMPLETED && state != OfferState.REMOVED;
	}

	/**
	 * Adds a new item to withdraw.
	 * @param itemId The item id.
	 * @param amount The amount to add.
	 */
	public void addWithdraw(int itemId, int amount) {
		addWithdraw(itemId, amount, false);
	}

	/**
	 * Adds a new item to withdraw.
	 * @param itemId The item id.
	 * @param amount The amount to add.
	 * @param abort If the item is added due to abort.
	 */
	public void addWithdraw(int itemId, int amount, boolean abort) {
		if (!abort) {
			if (sell) {
				if (itemId == 995) {
					totalCoinExchange += amount;
				}
			} else {
				if (itemId == 995) {
					totalCoinExchange -= amount;
				} else {
					totalCoinExchange += offeredValue * amount;
				}
			}
		}
		for (int i = 0; i < withdraw.length; i++) {
			if (withdraw[i] == null) {
				withdraw[i] = new Item(itemId, amount);
				break;
			}
			if (withdraw[i].getId() == itemId) {
				withdraw[i].setAmount(withdraw[i].getAmount() + amount);
				break;
			}
		}
		if (player != null) {
			sendItems();
		}
	}

	/**
	 * Sends the items.
	 */
	public void sendItems() {
		if (player == null) {
			return;
		}
		PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, -1757, 523 + index, withdraw, false));
	}

	/**
	 * Sets the default values for an offer.
	 */
	public void setDefault() {
		if (entry == null) {
			return;
		}
		this.amount = 1;
		this.offeredValue = entry.getValue();
	}

	/**
	 * Sends a notification to the player, if he is online.
	 * @param message The notification message.
	 */
	public void notify(String message) {
		if (player == null || !player.isActive()) {
			return;
		}
		player.getPacketDispatch().sendMessage(message);
		player.getGrandExchange().update(this);
	}

	/**
	 * Checks if the offer is currently being limited by the 4-hours buying
	 * limitation.
	 * @return {@code True} if so.
	 */
	public boolean isLimited() {
		return !sell && BuyingLimitation.isLimited(itemId, playerUID);
	}

	/**
	 * Initializes the offer.
	 */
	public void init() {
		this.timeStamp = entry.getLastUpdate();
	}

	/**
	 * Gets the database entry of this offer.
	 * @return The grand exchange entry.
	 */
	public GrandExchangeEntry getEntry() {
		return entry;
	}

	/**
	 * Gets the total amount of money entered.
	 * @return The total value.
	 */
	public int getTotalValue() {
		return offeredValue * amount;
	}

	/**
	 * Gets the itemId.
	 * @return The itemId.
	 */
	public int getItemId() {
		return itemId;
	}

	/**
	 * Gets the timeStamp.
	 * @return The timeStamp.
	 */
	public long getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Gets the amount of this item left to buy.
	 * @return The amount.
	 */
	public int getAmountLeft() {
		return getAmountLeft(false);
	}

	/**
	 * Gets the amount of this item left to buy.
	 * @param limit If the buying limit should be taken into consideration.
	 * @return The amount.
	 */
	public int getAmountLeft(boolean limit) {
		int left = amount - completedAmount;
		if (limit && !sell && left > 0) {
			int maximum = BuyingLimitation.getMaximumBuy(itemId, playerUID);
			if (left >= maximum) {
				left = maximum;
				limitation = true;
			}
		}
		return left;
	}

	/**
	 * Gets the amount.
	 * @return The amount.
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Sets the amount.
	 * @param amount The amount to set.
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * Gets the offeredValue.
	 * @return The offeredValue.
	 */
	public int getOfferedValue() {
		return offeredValue;
	}

	/**
	 * Sets the offeredValue.
	 * @param offeredValue The offeredValue to set.
	 */
	public void setOfferedValue(int offeredValue) {
		this.offeredValue = offeredValue;
	}

	/**
	 * Gets the sell.
	 * @return The sell.
	 */
	public boolean isSell() {
		return sell;
	}

	/**
	 * Sets the sell.
	 * @param sell The sell to set.
	 */
	public void setSell(boolean sell) {
		this.sell = sell;
	}

	/**
	 * Gets the state.
	 * @return The state.
	 */
	public OfferState getState() {
		return state;
	}

	/**
	 * Sets the state.
	 * @param state The state to set.
	 */
	public void setState(OfferState state) {
		this.state = state;
	}

	/**
	 * Gets the uid.
	 * @return The uid.
	 */
	public long getUid() {
		return uid;
	}

	/**
	 * Sets the uid.
	 * @param uid The uid to set.
	 */
	public void setUid(long uid) {
		this.uid = uid;
	}

	/**
	 * Gets the withdraw.
	 * @return The withdraw.
	 */
	public Item[] getWithdraw() {
		return withdraw;
	}

	/**
	 * Sets the withdraw.
	 * @param withdraw The withdraw to set.
	 */
	public void setWithdraw(Item[] withdraw) {
		this.withdraw = withdraw;
	}

	/**
	 * Gets the completedAmount.
	 * @return The completedAmount.
	 */
	public int getCompletedAmount() {
		return completedAmount;
	}

	/**
	 * Sets the completedAmount.
	 * @param completedAmount The completedAmount to set.
	 */
	public void setCompletedAmount(int completedAmount) {
		this.completedAmount = completedAmount;
	}

	/**
	 * Gets the index.
	 * @return The index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Sets the index.
	 * @param index The index to set.
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Gets the player.
	 * @return The player.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Sets the player.
	 * @param player The player to set.
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Sets the time stamp.
	 * @param timeStamp The time stamp.
	 */
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * Gets the totalCoinExchange.
	 * @return The totalCoinExchange.
	 */
	public int getTotalCoinExchange() {
		return totalCoinExchange;
	}

	/**
	 * Sets the totalCoinExchange.
	 * @param totalCoinExchange The totalCoinExchange to set.
	 */
	public void setTotalCoinExchange(int totalCoinExchange) {
		this.totalCoinExchange = totalCoinExchange;
	}

	/**
	 * Gets the playerUID.
	 * @return The playerUID.
	 */
	public int getPlayerUID() {
		return playerUID;
	}

	/**
	 * Sets the playerUID.
	 * @param playerUID The playerUID to set.
	 */
	public void setPlayerUID(int playerUID) {
		this.playerUID = playerUID;
	}

	/**
	 * Gets the limitation.
	 * @return The limitation.
	 */
	public boolean isLimitation() {
		return limitation;
	}

	/**
	 * Sets the limitation.
	 * @param limitation The limitation to set.
	 */
	public void setLimitation(boolean limitation) {
		this.limitation = limitation;
	}

	@Override
	public String toString() {
		return "[name=" + ItemDefinition.forId(itemId).getName() + ", itemId=" + itemId + ", amount=" + amount + ", completedAmount=" + completedAmount + ", offeredValue=" + offeredValue + ", index=" + index + ", sell=" + sell + ", state=" + state + ", withdraw=" + Arrays.toString(withdraw) + ", totalCoinExchange=" + totalCoinExchange + ", playerUID=" + playerUID + "]";
	}
}