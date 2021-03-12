package core.game.content.dialogue;

import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * @author 'Vexia
 */
@Initializable
public class SkullSceptreDialogue extends DialoguePlugin {

	public SkullSceptreDialogue() {

	}

	public SkullSceptreDialogue(Player player) {
		super(player);
	}

	@Override
	public int[] getIds() {
		return new int[] { 78489 };
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		end();
		return true;
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new SkullSceptreDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		interpreter.sendItemMessage(9009, "The two halves of the skull fit perfectly.");
		if (args.length == 1) {
			interpreter.sendItemMessage(9012, "The two halves of the sceptre fit perfectly.");// The
			// Sceptre", "appears
			// to
			// be
			// designed
			// to
			// have
			// something
			// on
			// top.");
			return true;
		}
		if (args.length == 2) {
			interpreter.sendItemMessage(9013, "The skull fits perfectly atop the Sceptre.");// The
			// Sceptre", "appears
			// to
			// be
			// designed
			// to
			// have
			// something
			// on
			// top.");
			return true;
		}
		stage = 0;
		return true;
	}

}
