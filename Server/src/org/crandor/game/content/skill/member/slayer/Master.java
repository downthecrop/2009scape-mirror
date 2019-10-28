package org.crandor.game.content.skill.member.slayer;

import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.player.Player;

/**
 * Represents a slayer master.
 * @author 'Vexia
 */
public enum Master {
	TURAEL(8273, new int[] {15, 50}, new int[] {0, 0, 0}), 
	MAZCHNA(8274, new int[] {30, 70}, new int[] {2, 5, 15}) {
		@Override
		public boolean hasRequirment(Player player) {
			return player.getProperties().getCurrentCombatLevel() >= 20;
		}
	},
	VANNAKA(1597, new int[] {30, 80}, new int[] {4, 20, 60}) {
		@Override
		public boolean hasRequirment(Player player) {
			return player.getProperties().getCurrentCombatLevel() >= 40;
		}
	},
	CHAELDAR(1598, new int[] {110, 170}, new int[] {10, 50, 150}) {
		@Override
		public boolean hasRequirment(Player player) {
			return player.getProperties().getCurrentCombatLevel() >= 70 && player.getSkills().getLevel(Skills.SLAYER) >= 25;
		}
	},
	
	NIEVE(8649, new int[] {120, 185}, new int[] {12, 60, 180}) {
		@Override
		public boolean hasRequirment(Player player) {
			return player.getProperties().getCurrentCombatLevel() >= 85;
		}
	},
	
	DURADEL(8275, new int[] {50, 199}, new int[] {15, 75, 225}) {
		@Override
		public boolean hasRequirment(Player player) {
			return player.getProperties().getCurrentCombatLevel() >= 100 && player.getSkills().getLevel(Skills.SLAYER) >= 50;
		}
	},
	WISE_OLD_MAN(3820, new int[] {20, 55}, new int[] {25, 90, 235}) {
		@Override
		public boolean hasRequirment(Player player) {
			return player.getProperties().getCurrentCombatLevel() >= 105 && player.getSkills().getLevel(Skills.SLAYER) >= 75;
		}
	};

	/**
	 * Represents the npc id.
	 */
	private final int npc;

	/**
	 * The ranges of task amts.
	 */
	private final int[] ranges;
	
	/**
	 * The points rewarded at task milestones.
	 */
	private final int[] taskPoints;

	/**
	 * Constructs a new {@Code Master} {@Code Object}
	 * @param npc The npc id.
	 * @param ranges The ranges.
	 * @param taskPoints The task points.
	 */
	Master(int npc, int[] ranges, int[] taskPoints) {
		this.npc = npc;
		this.ranges = ranges;
		this.taskPoints = taskPoints;
	}

	/**
	 * Checks if the player has the requiremnts.
	 * @param player the player.
	 * @return {@code True} if so.
	 */
	public boolean hasRequirment(Player player) {
		return true;
	}

	/**
	 * Gets the npc.
	 * @return The npc.
	 */
	public int getNpc() {
		return npc;
	}

	/**
	 * returns the value from the integer specification.
	 * @param id the id.
	 * @return @app value.
	 */
	public static Master forId(int id) {
		for (Master master : Master.values()) {
			if (master == null) {
				continue;
			}
			if (master.getNpc() == id) {
				return master;
			}
		}
		return null;
	}

	/**
	 * Gets the ranges.
	 * @return The ranges.
	 */
	public int[] getRanges() {
		return ranges;
	}

	/**
	 * Checks if two masters share the same task.
	 * @param master the master.
	 * @param myMaster the players master.
	 * @param player the player.
	 * @return {@code True} if so.
	 */
	public static boolean hasSameTask(Master master, Master myMaster, Player player) {
		Task task = player.getSlayer().getTask();
		if (master == myMaster) {
			return true;
		}
		if (task.hasMaster(master)) {
			return true;
		}
		return false;
	}
	

	/**
	 * Gets the taskPoints.
	 * @return the taskPoints.
	 */
	public int[] getTaskPoints() {
		return taskPoints;
	}
	
}
