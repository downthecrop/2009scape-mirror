package core.game.node.entity.skill.cooking.fermenting;

import core.game.node.entity.skill.Skills;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import core.tools.RandomFunction;

/**
 * Represents a pulse used to ferment wine.
 * @author 'Vexia
 * @date 22/12/2013
 */
public final class WineFermentingPulse extends Pulse {

	/**
	 * Represents the player instance.
	 */
	private final Player player;

	/**
	 * Represents a counter used to time when to ferment.
	 */
	private int count;

	/**
	 * Constructs a new {@code WineFermentingPulse} {@Code Object}
	 * @param delay the delay.
	 */
	public WineFermentingPulse(int delay, final Player player) {
		super(delay);
		this.player = player;
	}

	@Override
	public boolean pulse() {
		if (count++ >= 16) {
			int rand = RandomFunction.random(1, 3);
			switch (rand) {
			case 1:
				if (player.getInventory().contains(1995, 1)) {
					player.getInventory().replace(new Item(1991, 1), player.getInventory().getSlot(new Item(1995, 1)));
				} else if (player.getBank().contains(1995, 1)) {
					player.getBank().replace(new Item(1991, 1), player.getBank().getSlot(new Item(1995, 1)));
				}
				return true;
			case 2:
				if (player.getInventory().contains(1995, 1)) {
					player.getInventory().replace(new Item(1993, 1), player.getInventory().getSlot(new Item(1995, 1)));
					player.getSkills().addExperience(Skills.COOKING, 200, true);
				} else if (player.getBank().contains(1995, 1)) {
					player.getBank().replace(new Item(1993, 1), player.getBank().getSlot(new Item(1995, 1)));
					player.getSkills().addExperience(Skills.COOKING, 200);
				}
				return true;
			case 3:
				if (player.getInventory().contains(1995, 1)) {
					player.getInventory().replace(new Item(1993, 1), player.getInventory().getSlot(new Item(1995, 1)));
					player.getSkills().addExperience(Skills.COOKING, 200);
				} else if (player.getBank().contains(1995, 1)) {
					player.getBank().replace(new Item(1993, 1), player.getBank().getSlot(new Item(1995, 1)));
					player.getSkills().addExperience(Skills.COOKING, 200);
				}
				return true;
			}
			return true;
		}
		count++;
		return false;
	}

	/**
	 * Gets the player.
	 * @return the player.
	 */
	public Player getPlayer() {
		return player;
	}

}
