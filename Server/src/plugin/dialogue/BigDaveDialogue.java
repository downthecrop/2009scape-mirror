package plugin.dialogue;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for big dave.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class BigDaveDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code BigDaveDialogue} {@code Object}.
	 */
	public BigDaveDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code BigDaveDialogue} {@code Object}.
	 * @param player the player.
	 */
	public BigDaveDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new BigDaveDialogue(player);
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
	public int[] getIds() {
		return new int[] { 228 };
	}
}
