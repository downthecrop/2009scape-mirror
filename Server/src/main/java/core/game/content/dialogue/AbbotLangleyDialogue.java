package core.game.content.dialogue;

import core.plugin.Initializable;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;

/**
 * Represents the abbot langley dialogue.
 * @author Vexia
 * @version 1.0
 */
@Initializable
public final class AbbotLangleyDialogue extends DialoguePlugin {

	/**
	 * Represents the healing animation to use for the npc.
	 */
	private static final Animation ANIMATION = new Animation(717);

	/**
	 * Represents the healing craphics to use for the npc.
	 */
	private static final Graphics GRAPHICS = new Graphics(84);

	/**
	 * Constructs a new {@code AbbotLangleyDialogue} {@code Object}.
	 */
	public AbbotLangleyDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code AbbotLangleyDialogue} {@code Object}.
	 * @param player the player.
	 */
	public AbbotLangleyDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new AbbotLangleyDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		if (args.length >= 1) {
			if (args[0] instanceof NPC) {
				npc = (NPC) args[0];
			} else {
				npc("Only members of our order can go up there.");
				stage = 23;
				return true;
			}
		}
		npc("Greetings traveller.");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			if (player.getSavedData().getGlobalData().isJoinedMonastery()) {
				interpreter.sendOptions("Select an Option", "Can you heal me? I'm injured.", "Isn't this place built a bit out the way?");
			} else {
				interpreter.sendOptions("Select an Option", "Can you heal me? I'm injured.", "Isn't this place built a bit out the way?", "How do I get further into the monastery?");
			}
			stage = 1;
			break;
		case 1:
			switch (buttonId) {
			case 1:
				player("Can you heal me? I'm injured.");
				stage = 10;
				break;
			case 2:
				player("Isn't this place built a bit out of the way?");
				stage = 20;
				break;
			case 3:
				player("How do I get further into the monastery?");
				stage = 30;
				break;
			}
			break;
		case 10:
			npc("Ok.");
			stage = 11;
			break;
		case 11:
			player.getSkills().heal(((int) (player.getSkills().getStaticLevel(Skills.HITPOINTS) * 0.20)));
			npc.animate(ANIMATION);
			npc.graphics(GRAPHICS);
			interpreter.sendDialogue("Abbot Langley places his hands on your head. You feel a little better.");
			stage = 12;
			break;
		case 12:
			end();
			break;
		case 20:
			npc("We like it that way actually! We get disturbed less. We still", "get rather a large amount of travellers looking for", "sanctuary and healing here as it is!");
			stage = 21;
			break;
		case 21:
			end();
			break;
		case 22:
			npc("Only members of our order can go up there.");
			stage++;
			break;
		case 23:
			options("Well can I join your order?", "Oh, sorry.");
			stage++;
			break;
		case 24:
			switch (buttonId) {
			case 1:
				player("Well can I join your order?");
				stage = 26;
				break;
			case 2:
				player("Oh, sorry.");
				stage = 25;
				break;
			}
			break;
		case 25:
			end();
			break;
		case 26:
			if (player.getSkills().getStaticLevel(Skills.PRAYER) < 31) {
				npc("No. I am sorry, but I feel you are not devout enough.");
				stage++;
			} else {
				npc("Ok, I see you are someone suitable for our order. You", "may join.");
				stage = 31;
			}
			break;
		case 27:
			player.getPacketDispatch().sendMessage("You need a prayer level of 31 to join the order.");
			end();
			break;
		case 30:
			npc("I'm sorry but only members of our order are allowed", "in the second level of the monastery.");
			stage = 23;
			break;
		case 31:
			player.getSavedData().getGlobalData().setJoinedMonastery(true);
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 801 };
	}
}
