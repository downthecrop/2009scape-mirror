package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for the camel npc.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class CamelDialoguePlugin extends DialoguePlugin {

	/**
	 * Constructs a new {@code CamelDialoguePlugin} {@code Object}.
	 */
	public CamelDialoguePlugin() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code CamelDialoguePlugin} {@code Object}.
	 * @param player the player.
	 */
	public CamelDialoguePlugin(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new CamelDialoguePlugin(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "If I go near that camel, it'll probably bite my hand off.");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			end();
			player.getPacketDispatch().sendMessage("The camel spits at you, and you jump back hurriedly.");
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 2813 };
	}
}
