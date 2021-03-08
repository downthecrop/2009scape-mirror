package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the magic store dialogue plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class MagicStoreDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code MagicStoreDialogue} {@code Object}.
	 * @param player the player.
	 */
	public MagicStoreDialogue(final Player player) {
		super(player);
	}

	/**
	 * Constructs a new {@code MagicStoreDialogue} {@code Object}.
	 */
	public MagicStoreDialogue() {
		/**
		 * empty.
		 */
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new MagicStoreDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		npc("Welcome to the Magic Guild Store. Would you like to", "buy some magic supplies?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			options("Yes please.", "No thank you.");
			stage = 1;
			break;
		case 1:
			switch (buttonId) {
			case 1:
				player("Yes please.");
				stage = 10;
				break;
			case 2:
				end();
				break;
			}
			break;
		case 10:
			end();
			npc.openShop(player);
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 461 };
	}

}
