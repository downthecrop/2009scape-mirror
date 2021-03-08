package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the lumbridge swamp monk.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class LumbridgeSwampMonk extends DialoguePlugin {

	/**
	 * Constructs a new {@code LumbridgeSwampMonk} {@code Object}.
	 */
	public LumbridgeSwampMonk() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code LumbridgeSwampMonk} {@code Object}.
	 * @param player the player.
	 */
	public LumbridgeSwampMonk(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new LumbridgeSwampMonk(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Why are all of you standing around here?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "None of your business. Get lost.");
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
		return new int[] { 651 };
	}

}
