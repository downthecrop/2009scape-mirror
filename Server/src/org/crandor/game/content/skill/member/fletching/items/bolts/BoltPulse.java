package org.crandor.game.content.skill.member.fletching.items.bolts;

import org.crandor.game.content.skill.SkillPulse;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;

/**
 * Represents the bolt pulse class to make bolts.
 * @author 'Vexia
 */
public final class BoltPulse extends SkillPulse<Item> {

	/**
	 * Represents the feather item.
	 */
	private final Item feather = new Item(314);

	/**
	 * Represents the bolt.
	 */
	private final Bolt bolt;

	/**
	 * Represents the sets to do.
	 */
	private int sets;

	/**
	 * Represents if we're using sets.
	 */
	private boolean useSets = false;

	/**
	 * Constructs a new {@code BoltPulse.java} {@code Object}.
	 * @param player the player.
	 * @param node the node.
	 */
	public BoltPulse(Player player, Item node, final Bolt bolt, final int sets) {
		super(player, node);
		this.bolt = bolt;
		this.sets = sets;
	}

	@Override
	public boolean checkRequirements() {
		if (bolt.getItem().getId() == 13279) {
			if (!player.getSlayer().getLearned()[0]) {
				player.getDialogueInterpreter().sendDialogue("You need to unlock the ability to create broad bolts.");
				return false;
			}
		}
		if (player.getSkills().getLevel(Skills.FLETCHING) < bolt.getLevel()) {
			player.getDialogueInterpreter().sendDialogue("You need a fletching level of " + bolt.getLevel() + " in order to do this.");
			return false;
		}
		if (!player.getInventory().containsItem(feather)) {
			return false;
		}
		if (!player.getInventory().containsItem(bolt.getItem())) {
			return false;
		}
		if (player.getInventory().contains(bolt.getItem().getId(), 10) && player.getInventory().contains(feather.getId(), 10)) {
			useSets = true;
		} else {
			useSets = false;
		}
		return true;
	}

	@Override
	public void animate() {
	}

	@Override
	public boolean reward() {
		if (getDelay() == 1) {
			super.setDelay(3);
		}
		final Item boltt = bolt.getItem();
		if (useSets) {
			feather.setAmount(10);
			boltt.setAmount(10);
			player.getPacketDispatch().sendMessage("You fletch 10 bolts.");
		} else {
			player.getPacketDispatch().sendMessage("You attach a feather to a bolt.");
		}
		if (player.getInventory().remove(feather, boltt)) {
			Item product = bolt.getProduct();
			if (useSets) {
				product.setAmount(10);
			} else {
				product.setAmount(1);
			}
			player.getSkills().addExperience(Skills.FLETCHING, useSets ? bolt.getExperience() * 10 : bolt.getExperience(), true);
			player.getInventory().add(product);
		}
		feather.setAmount(1);
		if (!player.getInventory().containsItem(feather)) {
			return true;
		}
		if (!player.getInventory().containsItem(bolt.getItem())) {
			return true;
		}
		sets--;
		return sets == 0;
	}

	@Override
	public void message(int type) {
	}

}
