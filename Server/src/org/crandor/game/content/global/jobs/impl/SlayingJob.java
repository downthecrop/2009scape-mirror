package org.crandor.game.content.global.jobs.impl;

import org.crandor.game.content.global.jobs.Job;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.tools.RandomFunction;
import org.crandor.tools.StringUtils;

/**
 * A job that requires slaying monsters.
 * @author jamix77
 *
 */
public class SlayingJob extends Job {
	
	private int[] npcId;
	private int amount;
	private int originalAmount;

	public SlayingJob(int employer, Player player, int amount, int... npcId) {
		super(employer, player);
		this.npcId = npcId;
		this.amount = amount;
		this.originalAmount = amount;
	}

	@Override
	public boolean reward() {
		if (getAmount() <= 0) {
			if (!(getPlayer().getInventory().add(new Item(995,originalAmount*250)))) {
				getPlayer().getBank().add(new Item(995,originalAmount*250));
			}
			return true;
		}
		getPlayer().sendMessage("You still have " + amount + " " + StringUtils.plusS(new NPC(npcId[0]).getName()) + " left to slay.");
		return false;
	}
	
	/**
	 * Checks when an NPC is kiled that it can cound towards task completion
	 * @param npc
	 */
	public void handleDeath(NPC npc) {
		for (int i = 0; i < npcId.length; i++) {
			if (npc.getId() == npcId[i]) {
				amount -= 1;
				break;
			}
		}
	}
	
	public int getAmount() {
		return amount;
	}

	public int[] getNpcId() {
		return npcId;
	}
	
	public void setNpcId(int[] npcId) {
		this.npcId = npcId;
	}

	/**
	 * Jobs that you must slay monsters to complete
	 * @author jamix77
	 *
	 */
	public enum SlayingJobs {
		CHICKEN(25,26,41,1017),
		COW(25,25,397,1766,81,1767),
		GIANT_SPIDER(22,25,59,60),
		SCORPION(25,25,107,108,109,144,4402,4403),
		GOBLIN(25,25,100,101,102,298,299,444,445,489,745,1769,1770,1771,1772,1773,1774,1775,1776,2274,2275,2276,2277,2278,2279,2280,2281,2678,2679,2680,2681,3060,3264,3265,3266,3267);
		
		
		private final int npcId[];
		private final int upperBound;
		private final int lowerBound;
		
		private SlayingJobs(int lowerBound,int upperBound, int... npcId) {
			this.lowerBound = lowerBound;
			this.upperBound = upperBound;
			this.npcId = npcId;
		}
		
		public int getAmount() {
			return RandomFunction.random(lowerBound, upperBound);
		}

		public int[] getNpcId() {
			return npcId;
		}

		
		
	}

	public void setAmount(int int1) {
		amount = int1;
	}

}
