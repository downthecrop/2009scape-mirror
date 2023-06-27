package content.region.asgarnia.falador.quest.blackknightsfortress;

import core.game.dialogue.DialoguePlugin;
import core.game.dialogue.FacialExpression;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.game.world.update.flag.context.Animation;

/**
 * Represents the dialogue of listening throug a grill during the black knights'
 * fortress quest.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class BKListenDialogue extends DialoguePlugin {

	/**
	 * Represents the cabbage item.
	 */
	private static final Item CABBAGE = new Item(1965);

	/**
	 * Represents the animation to use.
	 */
	private static final Animation ANIMATION = new Animation(832);

	/**
	 * Represents the captain npc.
	 */
	private final int captain = 610;

	/**
	 * Represents the witch npc.
	 */
	private final int witch = 611;

	/**
	 * Represents the goblin greldo npc.
	 */
	private final int greldo = 612;

	/**
	 * Represents the cat npc.
	 */
	private final int cat = 4607;

	/**
	 * Constructs a new {@code BKListenDialogue} {@code Object}.
	 */
	public BKListenDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code BKListenDialogue} {@code Object}.
	 * @param player the player.
	 */
	public BKListenDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new BKListenDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		if (args != null && args.length == 2) {
			interpreter.sendDialogues(witch, FacialExpression.NEUTRAL, "Where has Greldo got to with that magic cabbage!");
			stage = 10;
			player.animate(ANIMATION);
			return true;
		}
		interpreter.sendDialogues(captain, FacialExpression.ASKING, "So... how's the secret weapon coming along?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendDialogues(witch, FacialExpression.HAPPY, "The invincibility potion is almost ready...");
			stage = 1;
			break;
		case 1:
			interpreter.sendDialogues(witch, FacialExpression.HAPPY, "It's taken me FIVE YEARS, but it's almost ready.");
			stage = 2;
			break;
		case 2:
			interpreter.sendDialogues(witch, FacialExpression.NEUTRAL, "Greldo the Goblin here is just going to fetch the last", "ingredient for me.");
			stage = 3;
			break;
		case 3:
			interpreter.sendDialogues(witch, FacialExpression.NEUTRAL, "It's a special grown cabbage grown by my cousin", "Helda who lives in Draynor Manor.");
			stage = 4;
			break;
		case 4:
			interpreter.sendDialogues(witch, FacialExpression.NEUTRAL, "The soil there is slightly magical and it gives the", "cabbages slight magical properties....");
			stage = 5;
			break;
		case 5:
			interpreter.sendDialogues(witch, FacialExpression.AMAZED, "...not to mention the trees!");
			stage = 6;
			break;
		case 6:
			interpreter.sendDialogues(witch, FacialExpression.NEUTRAL, "Now remember Greldo, only a Draynor Manor", "cabbage will do! Don't get lazy and bring any old", "cabbage, THAT would ENTIRELY wreck the potion!");
			stage = 7;
			break;
		case 7:
			interpreter.sendDialogues(greldo, FacialExpression.OLD_NORMAL, "Yeth, Mithreth.");
			stage = 8;
			break;
		case 8:
			player.getQuestRepository().getQuest("Black Knights' Fortress").setStage(player, 20);
			end();
			break;
		case 10:
			interpreter.sendDialogues(captain, FacialExpression.ASKING, "What's that noise?");
			stage = 11;
			break;
		case 11:
			interpreter.sendDialogues(witch, FacialExpression.NEUTRAL, "Hopefully Greldo with the cabbage... yes, look her it", "co....NOOOOOoooo!");
			stage = 12;
			break;
		case 12:
			interpreter.sendDialogues(witch, FacialExpression.EXTREMELY_SHOCKED, "My potion!");
			stage = 13;
			break;
		case 13:
			interpreter.sendDialogues(captain, FacialExpression.WORRIED, "Oh boy, this doesn't look good!");
			stage = 14;
			break;
		case 14:
			interpreter.sendDialogues(cat, FacialExpression.EXTREMELY_SHOCKED, "Meow!");
			stage = 15;
			break;
		case 15:
			if (player.getInventory().remove(CABBAGE)) {
				player.getQuestRepository().getQuest("Black Knights' Fortress").setStage(player, 30);
				interpreter.sendDialogues(player, FacialExpression.HAPPY, "Looks like my work here is done. Seems like that's", "successfully sabotaged their little secret weapon plan.");
				stage = 16;
			}
			break;
		case 16:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 992752973 };
	}
}
