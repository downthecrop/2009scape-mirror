package content.region.misthalin.barbvillage.stronghold;

import core.api.Container;
import core.game.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.emote.Emotes;
import org.rs09.consts.Items;

import static core.api.ContentAPIKt.addItem;

/**
 * Represents the dialogue plugin used for the box of health.
 * @author 'Vexia
 * @version 1.0
 */
public final class BoxOfHealthDialogue extends DialoguePlugin {

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
			if (!addItem(player, Items.COINS_995, 5000, Container.INVENTORY)) {
				player.getPacketDispatch().sendMessage("You don't have enough inventory space.");
				end();
				break;
			}
			stage = 1;
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