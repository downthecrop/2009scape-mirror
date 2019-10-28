package plugin.dialogue;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.node.entity.player.Player;

/**
 * Represents the gunthor brave dialogue.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class GunthorBraveDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code GunthorBraveDialogue} {@code Object}.
	 */
	public GunthorBraveDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code GunthorBraveDialogue} {@code Object}.
	 * @param player the player.
	 */
	public GunthorBraveDialogue(final Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new GunthorBraveDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		npc("You look funny.");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		npc.getProperties().getCombatPulse().attack(player);
		end();
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 199 };
	}

}
