package org.crandor.game.content.skill;

import org.crandor.game.node.entity.Entity;
import org.crandor.game.world.GameWorld;

/**
 * Handles the skill restoration data.
 * @author Emperor
 */
public final class SkillRestoration {

	/**
	 * The skill index.
	 */
	private final int skillId;

	/**
	 * The current tick.
	 */
	private int tick;

	/**
	 * Constructs a new {@code SkillRestoration} {@code Object}.
	 * @param skillId The skill id.
	 */
	public SkillRestoration(int skillId) {
		this.skillId = skillId;
		restart();
	}

	/**
	 * Restores the skill.
	 * @param entity The entity.
	 */
	public void restore(Entity entity) {
		Skills skills = entity.getSkills();
		if (tick < GameWorld.getTicks()) {
			if (skillId == Skills.HITPOINTS) {
				int max = skills.getMaximumLifepoints();
				if (skills.getLifepoints() != max) {
					skills.heal(skills.getLifepoints() < max ? 1 : -1);
				}
			} else {
				int dynamic = skills.getLevel(skillId);
				int stat = skills.getStaticLevel(skillId);
				if (dynamic != stat) {
					skills.updateLevel(skillId, dynamic < stat ? 1 : -1, stat);
				}
			}
			restart();
		}
	}

	/**
	 * Gets the tick.
	 * @return The tick.
	 */
	public int getTick() {
		return tick;
	}

	/**
	 * Sets the tick.
	 * @param tick The tick to set.
	 */
	public void setTick(int tick) {
		this.tick = tick;
	}

	/**
	 * Gets the skillId.
	 * @return The skillId.
	 */
	public int getSkillId() {
		return skillId;
	}

	/**
	 * Restarts the restoration.
	 */
	public void restart() {
		this.tick = GameWorld.getTicks() + 100;
	}
}