/**
 * 
 */
package org.crandor.game.content.global.jobs;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;

/**
 * Represents a gathering job, eg fishing woodcutting and such.
 * @author jamix77
 *
 */
public class GatheringJob extends Job {
	
	private int itemId;
	private int amount;

	/**
	 * Constructs a new @{Code GatheringJob} object.
	 */
	public GatheringJob(int employer, Player player,int amount, int itemId) {
		super(employer,player);
		this.itemId = itemId;
		this.setAmount(amount);
	}

	@Override
	public
	boolean reward() {
		if (getPlayer().getInventory().getAmount(itemId) >= getAmount()) {
			getPlayer().getInventory().remove(new Item(itemId,getAmount()));
			setAmount(0);
			return true;
		} else {
			setAmount(getAmount() - getPlayer().getInventory().getAmount(itemId));
			getPlayer().getInventory().remove(new Item(itemId,getPlayer().getInventory().getAmount(itemId)));
			getPlayer().sendMessage("You have " + getAmount() + " left to get.");
		}
		return false;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getItemId() {
		return itemId;
	}


}
