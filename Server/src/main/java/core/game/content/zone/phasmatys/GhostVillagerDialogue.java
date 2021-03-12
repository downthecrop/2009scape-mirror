package core.game.content.zone.phasmatys;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;
import core.tools.RandomFunction;

/**
 * Handles the ghost villager dialogue.
 * @author Vexia
 */
public final class GhostVillagerDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code GhostVillagerDialogue} {@code Object}.
	 */
	public GhostVillagerDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code GhostVillagerDialogue} {@code Object}.
	 * @param player the player.
	 */
	public GhostVillagerDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new GhostVillagerDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		if (PhasmatysZone.hasAmulet(player)) {
			final int random = RandomFunction.random(10);
			switch (random) {
			default:
				npc("What do you want, mortal?");
				break;
			}
		} else {
			npc("Woooo wooo wooooo woooo");
			stage = 10;
		}
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			end();
			break;
		case 10:
			interpreter.sendDialogue("You cannot understand the ghost.");
			stage++;
			break;
		case 11:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 1697 };
	}

}
