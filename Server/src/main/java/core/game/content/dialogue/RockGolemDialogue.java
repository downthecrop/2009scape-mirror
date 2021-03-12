package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Handles the rock golem dialgoue.
 * @author Empathy
 *
 */
@Initializable
public final class RockGolemDialogue extends DialoguePlugin {

		/**
		 * Constructs a new {@code RockGolemDialogue} {@code Object}.
		 */
		public RockGolemDialogue() {
			/**
			 * empty.
			 */
		}

		/**
		 * Constructs a new {@code RockGolemDialogue} {@code Object}.
		 * 
		 * @param player the player.
		 */
		public RockGolemDialogue(Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new RockGolemDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			npc = (NPC) args[0];
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "So you're made entirely of rocks?");
			stage = 0;
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			switch (stage) {
			case 0:
				interpreter.sendDialogues(npc, FacialExpression.OLD_NORMAL, "Not quite, my body is formed mostly of minerals.");
				stage = 1;
				break;
			case 1:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Aren't minerals just rocks?");
				stage = 2;
				break;
			case 2:				
				interpreter.sendDialogues(npc, FacialExpression.OLD_NORMAL, "No, rocks are rocks, minerals are minerals. I am", "formed from minerals.");
				stage = 3;
				break;
			case 3:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "But you're a Rock Golem...");
				stage = 4;
				break;
			case 4:
				end();
				break;
			}
			return true;
		}

		@Override
		public int[] getIds() {
			return new int[] { 8637 };
		}
	}
