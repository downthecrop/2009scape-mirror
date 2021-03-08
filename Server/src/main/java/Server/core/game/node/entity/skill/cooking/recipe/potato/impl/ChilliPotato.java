package core.game.node.entity.skill.cooking.recipe.potato.impl;

import core.game.node.entity.skill.cooking.recipe.potato.PotatoRecipe;
import core.game.node.item.Item;

/**
 * Represents the chilli potato recipe. This recipe consists of adding chilli
 * con carne to a baked butter potato.
 * @author 'Vexia
 * @date 22/12/2013
 */
public class ChilliPotato extends PotatoRecipe {

	/**
	 * Represents the chilli potato item.
	 */
	private static final Item CHILLI_POTATO = new Item(7054);

	/**
	 * Represents the chilli con carne item/
	 */
	private static final Item CHILLI_CON_CARNE = new Item(7062);

	@Override
	public Item getProduct() {
		return CHILLI_POTATO;
	}

	@Override
	public Item[] getIngredients() {
		return new Item[] { CHILLI_CON_CARNE };
	}

	@Override
	public boolean isTopping() {
		return true;
	}

	@Override
	public int getLevel() {
		return 41;
	}

	@Override
	public double getExperience() {
		return 10;
	}

}
