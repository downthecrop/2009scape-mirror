package core.game.content.dialogue;

import core.game.node.entity.skill.Skills;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.game.world.update.flag.context.Animation;

/**
 * Represents the dialogue plugin used for making a dragon shield.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class DragonShieldDialogue extends DialoguePlugin {

	/**
	 * Represents the item shield parts.
	 */
	private static final Item[] SHIELD_PARTS = new Item[] { new Item(2366), new Item(2368) };

	/**
	 * Represents th edraconic visage item.
	 */
	private static final Item DRACONIC_VISAGE = new Item(11286);

	/**
	 * Represents the anti dragon fire shield.
	 */
	private static final Item ANTI_DRAGONSHIELD = new Item(1540);

	/**
	 * Represents the dragon fire shield item.
	 */
	private static final Item DRAGON_FIRESHIELD = new Item(11284);

	/**
	 * Represents the shield item.
	 */
	private static final Item SQ_SHIELD = new Item(1187);

	/**
	 * Represents the hammering animation.
	 */
	private static Animation ANIMATION = new Animation(898);

	/**
	 * Represents the shield type.
	 */
	private int type;

	/**
	 * Constructs a new {@code DragonShieldDialogue} {@code Object}.
	 */
	public DragonShieldDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code DragonShieldDialogue} {@code Object}.
	 * @param player the player.
	 */
	public DragonShieldDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new DragonShieldDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		type = (int) args[0];
		if (type == 1) {
			interpreter.sendDialogue("You set to work trying to fix the ancient shield. It's seen some", "heavy reward and needs some serious work doing to it.");
			stage = 0;
		} else {
			interpreter.sendDialogue("You set to work, trying to attach the ancient draconic", "visage to your anti-dragonbreath shield. It's not easy to", "work with the ancient artifact and it takes all of your", "skills as a master smith.");
			stage = 10;
		}
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			player.lock(5);
			player.animate(ANIMATION);
			interpreter.sendDialogue("Even for an experienced armourer it is not an easy task, but", "eventually it is ready. You have restored the dragon square shield to", "its former glory.");
			if (player.getInventory().remove(SHIELD_PARTS)) {
				player.getInventory().add(SQ_SHIELD);
			}
			player.getSkills().addExperience(Skills.SMITHING, 75, true);
			stage = 1;
			break;
		case 1:
			end();
			break;
		case 10:
			player.lock(5);
			player.animate(ANIMATION);
			interpreter.sendDialogue("Even for an experienced armourer it is not an easy task, but", "eventually it is ready. You have crafted the", "draconic visage and anti-dragonbreath shield into a", "dragonfire shield.");
			if (player.getInventory().remove(DRACONIC_VISAGE, ANTI_DRAGONSHIELD)) {
				player.getInventory().add(DRAGON_FIRESHIELD);
			}
			player.getSkills().addExperience(Skills.SMITHING, 2000);
			stage = 1;
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 82127843 };
	}
}
