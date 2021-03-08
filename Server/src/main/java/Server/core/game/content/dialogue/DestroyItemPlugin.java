package core.game.content.dialogue;

import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.audio.Audio;
import core.plugin.Initializable;
import core.game.node.item.Item;

/**
 * Represents the destroy item plugin.
 * @author Vexia
 * 
 */
@Initializable
public final class DestroyItemPlugin extends DialoguePlugin {

	/**
	 * Represents the sound to send for destroying an item.
	 */
	private static final Audio SOUND = new Audio(4500, 1, 0);

	/**
	 * Represents the item parameter.
	 */
	private Item item;

	/**
	 * Constructs a new {@code DestroyItemPlugin} {@code Object}.
	 */
	public DestroyItemPlugin() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code DestroyItemPlugin} {@code Object}.
	 * @param player the player.
	 */
	public DestroyItemPlugin(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new DestroyItemPlugin(player);
	}

	@Override
	public boolean open(Object... args) {
		item = (Item) args[0];
		interpreter.sendDestroyItem(item.getId(), item.getName());
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		if (buttonId == 2) {
			if (player.getInventory().remove(item)) {
				player.getAudioManager().send(SOUND);
				end();
			}
		}
		if (buttonId == 3) {
			end();
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 9878 };
	}

}
