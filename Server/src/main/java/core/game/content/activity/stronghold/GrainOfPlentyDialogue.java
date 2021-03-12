package core.game.content.activity.stronghold;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.emote.Emotes;
import core.game.node.item.Item;

/**
 * Represents the grain of plenty dialogue plugin.
 * @author 'Vexia
 * @version 1.0
 */
public final class GrainOfPlentyDialogue extends DialoguePlugin {

	/**
	 * Represents the coins item.
	 */
	private static final Item COINS = new Item(995, 3000);

	/**
	 * Constructs a new {@code GrainOfPlenty} {@code Object}.
	 */
	public GrainOfPlentyDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code GrainOfPlenty} {@code Object}.
	 * @param player the player.
	 */
	public GrainOfPlentyDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new GrainOfPlentyDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		interpreter.sendDialogue("The grain shifts in the sack, sighing audible words....");
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
			player.getSavedData().getGlobalData().getStrongHoldRewards()[1] = true;
			interpreter.sendDialogue("...congratualtions adventurer, you have been deemed worthy of this", "reward. You have also unlocked the Slap Head emote!");
			stage = 1;
			player.getInventory().add(COINS);
			player.getEmoteManager().unlock(Emotes.SLAP_HEAD);
			break;
		case 1:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 56875 };
	}
}