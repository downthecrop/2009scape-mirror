package org.crandor.game.content.skill.member.fletching.items.darts;

import org.crandor.game.content.skill.SkillPulse;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;

/**
 * Represents the dart pulse.
 * @author 'Vexia
 */
public final class DartPulse extends SkillPulse<Item> {

	/**
	 * Represents the feather item.
	 */
	private static final Item FEATHER = new Item(314);

	/**
	 * Represents the dart.
	 */
	private final Dart dart;

	/**
	 * Represents the sets to make.
	 */
	private int sets;

	/**
	 * Represents if we're using sets.
	 */
	private boolean useSets = false;

	/**
	 * Constructs a new {@code DartPulse.java} {@code Object}.
	 * @param player the player.
	 * @param node the node.
	 */
	public DartPulse(Player player, Item node, Dart dart, int sets) {
		super(player, node);
		this.dart = dart;
		this.sets = sets;
	}

	@Override
	public boolean checkRequirements() {
		if (player.getSkills().getLevel(Skills.FLETCHING) < dart.getLevel()) {
			player.getDialogueInterpreter().sendDialogue("You need a fletching level of " + dart.getLevel() + " to do this.");
			return false;
		}
		if (!player.getInventory().containsItem(FEATHER)) {
			return false;
		}
		if (!player.getInventory().containsItem(dart.getItem())) {
			return false;
		}
		if (player.getInventory().contains(dart.getItem().getId(), 10) && player.getInventory().contains(FEATHER.getId(), 10)) {
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
		final Item dartt = dart.getItem();
		if (useSets) {
			FEATHER.setAmount(10);
			dartt.setAmount(10);
			player.getPacketDispatch().sendMessage("You attach feathers to 10 darts.");
		} else {
			player.getPacketDispatch().sendMessage("You attach a feather to a dart.");
		}
		if (player.getInventory().remove(FEATHER, dartt)) {
			Item product = dart.getProduct();
			if (useSets) {
				product.setAmount(10);
			} else {
				product.setAmount(1);
			}
			player.getSkills().addExperience(Skills.FLETCHING, useSets ? dart.getExperience() * 10 : dart.getExperience(), true);
			player.getInventory().add(product);
		}
		FEATHER.setAmount(1);
		if (!player.getInventory().containsItem(FEATHER)) {
			return true;
		}
		if (!player.getInventory().containsItem(dart.getItem())) {
			return true;
		}
		useSets = false;
		sets--;
		return sets == 0;
	}

	@Override
	public void message(int type) {
	}

}
