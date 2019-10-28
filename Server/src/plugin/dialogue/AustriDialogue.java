package plugin.dialogue;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for austri.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class AustriDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code AustriDialogue} {@code Object}.
	 */
	public AustriDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code AustriDialogue} {@code Object}.
	 * @param player the player.
	 */
	public AustriDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new AustriDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		npc("What about my size?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			player("Erm... What?");
			stage = 1;
			break;
		case 1:
			npc("Stupid human.");
			stage = 2;
			break;
		case 2:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 232 };
	}
}
