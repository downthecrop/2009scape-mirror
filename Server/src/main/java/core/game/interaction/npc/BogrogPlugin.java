package core.game.interaction.npc;

import core.cache.def.impl.NPCDefinition;
import core.game.component.Component;
import core.game.container.access.InterfaceContainer;
import core.plugin.Initializable;
import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.skill.Skills;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;

/**
 * Handles the bogrog npc.
 * @author Vexia
 */
@Initializable
public final class BogrogPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.forId(4472).getHandlers().put("option:swap", this);
		ClassScanner.definePlugin(new BogrogDialogue());
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (option) {
		case "swap":
			openSwap(player);
			break;
		}
		return true;
	}

	/**
	 * Opens the swap interface.
	 * @param player the player.
	 */
	private void openSwap(Player player) {
		if (player.getSkills().getStaticLevel(Skills.SUMMONING) < 21) {
			player.sendMessage("You need a Summoning level of at least 21 in order to do that.");
			return;
		} else {
			InterfaceContainer.generateItems(player,player.getInventory().toArray(),new String[] {"Swap X","Swap 10","Swap 5","Swap 1","Value"}, 644,0,7,4,200);
			player.getInterfaceManager().openSingleTab(new Component(644));
		}
	}

	/**
	 * Handles the bogrog dialogue.
	 * @author Vexia
	 */
	public final class BogrogDialogue extends DialoguePlugin {

		/**
		 * Constructs a new {@Code BogrogDialogue} {@Code Object}
		 */
		public BogrogDialogue() {
			/**
			 * empty.
			 */
		}

		/**
		 * Constructs a new {@Code BogrogDialogue} {@Code Object}
		 * @param player the player.
		 */
		public BogrogDialogue(Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new BogrogDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			npc = (NPC) args[0];
			npc("Hey, yooman, what you wanting?");
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			switch (stage) {
			case 0:
				options("Can I buy some summoning supplies?", "Are you interested in buying pouch pouches or scrolls?");
				stage++;
				break;
			case 1:
				switch (buttonId) {
				case 1:
					player("Can I buy some summoning supplies?");
					stage = 10;
					break;
				case 2:
					player("Are you interested in buying pouch pouches or scrolls?");
					stage = 20;
					break;
				}
				break;
			case 10:
				npc("Hur, hur, hur! Yooman's gotta buy lotsa stuff if yooman", "wants ta train good!");
				stage++;
				break;
			case 11:
				npc.openShop(player);
				end();
				break;
			case 20:
				npc("Des other ogre's stealin' Bogrog's stock. Gimmie pouches", "and scrolls and yooman gets da shardies.");
				stage++;
				break;
			case 21:
				player("Ok.");
				stage++;
				break;
			case 22:
				openSwap(player);
				end();
				break;
			}
			return true;
		}

		@Override
		public int[] getIds() {
			return new int[] { 4472 };
		}
	}
}
