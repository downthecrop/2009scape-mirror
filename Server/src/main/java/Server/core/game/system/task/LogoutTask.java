package core.game.system.task;

import core.game.node.entity.player.Player;
import core.game.world.GameWorld;

/**
 * A task called upon when a player disconnects from the game.
 * @author Emperor
 */
public abstract class LogoutTask {

	/**
	 * The amount of ticks this logout task is valid for.
	 */
	private int validity;

	/**
	 * Constructs a new {@code LogoutTask} {@code Object}.
	 */
	public LogoutTask() {
		this(Integer.MAX_VALUE - GameWorld.getTicks());
	}

	/**
	 * Constructs a new {@code LogoutTask} {@code Object}.
	 * @param ticks The amount of ticks this logout task is valid.
	 */
	public LogoutTask(int ticks) {
		this.validity = GameWorld.getTicks() + ticks;
	}

	/**
	 * Runs the task.
	 * @param player The player.
	 */
	public abstract void run(Player player);

	/**
	 * Fires the logout task, if it is still valid.
	 * @param player The player logging out.
	 */
	public void fire(Player player) {
		if (isValid()) {
			run(player);
		}
	}

	/**
	 * Checks if the logout task is valid.
	 * @return {@code True} if so.
	 */
	public boolean isValid() {
		return validity > GameWorld.getTicks();
	}

	/**
	 * Sets the new validity delay.
	 * @param ticks The amount of ticks this task should remain valid for.
	 */
	public void setDelay(int ticks) {
		this.validity = GameWorld.getTicks() + ticks;
	}
}