package core.game.node.entity.npc.familiar;

import core.plugin.Initializable;
import core.game.content.dialogue.DialoguePlugin;
import core.game.content.dialogue.FacialExpression;
import core.game.node.entity.npc.Metamorphosis;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;

/**
 * Handles the metamorphosis of the zulrah pet.
 * @author Empathy
 *
 */
@Initializable
public class SnakelingNPC extends Metamorphosis {

	/**
	 * The snakeling ids.
	 */
	public static final int[] SNAKELING_IDS = new int[] { 8626, 8627, 8628 };
	
	/**
	 * 
	 * Constructs a new {@code SnakelingNPC} object.
	 */
	public SnakelingNPC() {
		super(SNAKELING_IDS);
	}
	
	@Override
	public DialoguePlugin getDialoguePlugin() {
		return new PetSnakelingDialogue();
	}
	
	/**
	 * Handles the pet snakeling dialogue.
	 * @author Empathy
	 *
	 */
	public final class PetSnakelingDialogue extends DialoguePlugin {

		/**
		 * Constructs a new {@code PetSnakelingDialogue} {@code Object}.
		 */
		public PetSnakelingDialogue() {
			/**
			 * empty.
			 */
		}

		/**
		 * Constructs a new {@code PetSnakelingDialogue} {@code Object}.
		 * 
		 * @param player the player.
		 */
		public PetSnakelingDialogue(Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new PetSnakelingDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			npc = (NPC) args[0];
			player("Hey little snake!");
			stage = 0;
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			switch (stage) {
			case 0:
				npc(FacialExpression.OLD_NORMAL, "Soon, Zulrah shall establish dominion over this plane.");
				stage = 1;
				break;
			case 1:
				player("Wanna play fetch?");
				stage = 2;
				break;
			case 2:
				npc(FacialExpression.OLD_NORMAL, "Submit to the almighty Zulrah.");
				stage = 3;
				break;
			case 3:
				player("Walkies? Or slidies...?");
				stage = 4;
				break;
			case 4:
				npc(FacialExpression.OLD_NORMAL, "Zulrah's wilderness as a God will soon be demonstrated.");
				stage = 5;
				break;
			case 5:
				player("I give up...");
				stage = 6;
				break;
			case 6:
				end();
				break;
			}
			return true;
		}

		@Override
		public int[] getIds() {
			return SNAKELING_IDS;
		}
	}

}
