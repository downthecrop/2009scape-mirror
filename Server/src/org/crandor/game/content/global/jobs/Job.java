package org.crandor.game.content.global.jobs;

import org.crandor.game.node.entity.player.Player;

/**
 * Represents the job of a player
 * @author jamix77
 *
 */
public abstract class Job {
	
	private final int employer;
	private final Player player;

	/**
	 * Constructs a new @{Code Job} object.
	 * @param employer
	 */
	protected Job(int employer, Player player) {
		this.employer = employer;
		this.player = player;
	}
	
	/**
	 * The reward for the player after the player has completed their job.
	 */
	public abstract boolean reward();
	
	/**
	 * The employer of the job.
	 * @return
	 */
	public int getEmployer() {
		return employer;
	}

	public Player getPlayer() {
		return player;
	}
	
}
