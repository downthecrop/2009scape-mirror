package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used to handle the anton npc.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class AntonDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code AntonDialogue} {@code Object}.
	 */
	public AntonDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code AntonDialogue} {@code Object}.
	 * @param player the player.
	 */
	public AntonDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new AntonDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		npc("Ahhh, hello there. How can I help?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			player("Looks like you have a good", "selection of weapons around here...");
			stage = 1;
			break;
		case 1:
			npc("Indeed so, specially imported from the finest smiths", "around the lands, take a look at my wares.");
			stage = 2;
			break;
		case 2:
			end();
			npc.openShop(player);
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 4295 };
	}
}
