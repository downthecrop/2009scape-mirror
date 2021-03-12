package core.game.content.activity.mta;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;

/**
 * Handles the progress hat dialogue.
 * @author Vexia
 */
public class ProgressHatDialogue extends DialoguePlugin {

	/**
	 * The progress hat.
	 */
	private Item progressHat;

	/**
	 * Constructs a new {@Code ProgressHatDialogue} {@Code Object}
	 */
	public ProgressHatDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@Code ProgressHatDialogue} {@Code Object}
	 * @param player the player.
	 */
	public ProgressHatDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new ProgressHatDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		if (args.length > 1) {
			progressHat = (Item) args[0];
			npc("How dare you destroy me? You'll lose your Pizazz", "Points!");
			stage = 50;
			return true;
		}
		player("Mr Progress Hat... sir?");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			npc("Can't you see I'm busy?");
			stage++;
			break;
		case 1:
			player("But you're just a hat? Can you tell me my Pizazz", "Point totals?");
			stage++;
			break;
		case 2:
			npc("Ok, I suppose it's my job. You have:", getPoints(0) + " Telekinetic, " + getPoints(1) + " Alchemist,", getPoints(2) + " Enchantment, and " + getPoints(3) + " Graveyard Pizazz Points.");
			stage++;
			break;
		case 3:
			player("Thank you!");
			stage++;
			break;
		case 4:
			end();
			break;
		case 50:
			interpreter.sendOptions("Destroy Hat?", "Yes", "No");
			stage++;
			break;
		case 51:
			switch (buttonId) {
			case 1:
				if (progressHat == null) {
					end();
					return true;
				}
				if (!player.getInventory().containsItem(progressHat)) {
					end();
					return true;
				}
				player.getInventory().remove(progressHat);
				interpreter.sendDialogue("The hat whispers as you destroy it. You can get another from the", "Entrance Guardian.");
				stage = 4;
				break;
			case 2:
				npc("I think so too!");
				stage = 4;
				break;
			}
			break;
		}
		return true;
	}

	/**
	 * Gets the pizzaz points.
	 * @param index the index.
	 * @return the points.
	 */
	public int getPoints(int index) {
		return player.getSavedData().getActivityData().getPizazzPoints(index);
	}

	@Override
	public int[] getIds() {
		return new int[] { 3096 };
	}

}
