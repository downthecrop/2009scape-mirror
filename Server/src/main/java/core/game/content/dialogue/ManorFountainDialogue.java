package core.game.content.dialogue;

import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.player.Player;
import core.game.node.item.GroundItemManager;
import core.plugin.Initializable;
import core.game.node.item.Item;

/**
 * Represents the dialogue that handles the draynor manor fountain.
 * @author 'Vexia
 * @date 24/12/2013
 */
@Initializable
public class ManorFountainDialogue extends DialoguePlugin {

	/**
	 * Represents the dialogue id of the fountain dialogue.
	 */
	public static final int DIALOGUE_ID = 3954922;

	/**
	 * Represents the pressure gauge item.
	 */
	private static final Item PRESSURE_GAUGE = new Item(271);

	/**
	 * Constructs a new {@code ManorFountainDialogue} {@code Object}.
	 */
	public ManorFountainDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code ManorFountainDialogue} {@code Object}.
	 * @param player the player.
	 */
	public ManorFountainDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new ManorFountainDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		if (player.getAttribute("pressure-gauge", false) && player.getInventory().containsItem(PRESSURE_GAUGE)) {
			interpreter.sendDialogues(player, null, "It's full of dead fish!");
			stage = 5;
			return true;
		}
		interpreter.sendDialogues(player, null, "There seems to be a pressure gauge in here...");
		stage = player.getAttribute("piranhas-killed", false) ? 3 : 1;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 1:
			player.getImpactHandler().manualHit(player, 1, HitsplatType.NORMAL);
			player.sendChat("Ow!");
			interpreter.sendDialogues(player, null, "... and a lot of piranhas!", "I can't get the guage out.");
			stage = 2;
			break;
		case 2:
			end();
			break;
		case 3:
			interpreter.sendDialogues(player, null, "... and a lot of dead fish.");
			stage = 4;
			break;
		case 4:
			if (!player.getInventory().add(PRESSURE_GAUGE)) {
				GroundItemManager.create(PRESSURE_GAUGE, player);
			}
			player.getPacketDispatch().sendMessage("You get the pressure gauge from the fountain.");
			player.setAttribute("/save:pressure-gauge", true);
			end();
			break;
		case 5:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 3954922 };
	}
}
