package content.global.skill.fletching.items.arrow;

import core.game.node.entity.player.Player;
import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import org.rs09.consts.Items;

import static core.api.ContentAPIKt.hasSpaceFor;
import static core.api.ContentAPIKt.sendDialogue;

/**
 * Represents the arrow pulse for creating unfinished ogre arrows.
 * @author 'Vexia
 */
public final class HeadlessOgreArrowPulse extends SkillPulse<Item> {

	/**
	 * Represents the headless ogre arrow item.
	 */
	private final Item HEADLESS_ARROW = new Item(Items.FLIGHTED_OGRE_ARROW_2865);

	/**
	 * Represents the ogre arrow shaft item.
	 */
	private final Item ARROW_SHAFT = new Item(Items.OGRE_ARROW_SHAFT_2864);

	/**
	 * Represents the feather items.
	 */
	private static final Item[] FEATHER = new Item[] {
			new Item(Items.FEATHER_314, 4),
	};

	/**
	 * The feather being used.
	 */
	private Item feather;

	/**
	 * Represents the amount to make.
	 */
	private int sets;

	/**
	 * Constructs a new {@code ArrowPulse.java} {@code Object}.
	 * @param player the player.
	 * @param node the node.
	 */
	public HeadlessOgreArrowPulse(Player player, Item node, Item feather, int sets) {
		super(player, node);
		this.sets = sets;
		this.feather = feather;
	}

	@Override
	public boolean checkRequirements() {
		if (!player.getInventory().containsItem(ARROW_SHAFT)) {
			player.getDialogueInterpreter().sendDialogue("You don't have any arrow shafts.");
			return false;
		}
		if (feather == null || !player.getInventory().containsItem(feather)) {
			player.getDialogueInterpreter().sendDialogue("You don't have any feathers.");
			return false;
		}
		if (!hasSpaceFor(player, HEADLESS_ARROW.asItem())) {
			sendDialogue(player, "You do not have enough inventory space.");
			return false;
		}
		return true;
	}

	@Override
	public void animate() {
	}

	@Override
	public boolean reward() {
		int featherAmount = player.getInventory().getAmount(feather);
		int shaftAmount = player.getInventory().getAmount(ARROW_SHAFT);
		if (getDelay() == 1) {
			super.setDelay(3);
		}
		if (featherAmount >= 24 && shaftAmount >= 6) {
			feather.setAmount(24);
			ARROW_SHAFT.setAmount(6);
			player.getPacketDispatch().sendMessage("You attach 24 feathers to 6 ogre arrow shafts.");
		} else {
			int amount = Math.min(featherAmount / 4, shaftAmount);
			feather.setAmount(amount*4);
			ARROW_SHAFT.setAmount(amount);
			player.getPacketDispatch().sendMessage(amount == 1
					? "You attach a feathers to a shaft." : "You attach " + amount * 4 + " feathers to " + amount + " ogre arrow shafts.");
		}
		if (player.getInventory().remove(feather, ARROW_SHAFT)) {
			HEADLESS_ARROW.setAmount(ARROW_SHAFT.getAmount());
			player.getSkills().addExperience(Skills.FLETCHING, HEADLESS_ARROW.getAmount(), true);
			player.getInventory().add(HEADLESS_ARROW);
		}
		HEADLESS_ARROW.setAmount(1);
		feather.setAmount(1);
		ARROW_SHAFT.setAmount(1);
		if (!player.getInventory().containsItem(ARROW_SHAFT)) {
			return true;
		}
		if (!player.getInventory().containsItem(feather)) {
			return true;
		}
		sets--;
		return sets <= 0;
	}

	@Override
	public void message(int type) {
	}

	/**
	 * Gets the feather item.
	 * @return the item.
	 */
	private Item getFeather() {
		int length = FEATHER.length;
		for (int i = 0; i < length; i++) {
			Item f = FEATHER[i];
			if (player.getInventory().containsItem(f)) {
				return f;
			}
		}
		return null;
	}
}
