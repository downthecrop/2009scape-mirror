package content.global.skill.smithing;

import content.global.skill.skillcapeperks.SkillcapePerks;
import core.api.Container;
import core.game.dialogue.DialoguePlugin;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs09.consts.Items;

import static core.api.ContentAPIKt.*;

/**
 * Represents the dialogue plugin used for making a dragon shield.
 * @author 'Vexia
 * @author Player Name
 * @version 1.1
 */
@Initializable
public final class DragonShieldDialogue extends DialoguePlugin {
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
		if (!inInventory(player, Items.HAMMER_2347, 1) && !SkillcapePerks.isActive(SkillcapePerks.BAREFISTED_SMITHING, player)) {
			interpreter.sendDialogue("You need a hammer to work the metal with.");
		}
        int type = (int) args[0];
		if (type == 1) {
			if (!(inInventory(player, Items.SHIELD_RIGHT_HALF_2368, 1) && inInventory(player, Items.SHIELD_LEFT_HALF_2366, 1))) {
				interpreter.sendDialogue("You need the other half of the shield."); //todo authentic message
				return false;
			}
			interpreter.sendDialogue("You set to work trying to fix the ancient shield. It's seen some", "heavy action and needs some serious work doing to it.");
			stage = 0;
		} else {
			if (!inInventory(player, Items.ANTI_DRAGON_SHIELD_1540, 1)) {
				interpreter.sendDialogue("You need an anti-dragon shield to attach the visage to."); //todo authentic message
				return false;
			}
			if (!inInventory(player, Items.DRACONIC_VISAGE_11286, 1)) {
				interpreter.sendDialogue("You don't have anything you could attach to the shield."); //todo authentic message
				return false;
			}
			interpreter.sendDialogue("You set to work, trying to attach the ancient draconic", "visage to your anti-dragonbreath shield. It's not easy to", "work with the ancient artifact and it takes all of your", "skills as a master smith.");
			stage = 10;
		}
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			lock(player, 5);
			animate(player, 898, false);
			if (removeItem(player, Items.SHIELD_RIGHT_HALF_2368, Container.INVENTORY) && removeItem(player, Items.SHIELD_LEFT_HALF_2366, Container.INVENTORY)) {
				interpreter.sendDialogue("Even for an experienced armourer it is not an easy task, but", "eventually it is ready. You have restored the dragon square shield to", "its former glory.");
				addItem(player, Items.DRAGON_SQ_SHIELD_1187, 1, Container.INVENTORY);
				rewardXP(player, Skills.SMITHING, 75);
			}
			stage = 1;
			break;
		case 1:
			end();
			break;
		case 10:
			lock(player, 5);
			animate(player, 898, false);
			if (removeItem(player, Items.ANTI_DRAGON_SHIELD_1540, Container.INVENTORY) && removeItem(player, Items.DRACONIC_VISAGE_11286, Container.INVENTORY)) {
				interpreter.sendDialogue("Even for an experienced armourer it is not an easy task, but", "eventually it is ready. You have crafted the", "draconic visage and anti-dragonbreath shield into a", "dragonfire shield.");
				addItem(player, Items.DRAGONFIRE_SHIELD_11284, 1, Container.INVENTORY);
				rewardXP(player, Skills.SMITHING, 2000);
			}
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
