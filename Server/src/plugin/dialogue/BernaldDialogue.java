package plugin.dialogue;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for the bernald npc.
 * @author 'Vexia
 * @version 1.1
 */
@InitializablePlugin
public final class BernaldDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code BernaldDialogue} {@code Object}.
	 */
	public BernaldDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code BernaldDialogue} {@code Object}.
	 * @param player the player.
	 */
	public BernaldDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new BernaldDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		player("How are your grapes coming along?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			npc("Marvellous, thanks to your help, " + player.getUsername() + "!");
			stage = 1;
			break;
		case 1:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 2580 };
	}
}
