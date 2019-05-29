package plugin.dialogue;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.world.update.flag.context.Animation;

/**
 * Represents the banana crate dialogue.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class BananaCrateDialogue extends DialoguePlugin {

	/**
	 * Represents the dialogue id.
	 */
	public static final int ID = 9682749;

	/**
	 * Represents the animation to use.
	 */
	private static final Animation ANIMATION = new Animation(832);

	/**
	 * Represents the banana item.
	 */
	private static final Item BANANA = new Item(1963);

	/**
	 * Constructs a new {@code BananaCrateDialogue} {@code Object}.
	 */
	public BananaCrateDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code BananaCrateDialogue} {@code Object}.
	 * @param player the player.
	 */
	public BananaCrateDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new BananaCrateDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		interpreter.sendOptions("Do you want to take a banana?", "Yes.", "No.");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			switch (buttonId) {
			case 1:
				if (player.getInventory().add(BANANA)) {
					player.animate(ANIMATION);
					player.getPacketDispatch().sendMessage("You take a banana.");
				}
				end();
				break;
			case 2:
				end();
				break;
			}
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { ID };
	}
}
