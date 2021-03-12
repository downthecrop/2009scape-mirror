package core.game.content.activity.stronghold;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.emote.Emotes;
import core.game.node.item.Item;

/**
 * Represents the dialogue plugin used for the box of health.
 * @author 'Vexia
 * @version 1.0
 */
public final class BoxOfHealthDialogue extends DialoguePlugin {

	/**
	 * Represents the coins to recieve.
	 */
	private static final Item COINS = new Item(995, 5000);

	/**
	 * Constructs a new {@code BoxOfHealth} {@code Object}.
	 */
	public BoxOfHealthDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code BoxOfHealth} {@code Object}.
	 * @param player the player.
	 */
	public BoxOfHealthDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new BoxOfHealthDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		interpreter.sendDialogue("The box hinges creak and appear to be forming audible words....");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			if (player.getInventory().freeSlots() == 0) {
				player.getPacketDispatch().sendMessage("You don't have enough inventory space.");
				end();
				break;
			}
			stage = 1;
			player.getInventory().add(COINS);
			interpreter.sendDialogue("...congratulations adventurer, you have been deemed worthy of this", "reward. You have also unlocked the Idea emote!");
			player.getEmoteManager().unlock(Emotes.IDEA);
			player.getSavedData().getGlobalData().getStrongHoldRewards()[2] = true;
			break;
		case 1:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 96878 };
	}

}