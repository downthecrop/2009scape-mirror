package org.crandor.game.content.skill.member.slayer;

import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.player.Player;

/**
 * Represents a task to be set.
 * @author 'Vexia
 */
public class Task {

	/**
	 * Represents the npc ids reated to the task.
	 */
	private final int[] npcs;

	/**
	 * Represents the tip to give.
	 */
	private final Object[] tip;

	/**
	 * Represents the masters able to assign this task.
	 */
	private final Master[] masters;

	/**
	 * The required slayer level.
	 */
	private final int level;

	/**
	 * If the task is killing undead NPCs.
	 */
	private final boolean undead;

	/**
	 * Any requirement equipment for this task.
	 */
	private final Equipment[] equipment;

	/**
	 * The task amount hash.
	 */
	private int taskAmtHash;

	/**
	 * Constructs a new {@code Task} {@code Object}.
	 * @param npcs the npcs.
	 * @param tip the tips.
	 * @param level the level.
	 * @param masters the masters.
	 * @param undead if undead.
	 * @param equipment the equipment.
	 */
	public Task(int[] npcs, Object[] tip, int level, Master[] masters, boolean undead, Equipment... equipment) {
		this.npcs = npcs;
		this.tip = tip;
		this.level = level;
		this.masters = masters;
		this.undead = undead;
		this.equipment = equipment;
	}

	/**
	 * Constructs a new {@code Task} {@code Object}.
	 * @param npcs the npcs.
	 * @param tip the tips.
	 * @param level the level.
	 * @param masters the masters.
	 * @param undead if undead.
	 * @param the hash of amounts to recieve.
	 * @param equipment the equipment.
	 */
	public Task(int[] npcs, Object[] tip, int level, Master[] masters, boolean undead, int taskAmtHash, Equipment... equipment) {
		this(npcs, tip, level, masters, undead, equipment);
		this.taskAmtHash = taskAmtHash;
	}

	/**
	 * Checks if this task can be assigned to the player.
	 * @param player the player.
	 * @param master the master.
	 * @return {@code True} if so.
	 */
	public boolean canAssign(Player player, Master master) {
		final int slayerLevel = player.getSkills().getStaticLevel(Skills.SLAYER);
		if (isDisabled()) {
			return false;
		}
		if (getLevel() > slayerLevel) {
			return false;
		}
		if (!hasEquipmentRequirement(player)) {
			return false;
		}
		if (player.getSlayer().getRemoved().contains(this)) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if they have the equipment requirement.
	 * @param player the player.
	 * @return {@code True} if so.
	 */
	public boolean hasEquipmentRequirement(final Player player) {
		for (Equipment e : equipment) {
			if (!e.hasRequirement(player)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if the task contains the master given.
	 * @param master the master.
	 * @return the {@code True} if so
	 */
	public boolean hasMaster(Master master) {
		for (int i = 0; i < masters.length; i++) {
			if (masters[i] == master) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the task is disabled.
	 * @return {@code True} if so.
	 */
	public boolean isDisabled() {
		return false;
	}

	/**
	 * Gets the ranges.
	 * @param master the master.
	 * @return the ranges.
	 */
	public int[] getRanges(Master master) {
		if (taskAmtHash != 0) {
			int min = taskAmtHash & 0xFFFF;
			int max = (taskAmtHash >> 16) & 0xFFFF;
			return new int[] { min, max };
		}
		return master.getRanges();
	}

	/**
	 * Gets the name.
	 * @return the name.
	 */
	public String getName() {
		return NPCDefinition.forId(npcs[0]).getName();
	}

	/**
	 * Gets the npcs.
	 * @return The npcs.
	 */
	public int[] getNpcs() {
		return npcs;
	}

	/**
	 * Gets the masters.
	 * @return The masters.
	 */
	public Master[] getMasters() {
		return masters;
	}

	/**
	 * Gets the level.
	 * @return The level.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Gets the tip.
	 * @return The tip.
	 */
	public String[] getTip() {
		return (String[]) tip;
	}

	/**
	 * Gets the undead.
	 * @return The undead.
	 */
	public boolean isUndead() {
		return undead;
	}

	/**
	 * Gets the equipment.
	 * @return The equipment.
	 */
	public Equipment[] getEquipment() {
		return equipment;
	}

}
