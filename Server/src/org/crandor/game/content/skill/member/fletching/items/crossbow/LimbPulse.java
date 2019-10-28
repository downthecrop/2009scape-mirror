package org.crandor.game.content.skill.member.fletching.items.crossbow;

import org.crandor.game.content.skill.SkillPulse;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;

/**
 * Represents the skill pulse of attaching limbs.
 * @author 'Vexia
 */
public class LimbPulse extends SkillPulse<Item> {

	/**
	 * Represents the limbs.
	 */
	private final Limb limb;

	/**
	 * Represents the amount.
	 */
	private int amount;

	/**
	 * Constructs a new {@code StringcrossbowPlugin.java} {@code Object}.
	 * @param player the player.
	 * @param node the node.
	 */
	public LimbPulse(Player player, Item node, final Limb limb, int amount) {
		super(player, node);
		this.limb = limb;
		this.amount = amount;
	}

	@Override
	public boolean checkRequirements() {
		if (player.getSkills().getLevel(Skills.FLETCHING) < limb.getLevel()) {
			player.getDialogueInterpreter().sendDialogue("You need a fletching level of " + limb.getLevel() + " to attach these limbs.");
			return false;
		}
		if (node.getId() != limb.getLimb().getId()) {
			player.getDialogueInterpreter().sendDialogue("That's not the correct limb to attach.");
			return false;
		}
		if (!player.getInventory().containsItem(limb.getLimb())) {
			return false;
		}
		if (!player.getInventory().containsItem(node)) {
			return false;
		}
		return true;
	}

	@Override
	public void animate() {
		player.animate(limb.getAnimation());
	}

	@Override
	public boolean reward() {
		if (getDelay() == 1) {
			super.setDelay(5);
			return false;
		}
		if (player.getInventory().remove(limb.getStock(), limb.getLimb())) {
			player.getInventory().add(limb.getProduct());
			player.getSkills().addExperience(Skills.FLETCHING, limb.getExperience(), true);
			player.getPacketDispatch().sendMessage("You attach the metal limbs to the stock.");
		}
		if (!player.getInventory().containsItem(limb.getLimb())) {
			return true;
		}
		amount--;
		return amount == 0;
	}

	@Override
	public void message(int type) {
		switch (type) {
		case 0:
			break;
		case 1:
			break;
		}
	}

}
