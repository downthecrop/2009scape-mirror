package org.crandor.game.content.skill.member.agility;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.map.Direction;
import org.crandor.plugin.Plugin;

/**
 * Handles an agility shortcut.
 * @author Vexia
 */
public abstract class AgilityShortcut extends OptionHandler {

	/**
	 * The ids used for this shortcut.
	 */
	private final int[] ids;

	/**
	 * The level required.
	 */
	private final int level;

	/**
	 * The experience required.
	 */
	private final double experience;

	/**
	 * If the player can fail this shortcut.
	 */
	private final boolean canFail;

	/**
	 * The chance to fail.
	 */
	private double failChance;

	/**
	 * The options for the shortcut.
	 */
	private final String[] options;

	/**
	 * Constructs a new {@Code AgilityShortcut} {@Code Object}
	 * @param ids the ids.
	 * @param level the level.
	 * @param experience the experience.
	 * @param canFail if this shortcut can be failed.
	 * @param failChance the chance to fail
	 * @param options the options.
	 */
	public AgilityShortcut(int[] ids, int level, double experience, boolean canFail, double failChance, String... options) {
		this.ids = ids;
		this.level = level;
		this.experience = experience;
		this.canFail = canFail;
		this.failChance = failChance;
		this.options = options;
	}

	/**
	 * Constructs a new {@Code AgilityShortcut} {@Code Object}
	 * @param ids the ids.
	 * @param level the level.
	 * @param experience the experience.
	 * @param options the options.
	 */
	public AgilityShortcut(int[] ids, int level, double experience, String... options) {
		this(ids, level, experience, false, 0.0, options);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		configure(this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (!checkRequirements(player)) {
			return true;
		}
		run(player, node.asObject(), option, checkFail(player, node.asObject(), option));
		return true;
	}

	/**
	 * Runs the agility reward for the short cut.
	 * @param player the player.
	 * @param object the object.
	 * @param option the option.
	 * @param if the shortcut failed.
	 */
	public abstract void run(Player player, GameObject object, String option, boolean failed);

	/**
	 * Checks the requirements for a player.
	 * @param player the player.
	 * @return {@code True} if so.
	 */
	public boolean checkRequirements(Player player) {
		if (player.getSkills().getLevel(Skills.AGILITY) < level) {
			player.getDialogueInterpreter().sendDialogue("You need an Agility level of at least " + level + " to negotiate this obstacle.");
			return false;
		}
		return true;
	}

	/**
	 * Checks if the player failed the shortcut.
	 * @param player the player.
	 * @param object the object.
	 * @param options the option.
	 * @return {@code True} if failed.
	 */
	private boolean checkFail(Player player, GameObject object, String option2) {
		if (!canFail) {
			return false;
		}
		return AgilityHandler.hasFailed(player, level, failChance);
	}

	/**
	 * Configures this shortcut.
	 */
	public void configure(AgilityShortcut shortcut) {
		for (int objectId : shortcut.ids) {
			ObjectDefinition def = ObjectDefinition.forId(objectId);
			for (String option : shortcut.options) {
				def.getConfigurations().put("option:" + option, shortcut);
			}
		}
	}

	/**
	 * Gets the proper object direction.
	 * @param direction the direction.
	 * @return the direction.
	 */
	protected Direction getObjectDirection(Direction direction) {
		return direction == Direction.NORTH ? Direction.EAST : direction == Direction.SOUTH ? Direction.WEST : direction == Direction.EAST ? Direction.NORTH : Direction.SOUTH;
	}

	/**
	 * Gets the level.
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Gets the experience.
	 * @return the experience
	 */
	public double getExperience() {
		return experience;
	}

	/**
	 * Gets the canFail.
	 * @return the canFail
	 */
	public boolean isCanFail() {
		return canFail;
	}

	/**
	 * Gets the failChance.
	 * @return the failChance
	 */
	public double getFailChance() {
		return failChance;
	}

	/**
	 * Sets the bafailChance.
	 * @param failChance the failChance to set.
	 */
	public void setFailChance(double failChance) {
		this.failChance = failChance;
	}

	/**
	 * Gets the ids.
	 * @return the ids
	 */
	public int[] getIds() {
		return ids;
	}

	/**
	 * Gets the option.
	 * @return the option
	 */
	public String[] getOption() {
		return options;
	}

}
