package plugin.activity.pyramidplunder;

import org.crandor.game.content.activity.ActivityManager;
import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;

/**
 * Handles Guardian mummy dialogue.
 * @author Emperor
 */
public final class GuardMummyDialogue extends DialoguePlugin {

	/**
	 * If the player successfully caught the evil twin.
	 */
	private int type;

	/**
	 * Constructs a new {@code GuardMummyDialogue} {@code Object}.
	 */
	public GuardMummyDialogue() {
		super();
	}

	/**
	 * Constructs a new {@code GuardMummyDialogue} {@code Object}.
	 * @param player The player.
	 */
	public GuardMummyDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new GuardMummyDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		type = args.length == 1 ? 0 : (Integer) args[1];
		if (type == 1) {
			player("I know what I'm doing - let's get on with it.");
		} else {
			System.out.println("TODO:1");
		}
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage++) {
		case 0:
			if (type == 1) {
				npc("Fine, I'll take you to the first room now...");
			} else {
				System.out.println("TODO:2");
			}
			return true;
		case 1:
			if (type == 1) {
				end();
				ActivityManager.start(player, "Pyramid plunder", false);
			} else {
				System.out.println("TODO:3");
			}
			return true;
		}
		return false;
	}

	@Override
	public int[] getIds() {
		return new int[] { 4476, 4477 };
	}

}