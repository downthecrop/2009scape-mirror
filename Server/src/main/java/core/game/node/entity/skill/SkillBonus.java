package core.game.node.entity.skill;

/**
 * Represents a skill bonus.
 * @author Emperor
 */
public final class SkillBonus {

	/**
	 * The skill id.
	 */
	private final int skillId;

	/**
	 * The bonus.
	 */
	private final double bonus;

	/**
	 * The base bonus (in levels).
	 */
	private final int baseBonus;

	/**
	 * Constructs a new {@code SkillBonus} {@code Object}.
	 * @param skillId The skill id.
	 * @param bonus The bonus.
	 */
	public SkillBonus(int skillId, double bonus) {
		this(skillId, bonus, 0);
	}

	/**
	 * Constructs a new {@code SkillBonus} {@code Object}.
	 * @param skillId The skill id.
	 * @param bonus The bonus.
	 * @param baseBonus The base bonus in levels.
	 */
	public SkillBonus(int skillId, double bonus, int baseBonus) {
		this.skillId = skillId;
		this.bonus = bonus;
		this.baseBonus = baseBonus;
	}

	/**
	 * @return the skillId.
	 */
	public int getSkillId() {
		return skillId;
	}

	/**
	 * @return the bonus.
	 */
	public double getBonus() {
		return bonus;
	}

	/**
	 * @return the baseBonus.
	 */
	public int getBaseBonus() {
		return baseBonus;
	}

}
