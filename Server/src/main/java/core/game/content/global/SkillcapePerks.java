package core.game.content.global;

/**
 * Handles the skillcape perks.
 * @author Empathy
 *
 */
public enum SkillcapePerks {
	
	ATTACK(9747, 9748),
	STRENGTH(9750, 9751),
	DEFENCE(9753, 9754),
	RANGING(9756, 9757),
	PRAYER(9759, 9760),
	MAGIC(9762, 9763),
	RUNECRAFTING(9765, 9766),
	HITPOINTS(9768, 9769),
	AGILITY(9771, 9772),
	HERBLORE(9774, 9775),
	THIEVEING(9777, 9778),
	CRAFTING(9780, 9781),
	FLETCHING(9783, 9784),
	SLAYER(9786, 9787),
	CONSTRUCTION(9789, 9790),
	MINING(9792, 9793),
	SMITHING(9795, 9796),
	FISHING(9798, 9799),
	COOKING(9801, 9802),
	FIREMAKING(9804, 9805),
	WOODCUTTING(9807, 9808),
	FARMING(9810, 9811),	
	HUNTING(9948, 9949),
	SUMMONING(12169,12170),
	MAX_CAPE(14831, 14833, 14835, 14839, 14840)	
	;
	
	/**
	 * The skillcape Ids.
	 */
	private final int[] skillcapeIds;
	
	
	/**
	 * 
	 * Constructs a new {@code SkillcapePerks} object.
	 * @param skillcapeIds
	 */
	SkillcapePerks(int... skillcapeIds) {
		this.skillcapeIds = skillcapeIds;
	}

	/**
	 * The skillcapeIds.
	 * @return the skillcape ids.
	 */
	public int[] getSkillcapeIds() {
		return skillcapeIds;
	}
}
