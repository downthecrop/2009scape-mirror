package content.region.karamja.dialogue;

import core.game.dialogue.DialoguePlugin;
import core.game.dialogue.FacialExpression;
import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the karamja man dialogue plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class KaramajaManDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code KaramajaManDialogue} {@code Object}.
	 */
	public KaramajaManDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code KaramajaManDialogue} {@code Object}.
	 * @param player the player.
	 */
	public KaramajaManDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new KaramajaManDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(player, FacialExpression.HAPPY, "Hello, how's it going?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Not too bad, but I'm a little worried about the increase", "of goblins these days.");
			stage = 1;
			break;
		case 1:
			interpreter.sendDialogues(player, FacialExpression.HAPPY, "Don't worry, I'll kill them.");
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
		return new int[] { 3915 };
	}
}
