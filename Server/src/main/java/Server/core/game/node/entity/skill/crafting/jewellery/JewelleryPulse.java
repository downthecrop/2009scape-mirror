package core.game.node.entity.skill.crafting.jewellery;

import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.crafting.jewellery.JewelleryCrafting.JewelleryItem;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;

/**
 * Represents the pulse used to craft jewllery.
 * @author 'Vexia
 */
public final class JewelleryPulse extends SkillPulse<Item> {

	/**
	 * Represents the animation to use.
	 */
	private static final Animation ANIMATION = new Animation(3243);

	/**
	 * Represents the data of jewellery.
	 */
	private JewelleryItem type;

	/**
	 * Represents the amount to make.
	 */
	private int amount;

	/**
	 * Represents the ticks.
	 */
	private int ticks;

	/**
	 * Constructs a new {@code CraftJewellery.java} {@code Object}.
	 * @param player the player.
	 * @param node the node.
	 */
	public JewelleryPulse(Player player, Item node, JewelleryItem data, int amount) {
		super(player, node);
		this.type = data;
		this.amount = amount;
	}

	@Override
	public boolean checkRequirements() {
		if (player.getSkills().getLevel(Skills.CRAFTING) < type.getLevel()) {
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
		if (player.getInventory().remove(getItems())) {
			final Item item = new Item(type.getSendItem());
		    player.getInventory().add(item);
			player.getSkills().addExperience(Skills.CRAFTING, type.getExperience(), true);
		}
		amount--;
		return amount < 1;
	}

	/**
	 * Gets the items to remove.
	 * @return the items.
	 */
	private Item[] getItems() {
		Item items[] = new Item[type.getItems().length];
		int index = 0;
		for (int i = 0; i < type.getItems().length; i++) {
			items[index] = new Item(type.getItems()[i], 1);
			index++;
		}
		return items;
	}

}