package plugin.quest.arravshield;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.content.dialogue.FacialExpression;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;

/**
 * Represents the plugin used for jonny the beard.
 * @author 'Vexia
 * @version 1.0
 */
public final class JonnytheBeardPlugin extends DialoguePlugin {

	/**
	 * Constructs a new {@code JonnytheBearPlugin} {@code Object}.
	 */
	public JonnytheBeardPlugin() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code JonnytheBearPlugin} {@code Object}.
	 * @param player the player.
	 */
	public JonnytheBeardPlugin(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new JonnytheBeardPlugin(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		if (ShieldofArrav.isPhoenixMission(player)) {
			player.getPacketDispatch().sendMessage("Johnny the beard is not interested in talking.");
			end();
			return true;
		}
		interpreter.sendDialogues(npc, FacialExpression.NORMAL, "Will you buy me a beer?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendDialogues(player, FacialExpression.NORMAL, "No, I don't think I will.");
			stage = 1;
			break;
		case 1:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 645 };
	}
}