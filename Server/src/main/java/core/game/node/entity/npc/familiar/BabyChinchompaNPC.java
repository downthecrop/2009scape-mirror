package core.game.node.entity.npc.familiar;

import core.game.content.dialogue.DialoguePlugin;
import core.game.content.dialogue.FacialExpression;
import core.game.node.entity.npc.Metamorphosis;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * Handles the baby chinchompa pet. 
 * @author Empathy
 *
 */
@Initializable
public class BabyChinchompaNPC extends Metamorphosis {

	/**
	 * The chinchompa ids.
	 */
	private static final int[] CHINCHOMPA_IDS = new int[] { 8643, 8644, 8657, 8658 };

	/**
	 * Constructs a new {@code BabyChinchompaNPC} object.
	 */
	public BabyChinchompaNPC() {
		super(CHINCHOMPA_IDS);
	}
	
	@Override
	public DialoguePlugin getDialoguePlugin() {
		return new BabyChinchompaDialogue();
	}
	
	@Override
	public int getRandomNpcId() {
		int i = RandomFunction.getRandom(getIds().length - 1);
		if (getIds()[i] == 8658) {
			int x = RandomFunction.getRandom(30);
			if (x == 1) {
				return getIds()[i];
			} else {
				return getIds()[i-1];
			}
		}
		return getIds()[i];
	}

	/**
	 * Handles the BabyChinchompa Dialogue.
	 * @author Empathy
	 *
	 */
	public final class BabyChinchompaDialogue extends DialoguePlugin {

		/**
		 * Constructs a new {@code BabyChinchompaDialogue} {@code Object}.
		 */
		public BabyChinchompaDialogue() {
			/**
			 * empty.
			 */
		}

		/**
		 * Constructs a new {@code BabyChinchompaDialogue} {@code Object}.
		 * 
		 * @param player the player.
		 */
		public BabyChinchompaDialogue(Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new BabyChinchompaDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			npc = (NPC) args[0];
			interpreter.sendDialogues(npc, FacialExpression.OLD_NORMAL, npc.getId() != 8658 ? "Squeak! Squeak!" : "Squeaka! Squeaka!");
			stage = 0;
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			switch (stage) {
			case 0:
				end();
				break;
			}
			return true;
		}

		@Override
		public int[] getIds() {
			return new int[] { 8643, 8644, 8657, 8658 };
		}
	}
}
