package core.game.node.entity.skill.herblore;

import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;

/**
 * Handles the creation of a super combat potion.
 * @author Empathy
 *
 */
public class SuperCombatPulse extends SkillPulse<Item> {
	
	/**
	 * The ingredients.
	 */
	private static final int[] INGREDIENTS = new int[] { 2436, 2442, 2440, 269 };

	/**
	 * The product item.
	 */
	private static final Item PRODUCT = new Item(14871);
	
	/**
	 * The vial id.
	 */
	private static final Item VIAL = new Item(229);
	
	/**
	 * Represents the animation to use when making a potion.
	 */
	private static final Animation ANIMATION = new Animation(363);

	/**
	 * Represents the amount to make.
	 */
	private int amount;
	
	/**
	 * Constructs a new {@code SuperCombatPulse} object.
	 * @param player the player.
	 * @param node the node.
	 * @param amount the amount.
	 */
	public SuperCombatPulse(final Player player, final Item node, int amount) {
		super(player, node);
		this.amount = amount;
	}

	@Override
	public boolean checkRequirements() {
		if (player.getSkills().getStaticLevel(Skills.HERBLORE) < 90) {
			player.sendMessage("You need a Herblore level of at least 90 in order to do this");
			return false;
		}
		if (!player.getInventory().containsAll(INGREDIENTS)) {
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
			setDelay(2);
		}
		if (player.getInventory().containsAll(INGREDIENTS)) {
			if (player.getInventory().remove(new Item(INGREDIENTS[0]), new Item(INGREDIENTS[1]), new Item(INGREDIENTS[2]), new Item(INGREDIENTS[3]))) {
				VIAL.setAmount(2);
				player.getSkills().addExperience(Skills.HERBLORE, 150);
				player.animate(ANIMATION);
				player.getInventory().add(PRODUCT);
				player.getInventory().add(VIAL);
				VIAL.setAmount(1);
				amount--;
			}
		}
		return amount == 0;
	}

}
