package org.crandor.game.content.skill.member.fletching.items.gem;

import org.crandor.game.content.skill.SkillPulse;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;

/**
 * Represents the attaching of a gem bolt to a premade bolt.
 * @author 'Vexia
 * @date 01/12/2013
 */
public final class GemBoltPulse extends SkillPulse<Item> {

	/**
	 * Represents the gem bolt being made.
	 */
	private GemBolt bolt;

	/**
	 * Represents the sets to make.
	 */
	private int sets = 0;

	/**
	 * Represents the ticks passed.
	 */
	private int ticks;

	/**
	 * Constructs a new {@code GemBoltPulse} {@code Object}.
	 * @param player the player.
	 * @param node the node.
	 * @param sets the sets.
	 */
	public GemBoltPulse(Player player, Item node, GemBolt bolt, int sets) {
		super(player, node);
		this.bolt = bolt;
		this.sets = sets;
	}

	@Override
	public boolean checkRequirements() {
		if (player.getSkills().getLevel(Skills.FLETCHING) < bolt.getLevel()) {
			player.getDialogueInterpreter().sendDialogue("You need a Fletching level of " + bolt.getLevel() + " or above to do that.");
			return false;
		}
		if (!player.getInventory().containsItem(bolt.getBase()) || !player.getInventory().containsItem(bolt.getTip())) {
			return false;
		}
		return true;
	}

	@Override
	public void animate() {
	}

	@Override
	public boolean reward() {
		if (++ticks % 3 != 0) {
			return false;
		}
		if (player.getInventory().remove(bolt.getBase(), bolt.getTip())) {
			player.getInventory().add(bolt.getProduct());
			player.getSkills().addExperience(Skills.FLETCHING, bolt.getExperience(), true);
			player.getPacketDispatch().sendMessage("You fletch 10 bolts.");
		}
		sets--;
		return sets <= 0;
	}

}
