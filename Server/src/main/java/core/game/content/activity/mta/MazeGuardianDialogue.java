package core.game.content.activity.mta;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;

import core.game.content.activity.mta.impl.TelekineticZone;

/**
 * Handles the maze guardian dialogue.
 * @author Vexia
 */
public class MazeGuardianDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code MazeGuardianDialogue} {@code Object}
	 */
	public MazeGuardianDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code MazeGuardianDialogue} {@code Object}
	 * @param player the player.
	 */
	public MazeGuardianDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new MazeGuardianDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		player("Hi!");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			npc("Well done on releasing me. Would you like to try", "another maze?");
			stage++;
			break;
		case 1:
			options("Yes please!", "No thanks.");
			stage++;
			break;
		case 2:
			switch (buttonId) {
			case 1:
				player("Yes please!");
				stage = 5;
				break;
			case 2:
				player("No thanks.");
				stage++;
				break;
			}
			break;
		case 3:
			npc("Very well. Talk to me if you want to move onto the", "next maze, or you can return to the entrance hall", "through the portal.");
			stage++;
			break;
		case 4:
			end();
			break;
		case 5:
			npc("Very well, I shall teleport you.");
			stage++;
			break;
		case 6:
			npc.clear();
			TelekineticZone.getZone(player).setUp();
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 3102 };
	}

}
