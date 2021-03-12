package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the lumbridge swamp archer dialogue.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class LumbridgeSwampArcher extends DialoguePlugin {

	/**
	 * Constructs a new {@code LumbridgeSwampArcher} {@code Object}.
	 */
	public LumbridgeSwampArcher() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code LumbridgeSwampArcher} {@code Object}.
	 * @param player the player.
	 */
	public LumbridgeSwampArcher(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new LumbridgeSwampArcher(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Why are you guys hanging around here?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "(ahem)...'Guys'?");
			stage = 1;
			break;
		case 1:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Uh... yeah, sorry about that. Why are you all standing", "around out here?");
			stage = 2;
			break;
		case 2:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Well, that's really none of your business.");
			stage = 3;
			break;
		case 3:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 649 };
	}
}
