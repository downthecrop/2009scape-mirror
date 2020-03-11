package org.crandor.game.content.global.jobs.impl;

import org.crandor.game.content.global.jobs.JobType;
import org.crandor.game.content.skill.Skills;
import org.crandor.tools.RandomFunction;

/**
 * Represents the jobs that Wilfred can give.
 * @author jamix77
 *
 */
public enum WilfredJobs {
	LOG(22,28, 1, 1511,JobType.GATHERING, Skills.WOODCUTTING),
	OAK(22,28,15, 1521,JobType.GATHERING,Skills.WOODCUTTING),
	WILLOW(22,28,30, 1519,JobType.GATHERING,Skills.WOODCUTTING);
	
	private final int itemId;
	private final int upperBound;
	private final int lowerBound;
	private final int lvlReq;
	private final JobType jobType;
	private final int skillId;
	
	private WilfredJobs(int lowerBound,int upperBound, int lvlReq, int itemId, JobType jobType, int skillId) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.itemId = itemId;
		this.lvlReq = lvlReq;
		this.jobType = jobType;
		this.skillId = skillId;
	}

	public int getLvlReq() {
		return lvlReq;
	}

	public int getItemId() {
		return itemId;
	}
	
	public int getAmount() {
		return RandomFunction.random(lowerBound, upperBound);
	}

	public JobType getJobType() {
		return jobType;
	}

	public int getSkillId() {
		return skillId;
	}
	
	
}