package plugin.dialogue;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.node.entity.player.Player;

/**
 * Rerpesents the dialogue plugin used for the bonzo npc.
 * @author 'Vexia
 * @note finish with fishing contests.
 * @version 1.0
 */
@InitializablePlugin
public final class BonzoDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code BonzoDialogue} {@code Object}.
	 */
	public BonzoDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code BonzoDialogue} {@code Object}.
	 * @param player
	 */
	public BonzoDialogue(Player player) {
		super(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		end();
		player.getPacketDispatch().sendMessage("He doesn't seem interested in talking to you.");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {

		}
		return true;
	}

	@Override
	public DialoguePlugin newInstance(Player player) {

		return new BonzoDialogue(player);
	}

	@Override
	public int[] getIds() {
		return new int[] { 225 };
	}
}
