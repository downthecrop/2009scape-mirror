package org.crandor.game.content.skill.member.fletching.items.gem;

import org.crandor.game.content.skill.SkillPulse;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.update.flag.context.Animation;

/**
 * Represents the gem cutting pulse(gem to bolt).
 * @author 'Vexia
 * @date 01/12/2013
 */
public final class GemBoltCutPulse extends SkillPulse<Item> {

	/**
	 * Represents the cutting animation.
	 */
	private static final Animation ANIMATION = new Animation(6702);

	/**
	 * Represents the gem we're cutting.
	 */
	private final Gem gem;

	/**
	 * Represents the amount to make.
	 */
	private int amount;

	/**
	 * Represents the ticks passed.
	 */
	private int ticks;

	/**
	 * Constructs a new {@code GemCutPulse.java} {@code Object}.
	 * @param player the player.
	 * @param node the node.
	 * @param amount the amount.
	 */
	public GemBoltCutPulse(Player player, Item node, final Gem gem, final int amount) {
		super(player, node);
		this.gem = gem;
		this.amount = amount;
	}

	@Override
	public boolean checkRequirements() {
		if (player.getSkills().getLevel(Skills.FLETCHING) < gem.getLevel()) {
			player.getDialogueInterpreter().sendDialogue("You need a Fletching level of " + gem.getLevel() + " or above to do that.");
			return false;
		}
		if (!player.getInventory().containsItem(gem.getGem())) {
			return false;
		}
		return true;
	}

	@Override
	public void animate() {
		if (ticks % 5 == 0) {
			player.animate(ANIMATION);
		}
	}

	@Override
	public boolean reward() {
		if (++ticks % 5 != 0) {
			return false;
		}
		if (player.getInventory().remove(gem.getGem())) {
			player.getInventory().add(gem.getBolt());
			player.getSkills().addExperience(Skills.FLETCHING, gem.getExperience(), true);
		}
		amount--;
		return amount <= 0;
	}

}
