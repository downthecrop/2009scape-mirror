package core.game.content.dialogue;

import core.plugin.Initializable;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;

/**
 * Represents the godsword dialogue plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class GodswordDialogue extends DialoguePlugin {

	/**
	 * Represents the animation to use.
	 */
	private static final Animation ANIMATION = new Animation(898);

	/**
	 * Represents the godsword blade item.
	 */
	private static final Item BLADE = new Item(11690);

	/**
	 * Represents the used item id.
	 */
	private int used;

	/**
	 * Constructs a new {@code GodswordDialogue} {@code Object}.
	 */
	public GodswordDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code GodswordDialogue} {@code Object}.
	 * @param player the player.
	 */
	public GodswordDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new GodswordDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		used = (int) args[0];
		interpreter.sendDialogue("You set to work, trying to fix the ancient sword.");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			boolean passBlade = true;
			int remove = -1;
			if (used == 11692 && player.getInventory().contains(11710, 1)) {
				passBlade = false;
				remove = 11710;
			}
			if (used == 11710 && player.getInventory().containItems(11692)) {
				passBlade = false;
				remove = 11692;
			}
			if (used == 11688 && player.getInventory().contains(11712, 1)) {
				passBlade = false;
				remove = 11712;
			}
			if (used == 11712 && player.getInventory().containItems(11688)) {
				passBlade = false;
				remove = 11688;
			}
			if (used == 11686 && player.getInventory().contains(11714, 1)) {
				passBlade = false;
				remove = 11714;
			}
			if (used == 11714 && player.getInventory().containItems(11686)) {
				passBlade = false;
				remove = 11686;
			}
			if (!passBlade) {
				if (player.getInventory().remove(new Item(used)) && player.getInventory().remove(new Item(remove))) {
					player.lock(5);
					player.animate(ANIMATION);
					interpreter.sendDialogue("Even for an experienced smith it is not an easy task, but eventually", "it is done.");
					player.getSkills().addExperience(Skills.SMITHING, 100, true);
					player.getInventory().add(BLADE);
				}
				return true;
			}
			int base = -1;
			if (used == 11710) {
				if (player.getInventory().contains(11712, 1)) {
					base = 11712;
				} else if (player.getInventory().contains(11714, 1)) {
					base = 11714;
				}
			}
			if (used == 11712) {
				if (player.getInventory().contains(11710, 1)) {
					base = 11710;
				} else if (player.getInventory().contains(11714, 1)) {
					base = 11714;
				}
			}
			if (used == 11714) {
				if (player.getInventory().contains(11712, 1)) {
					base = 11712;
				} else if (player.getInventory().contains(11710, 1)) {
					base = 11710;
				}
			}
			if (base == -1) {
				end();
				player.getPacketDispatch().sendMessage("You didn't have all the required items.");
				return true;
			}
			if (player.getInventory().remove(new Item(used)) && player.getInventory().remove(new Item(base))) {
				int shard = -1;
				if (used == 11710 && base == 11712 || used == 11712 && base == 11710) {
					shard = 11686;
				} else if (used == 11710 && base == 11714 || used == 11714 && base == 11710) {
					shard = 11688;
				}
				if (used == 11712 && base == 11714 || used == 11714 && base == 11712) {
					shard = 11692;
				}
				player.lock(5);
				player.animate(ANIMATION);
				interpreter.sendDialogue("Even for an experienced smith it is not an easy task, but eventually", "it is done.");
				player.getSkills().addExperience(Skills.SMITHING, 100);
				player.getInventory().add(new Item(shard));
			}
			stage = 1;
			break;
		case 1:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 62362 };
	}
}
