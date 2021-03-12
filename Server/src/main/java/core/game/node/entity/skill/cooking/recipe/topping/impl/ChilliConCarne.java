package core.game.node.entity.skill.cooking.recipe.topping.impl;

import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.cooking.recipe.Recipe;
import core.game.interaction.NodeUsageEvent;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;

/**
 * Represents the chilli con carne recipe. This recipe consists of using spicy
 * sauce with cooked meat.
 * @author 'Vexia
 * @date 22/12/2013
 */
public class ChilliConCarne extends Recipe {

	/**
	 * Represents the bowl item.
	 */
	private static final Item SPICY_SAUCE = new Item(7072);

	/**
	 * Represents the chilli con carne item.
	 */
	private static final Item CHILLI_CON_CARNE = new Item(7062);

	/**
	 * Represents the cooked meat item.
	 */
	private static final Item COOKED_MEAT = new Item(2142);

	/**
	 * Represents the knife item.
	 */
	private static final Item KNIFE = new Item(946);

	@Override
	public void mix(final Player player, final NodeUsageEvent event) {
		if (player.getSkills().getLevel(Skills.COOKING) < 9) {
			player.getDialogueInterpreter().sendDialogue("You need a Cooking level of at least " + 9 + " in order to do this.");
			return;
		}
		if (!player.getInventory().containsItem(KNIFE)) {
			player.getDialogueInterpreter().sendDialogue("You need a knife in order to cut up the meat.");
			return;
		}
		super.mix(player, event);
		player.getSkills().addExperience(Skills.COOKING, 25, true);
	}

	@Override
	public Item getBase() {
		return SPICY_SAUCE;
	}

	@Override
	public Item getProduct() {
		return CHILLI_CON_CARNE;
	}

	@Override
	public Item[] getIngredients() {
		return new Item[] { COOKED_MEAT };
	}

	@Override
	public Item[] getParts() {
		return new Item[] {};
	}

	@Override
	public String getMixMessage(NodeUsageEvent event) {
		return "You put the cut up meat into the bowl.";
	}

	@Override
	public boolean isSingular() {
		return true;
	}

}
