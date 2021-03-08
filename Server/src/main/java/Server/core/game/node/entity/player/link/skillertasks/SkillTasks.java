package core.game.node.entity.player.link.skillertasks;

import core.game.node.entity.skill.Skills;

public enum SkillTasks {

	// Fishing
	FANCHOVIES1("Fish Anchovies Novice", "You must successfully catch 230 Raw Anchovies.", Difficulty.NOVICE, 230, Skills.FISHING, 15),
	FANCHOVIES2("Fish Anchovies Intermediate", "You must successfully catch 480 Raw Anchovies.", Difficulty.INTERMEDIATE, 480, Skills.FISHING, 15),
	FHERRING1("Fish Herring Novice", "You must successfully catch 280 Raw Herring.", Difficulty.NOVICE, 280, Skills.FISHING, 10),
	FHERRING2("Fish Herring Intermediate", "You must successfully catch 450 Raw Herring.", Difficulty.INTERMEDIATE, 450, Skills.FISHING, 10),
	FLOBSTER1("Fish Lobster Intermediate", "You must successfully catch 300 Raw Lobster.", Difficulty.INTERMEDIATE, 300, Skills.FISHING, 40),
	FLOBSTER2("Fish Lobster Advanced", "You must successfully catch 650 Raw Lobster.", Difficulty.ADVANCED, 650, Skills.FISHING, 40),
	FSALMON1("Fish Salmon Novice", "You must successfully catch 220 Raw Salmon.", Difficulty.NOVICE, 220, Skills.FISHING, 30),
	FSALMON2("Fish Salmon Intermediate", "You must successfully catch 400 Raw Salmon.", Difficulty.INTERMEDIATE, 400, Skills.FISHING, 30),
	FSHARK1("Fish Shark Intermediate", "You must successfully catch 220 Raw Shark.", Difficulty.INTERMEDIATE, 220, Skills.FISHING, 76),
	FSHARK2("Fish Shark Advanced", "You must successfully catch 380 Raw Shark.", Difficulty.ADVANCED, 380, Skills.FISHING, 76),
	FSHARK3("Fish Shark Elite", "You must successfully catch 500 Raw Shark.", Difficulty.ELITE, 500, Skills.FISHING, 76),
	FSHRIMP1("Fish Shrimp Novice", "You must successfully catch 300 Raw Shrimp.", Difficulty.NOVICE, 300, Skills.FISHING, 1),
	FSHRIMP2("Fish Shrimp Intermediate", "You must successfully catch 500 Raw Shrimp.", Difficulty.INTERMEDIATE, 500, Skills.FISHING, 1),
	FSWORD1("Fish Swordfish Intermediate", "You must successfully catch 320 Raw Swordfish.", Difficulty.INTERMEDIATE, 320, Skills.FISHING, 50),
	FSWORD2("Fish Swordfish Advanced", "You must successfully catch 510 Raw Swordfish.", Difficulty.ADVANCED, 510, Skills.FISHING, 50),
	FTROUT1("Fish Trout Novice", "You must successfully catch 250 Raw Trout.", Difficulty.NOVICE, 250, Skills.FISHING, 20),
	FTROUT2("Fish Trout Intermediate", "You must successfully catch 420 Raw Trout.", Difficulty.INTERMEDIATE, 420, Skills.FISHING, 20),
	FTUNA1("Fish Tuna Novice", "You must successfully catch 150 Raw Tuna.", Difficulty.NOVICE, 150, Skills.FISHING, 35),
	FTUNA2("Fish Tuna Intermediate", "You must successfully catch 340 Raw Tuna.", Difficulty.INTERMEDIATE, 340, Skills.FISHING, 35);

	private String assignment;
	private String description;
	private Difficulty difficulty;
	private int amount;
	private int skill;
	private int level;

	SkillTasks(String assignment, String description, Difficulty difficulty, int amount, int skill, int level) {
		this.assignment = assignment;
		this.description = description;
		this.difficulty = difficulty;
		this.amount = amount;
		this.skill = skill;
		this.level = level;
	}

	public int getAmount() {
		return amount;
	}

	public String getAssignment() {
		return assignment;
	}

	public String getDescription() {
		return description;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public int getLevel() {
		return level;
	}

	public int getSkill() {
		return skill;
	}

}
