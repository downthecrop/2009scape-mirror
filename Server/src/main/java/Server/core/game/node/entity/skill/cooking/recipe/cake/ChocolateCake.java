package core.game.node.entity.skill.cooking.recipe.cake;

import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.cooking.recipe.Recipe;
import core.game.interaction.NodeUsageEvent;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;

/**
 * Represents the chocolate cake recipe. This recipe consists of adding a
 * chocolate bar to a cake.
 * @author 'Vexia
 * @date 22/12/2013
 */
public class ChocolateCake extends Recipe {

	/**
	 * Represents the cake item.
	 */
	private static final Item CAKE = new Item(1891);

	/**
	 * Represents the chocolate cake item.
	 */
	private static final Item CHOCOLATE_CAKE = new Item(1897);

	/**
	 * Represents the chocolate bar item.
	 */
	private static final Item CHOCOLATE_BAR = new Item(1973);

	@Override
	public void mix(final Player player, final NodeUsageEvent event) {
		if (player.getSkills().getLevel(Skills.COOKING) < 50) {
			player.getDialogueInterpreter().sendDialogue("You need a Cooking level of 50 in order to do that.");
			return;
		}
		super.mix(player, event);
		player.getSkills().addExperience(Skills.COOKING, 30, true);
	}

	@Override
	public Item getBase() {
		return CAKE;
	}

	@Override
	public Item getProduct() {
		return CHOCOLATE_CAKE;
	}

	@Override
	public Item[] getIngredients() {
		return new Item[] { CHOCOLATE_BAR };
	}

	@Override
	public Item[] getParts() {
		return new Item[] {};
	}

	@Override
	public String getMixMessage(NodeUsageEvent event) {
		return "You add chocolate to the cake.";
	}

	@Override
	public boolean isSingular() {
		return true;
	}

}
