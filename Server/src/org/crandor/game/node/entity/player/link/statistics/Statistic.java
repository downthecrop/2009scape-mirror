package org.crandor.game.node.entity.player.link.statistics;

import org.crandor.game.node.entity.player.Player;

/**
 * A singular statistic.
 * @author jamix77
 *
 */
public class Statistic {
	
	/**
     * The player instance for this manager.
     */
    private final Player player;
    
    /**
     * The amount of the statistic.
     */
    private int statisticalAmount;

    /**
     * 
     * Constructs a new @{Code Statistic} object.
     * @param player
     */
	public Statistic(Player player, PlayerStatisticsManager sm) {
		this(player,0,sm);
	}
	
	/**
	 * 
	 * Constructs a new @{Code Statistic} object.
	 * @param player
	 * @param amount
	 */
	public Statistic(Player player,int amount, PlayerStatisticsManager sm) {
		this.player = player;
		this.statisticalAmount = amount;
		sm.addStatistic(this);
	}
	
	/**
	 * Increase only by one.
	 */
	public void incrementAmount() {
		increaseAmount(1);
	}
	
	/**
	 * Decrease only by one.
	 */
	public void decrementAmount() {
		decreaseAmount(1);
	}
	
	/**
	 * Increase by a certain amount.
	 * @param amount
	 */
	public void increaseAmount(int amount) {
		statisticalAmount += amount;
	}
	
	/**
	 * Decrease by a certain amount.
	 * @param amount
	 */
	public void decreaseAmount(int amount) {
		statisticalAmount -= amount;
	}

	/**
	 * @return the amount
	 */
	public int getStatisticalAmount() {
		return statisticalAmount;
	}

	public void setStatisticalAmount(int statisticalAmount) {
		this.statisticalAmount = statisticalAmount;
	}


}
