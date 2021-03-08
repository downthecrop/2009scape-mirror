package core.game.node.entity.skill.agility;

import core.game.node.entity.skill.Skills;
import core.game.interaction.OptionHandler;
import core.game.node.entity.player.Player;
import core.plugin.Plugin;

/**
 * Represents an agility course.
 * @author Emperor
 */
public abstract class AgilityCourse extends OptionHandler {

	/**
	 * The player doing the agility course.
	 */
	private final Player player;

	/**
	 * The obstacles the player has already passed.
	 */
	private final boolean[] obstaclesPassed;

	/**
	 * The completion experience.
	 */
	private double completionExperience;

	/**
	 * Constructs a new {@code AgilityCourse} {@code Object}.
	 * @param player The player.
	 * @param size The amount of obstacles in the course.
	 * @param completionExperience The amount of experience to gain upon
	 * completing the course.
	 */
	public AgilityCourse(Player player, int size, double completionExperience) {
		this.player = player;
		this.obstaclesPassed = new boolean[size];
		this.completionExperience = completionExperience;
	}

	@Override
	public final Plugin<Object> newInstance(Object arg) throws Throwable {
		if (arg == null) {
			configure();
			return this;
		}
		return createInstance((Player) arg);
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	/**
	 * Configures the course.
	 */
	public abstract void configure();

	/**
	 * Creates a new instance for the player.
	 * @param player The player.
	 * @return The agility course instance.
	 */
	public abstract AgilityCourse createInstance(Player player);

	/**
	 * Gets the agility course for the given player. If the player didn't have
	 * the course as an extension, an extension will be added.
	 * @param player The player.
	 * @return The agility course instance.
	 */
	public AgilityCourse getCourse(Player player) {
		AgilityCourse course = player.getExtension(AgilityCourse.class);
		if (course == null || course.getClass() != getClass()) {
			try {
				player.addExtension(AgilityCourse.class, course = createInstance(player));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return course;
	}

	/**
	 * Finishes the lap by checking for completion and resetting.
	 */
	public void finish() {
		if (isCompleted()) {
			player.getSkills().addExperience(Skills.AGILITY, completionExperience, true);
		}
		reset();
	}

	/**
	 * Checks if the course has been completed.
	 * @return {@code True} if so.
	 */
	public boolean isCompleted() {
		for (int i = 0; i < obstaclesPassed.length; i++) {
			if (!obstaclesPassed[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Resets the currently passed obstacles in the course.
	 */
	public void reset() {
		for (int i = 0; i < obstaclesPassed.length; i++) {
			obstaclesPassed[i] = false;
		}
	}

	/**
	 * Flags an obstacles as passed.
	 * @param index The obstacle index.
	 */
	public void flag(int index) {
		obstaclesPassed[index] = true;
		if (index == obstaclesPassed.length - 1) {
			finish();
		}
	}

	/**
	 * Gets the amount of damage to deal.
	 * @param player The player.
	 * @return The amount of damage.
	 */
	protected static int getHitAmount(Player player) {
		int hit = player.getSkills().getLifepoints() / 12;
		if (hit < 2) {
			hit = 2;
		}
		return hit;
	}

	/**
	 * Gets the player.
	 * @return The player.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the passed obstacles flags.
	 * @return The passed obstacles flags.
	 */
	public boolean[] getPassedObstacles() {
		return obstaclesPassed;
	}

}