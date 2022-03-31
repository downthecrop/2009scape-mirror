package core.game.interaction.npc;

import core.cache.def.impl.NPCDefinition;
import core.plugin.Initializable;
import core.game.content.dialogue.DialoguePlugin;
import core.game.content.dialogue.FacialExpression;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;

import core.game.node.entity.skill.herblore.PotionDecantingPlugin;

/**
 * Handles the zahur options.
 * @author Empathy
 *
 */
@Initializable
public class ZahurOptionPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.forId(3037).getHandlers().put("option:combine", this);
		ClassScanner.definePlugin(new ZahurDialoguePlugin());
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		PotionDecantingPlugin.decant(player);
		player.getDialogueInterpreter().sendDialogues(node.asNpc(), FacialExpression.HALF_GUILTY, "There, all done.");
		return true;
	}

	
	/**
	 * Handles the Zahur Dialogue.
	 * @author Empathy
	 *
	 */
	public class ZahurDialoguePlugin extends DialoguePlugin {
		
		/**
		 * Constructs a new {@code ZahurDialoguePlugin} {@code Object}.
		 */
		public ZahurDialoguePlugin() {
			/**
			 * empty.
			 */
		}

		/**
		 * Constructs a new {@code ZahurDialoguePlugin} {@code Object}.
		 * @param player the player.
		 */
		public ZahurDialoguePlugin(Player player) {
			super(player);
		}
		
		@Override
		public DialoguePlugin newInstance(Player player) {
			return new ZahurDialoguePlugin(player);
		}

		@Override
		public boolean open(Object... args) {
			npc("I can combine your potion vials to try and make", "the potions fit into fewer vials. This service is free.", "Would you like to do this?");
			stage = 1;
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			switch (stage) {
			case 1:
				options("Yes", "No");
				stage = 2;
				break;
			case 2:
				if (buttonId == 1) {
					PotionDecantingPlugin.decant(player);
					npc("There, all done.");
				}
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
			return new int[] { 3037 };
		}
		
	}
}
