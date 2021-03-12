package core.game.content.ame;

/**
 * Used to monitor experience gain.
 * @author Emperor
 */
public final class ExperienceMonitor {

	/**
	 * The skill id.
	 */
	private final int skillId;

	/**
	 * The amount of experience gained since the last monitor pulse.
	 */
	private int experienceAmount;

	/**
	 * Constructs a new {@code ExperienceMonitor} {@code Object}.
	 * @param skillId The skill id.
	 */
	public ExperienceMonitor(int skillId) {
		this.skillId = skillId;
	}

	/**
	 * Gets the experienceAmount.
	 * @return The experienceAmount.
	 */
	public int getExperienceAmount() {
		return experienceAmount;
	}

	/**
	 * Sets the experienceAmount.
	 * @param experienceAmount The experienceAmount to set.
	 */
	public void setExperienceAmount(int experienceAmount) {
		this.experienceAmount = experienceAmount;
	}

	/**
	 * Gets the skillId.
	 * @return The skillId.
	 */
	public int getSkillId() {
		return skillId;
	}
}