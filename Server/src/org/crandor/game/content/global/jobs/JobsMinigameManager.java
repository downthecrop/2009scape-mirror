package org.crandor.game.content.global.jobs;

import java.nio.ByteBuffer;

import org.crandor.game.content.global.jobs.impl.GatheringJob;
import org.crandor.game.content.global.jobs.impl.SlayingJob;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.info.login.SavingModule;

/**
 * Handles the jobs of a player
 * @author jamix77
 */
public final class JobsMinigameManager implements SavingModule {

	/**
	 * The current job of the player.
	 */
	private Job job;
	
	/**
	 * The player.
	 */
	private final Player player;
	
	/**
	 * The amount of jobs the player has completed.
	 */
	private int jobsDone = 0;


	/**
	 * Constructs a new {@Code TreasureTrailManager} {@Code
	 * Object}
	 * @param player the player.
	 */
	public JobsMinigameManager(Player player) {
		this.player = player;
	}

	@Override
	public void save(ByteBuffer buffer) {
		
	}

	@Override
	public void parse(ByteBuffer buffer) {

	}
	
	/**
	 * Increments the jobsDone variable by 1.
	 */
	public void incrementJobs() {
		jobsDone++;
	}


	/**
	 * Gets the player.
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}
	
	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public int getJobsDone() {
		return jobsDone;
	}

	public void setJobsDone(int jobsDone) {
		this.jobsDone = jobsDone;
	}



}
