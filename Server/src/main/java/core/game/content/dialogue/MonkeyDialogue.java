package core.game.content.dialogue;

import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * The dialogue for the desert monkey.
 * @author 'Vexia
 */
@Initializable
public final class MonkeyDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code MonkeyDialogue} {@code Object}.
	 */
	public MonkeyDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code MonkeyDialogue} {@code Object}.
	 * @param player the player.
	 */
	public MonkeyDialogue(final Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new MonkeyDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		player("Hey little man, how's it goin'?");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			npc("Ukkuk oook! Eeek aka, ahh aka gonk.");
			stage++;
			break;
		case 1:
			player(FacialExpression.HALF_GUILTY,"Yeah.");
			stage = 5;
			break;
		case 5:
			end();
			break;
		}
		return true;
	}
	@Override
	public int[] getIds() {
		return new int[] { 2301 };
	}

}