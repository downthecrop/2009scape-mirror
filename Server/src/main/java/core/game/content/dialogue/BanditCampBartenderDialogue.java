package core.game.content.dialogue;

import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Handles the Bandit Camp Bartender dialogue
 * @author Aero
 * @version 1.0
 */
@Initializable
public class BanditCampBartenderDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code BanditCampBartender} {@code Object}.
	 */
	public BanditCampBartenderDialogue() {
		/**
		 * Empty to stop instantiation.
		 */
	}

	/**
	 * Constructs a new {@code BanditCampBartender} {@code Object}.
	 * @param player The player to construct the class for.
	 */
	public BanditCampBartenderDialogue(final Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new BanditCampBartenderDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 1921 };
	}

}
