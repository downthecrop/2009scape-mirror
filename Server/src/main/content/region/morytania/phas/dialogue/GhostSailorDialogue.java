package content.region.morytania.phas.dialogue;

import core.game.dialogue.DialoguePlugin;
import core.game.dialogue.FacialExpression;
import content.region.morytania.phas.handlers.PhasmatysZone;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;

/**
 * Represents the ghost sailor dialogue plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class GhostSailorDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code GhostSailorDialogue} {@code Object}.
	 */
	public GhostSailorDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code GhostSailorDialogue} {@code Object}.
	 * @param player the player.
	 */
	public GhostSailorDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new GhostSailorDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		if (PhasmatysZone.hasAmulet(player)) {
			player("Hi there. Why do you still bother having ships here? I", "mean - you're dead, what use are they to you?");
			stage = 1;
		} else {
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Woooo wooo wooooo woooo");
			stage = 10;
		}
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 1:
			npc("We keep ships because we still need trade in", "Phasmatys. Every trader that comes to Phastmatys is", "made to worship the Ectofuntus, so that the Ectopower", "doesn't run out.");
			stage++;
			break;
		case 2:
			player("So, without traders to worship in the Temple you're", "history right?");
			stage++;
			break;
		case 3:
			npc("Aye, matey.");
			stage++;
			break;
		case 4:
			end();
			break;
		case 10:
			interpreter.sendDialogue("You cannot understand the ghost.");
			stage = 11;
			break;
		case 11:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 1703, 1704 };
	}
}