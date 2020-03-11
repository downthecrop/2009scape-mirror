package org.crandor.game.content.global.jobs.impl;

import org.crandor.game.content.global.jobs.JobType;
import org.crandor.game.content.skill.Skills;
import org.crandor.tools.RandomFunction;

/**
 * Represents the jobs that Mikasi can give.
 * @author jamix77
 *
 */
public enum MikasiJobs {
	LOG(20,28, 1, 1511,JobType.GATHERING, Skills.WOODCUTTING),
	COWHIDES(20,28,1, 1739,JobType.GATHERING,0),
	OAK(22,28,15, 1521,JobType.GATHERING,Skills.WOODCUTTING),
	WATER_RUNE(20,28,5, 555,JobType.GATHERING,Skills.RUNECRAFTING),
	EARTH_RUNE(20,28,9, 557,JobType.GATHERING,Skills.RUNECRAFTING),
	FIRE_RUNE(20,28,14, 554,JobType.GATHERING,Skills.RUNECRAFTING),
	AIR_RUNE(20,28,1, 556,JobType.GATHERING,Skills.RUNECRAFTING),
	RUNE_ESS(20,28,1, 1436,JobType.GATHERING,Skills.MINING),
	BALL_OF_WOOL(20,28,1, 1759,JobType.GATHERING,Skills.CRAFTING),
	LEATHER_GLOVE(20,28,1, 1059,JobType.GATHERING,Skills.CRAFTING),
	WILLOW(22,28,30, 1519,JobType.GATHERING,Skills.WOODCUTTING),
	LEATHER_BOOT(24,24,1, 1061,JobType.GATHERING,Skills.CRAFTING);
	
	private final int id;
	private final int upperBound;
	private final int lowerBound;
	private final int lvlReq;
	private final JobType jobType;
	private final int skillId;
	
	private MikasiJobs(int lowerBound,int upperBound, int lvlReq, int id, JobType jobType, int skillId) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.id = id;
		this.lvlReq = lvlReq;
		this.jobType = jobType;
		this.skillId = skillId;
	}

	public int getLvlReq() {
		return lvlReq;
	}

	public int getid() {
		return id;
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