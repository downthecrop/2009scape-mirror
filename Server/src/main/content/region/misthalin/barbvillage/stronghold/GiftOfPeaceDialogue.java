package content.region.misthalin.barbvillage.stronghold;

import core.api.Container;
import core.game.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.emote.Emotes;
import core.game.node.item.Item;
import org.rs09.consts.Items;

import static core.api.ContentAPIKt.addItem;

/**
 * Represents the gift of peace dialogue plugin.
 * @author 'Vexia
 * @version 1.0
 */
public final class GiftOfPeaceDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code GiftOfPeaceDialogue} {@code Object}.
	 */
	public GiftOfPeaceDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code GiftOfPeaceDialogue} {@code Object}.
	 * @param player the player.
	 */
	public GiftOfPeaceDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new GiftOfPeaceDialogue(player);
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
			if (!addItem(player, Items.COINS_995, 2000, Container.INVENTORY)) {
				player.getPacketDispatch().sendMessage("You don't have enough inventory space.");
				end();
				break;
			}
			interpreter.sendDialogue("...congratulations adventurer, you have been deemed worthy of this", "reward. You have also unlocked the Flap emote!");
			stage = 1;
			player.getEmoteManager().unlock(Emotes.FLAP);
			player.getSavedData().getGlobalData().getStrongHoldRewards()[0] = true;
			break;
		case 1:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 54678 };
	}
}