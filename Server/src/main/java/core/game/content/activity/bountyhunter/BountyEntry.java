package core.game.content.activity.bountyhunter;

import core.game.node.entity.player.Player;
import rs09.game.world.GameWorld;

/**
 * Holds a player's bounty hunter data.
 * @author Emperor
 */
public final class BountyEntry {

	/**
	 * The target.
	 */
	private Player target;

	/**
	 * The player hunting this player.
	 */
	private Player hunter;

	/**
	 * Constructs a new {@code BountyEntry} {@code Object}.
	 */
	public BountyEntry() {
		/*
		 * empty.
		 */
	}

	/**
	 * Updates the overlay.
	 * @param player The player.
	 */
	public void update(Player player) {
		String name = "No one";
		if (target != null) {
			name = target.getUsername();
		}
		player.getPacketDispatch().sendString(name, 653, 7);
		updatePenalty(player, false);
	}

	/**
	 * Updates the current penalty.
	 * @param player The player.
	 * @param unlock If the components should be unlocked.
	 */
	public void updatePenalty(Player player, boolean unlock) {
		int penalty = player.getAttribute("pickup_penalty", 0);
		int child = -1;
		if (GameWorld.getTicks() > penalty) {
			player.removeAttribute("pickup_penalty");
			player.getPacketDispatch().sendInterfaceConfig(653, 8, true);
		} else if (penalty != 0) {
			child = 8;
			int seconds = (int) Math.round((penalty - GameWorld.getTicks()) * 0.6);
			player.getPacketDispatch().sendString(seconds + " Sec", 653, 10);
		}
		penalty = player.getAttribute("exit_penalty", 0);
		if (GameWorld.getTicks() > penalty) {
			player.removeAttribute("exit_penalty");
			player.getPacketDispatch().sendInterfaceConfig(653, 11, true);
		} else if (penalty != 0) {
			child = 11;
			int seconds = (int) Math.round((penalty - GameWorld.getTicks()) * 0.6);
			player.getPacketDispatch().sendString(seconds + " Sec", 653, 13);
		}
		if (unlock && child > -1) {
			player.getPacketDispatch().sendInterfaceConfig(653, child, false);
		}
	}

	/**
	 * Gets the target.
	 * @return The target.
	 */
	public Player getTarget() {
		return target;
	}

	/**
	 * Sets the target.
	 * @param target The target to set.
	 */
	public void setTarget(Player target) {
		this.target = target;
	}

	/**
	 * Gets the hunter.
	 * @return The hunter.
	 */
	public Player getHunter() {
		return hunter;
	}

	/**
	 * Sets the hunter.
	 * @param hunter The hunter to set.
	 */
	public void setHunter(Player hunter) {
		this.hunter = hunter;
	}
}