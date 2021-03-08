package core.game.node.entity.skill.fletching.items.grapple;

import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;

/**
 * Represents the skill pulse used to create a mith grapple.
 * @author 'Vexia
 * @date 21/12/2013
 */
public final class GrapplePulse extends SkillPulse<Item> {

	/**
	 * Represents the mith grapple tip.
	 */
	private static final Item MITH_GRAPPLE = new Item(9418);

	/**
	 * Represents the mithril grapple tio.
	 */
	private static final Item GRAPPLE_TIP = new Item(9416);

	/**
	 * Represents the mithril bolt.
	 */
	private static final Item MITHRIL_BOLT = new Item(9142);

	/**
	 * Represents the amount of the grapple to make.
	 */
	private int amount;

	/**
	 * Constructs a new {@code GrapplePulse} {@code Object}.
	 * @param player the player.
	 * @param node the node.
	 * @param amount the amount.
	 */
	public GrapplePulse(Player player, Item node, int amount) {
		super(player, node);
		this.amount = amount;
	}

	@Override
	public boolean checkRequirements() {
		int inventoryAmount = player.getInventory().getAmount(GRAPPLE_TIP);
		if (amount > inventoryAmount) {
			amount = inventoryAmount;
		}
		if (!player.getInventory().containsItem(GRAPPLE_TIP) || !player.getInventory().containsItem(MITHRIL_BOLT)) {
			return false;
		}
		if (player.getSkills().getLevel(Skills.FLETCHING) < 59) {
			player.getDialogueInterpreter().sendDialogue("You need a fletching level of at least 59 in order to do this.");
			return false;
		}
		return true;
	}

	@Override
	public void animate() {
	}

	@Override
	public boolean reward() {
		if (getDelay() == 1) {
			setDelay(3);
			return false;
		}
		if (player.getInventory().remove(GRAPPLE_TIP) && player.getInventory().remove(MITHRIL_BOLT)) {
			player.getInventory().add(MITH_GRAPPLE);
			player.getSkills().addExperience(Skills.FLETCHING, 5, true);
		}
		amount--;
		return amount < 1;
	}

	@Override
	public void message(int type) {

	}

}
