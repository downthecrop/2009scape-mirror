package plugin.dialogue;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.TeleportManager.TeleportType;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.world.map.zone.impl.WildernessZone;

/**
 * Represents the spirit graahk's dialogue
 * @author Splinter
 * @version 1.0
 */
@InitializablePlugin
public final class SpiritGraahkDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code GertrudesCatDialogue} {@code Object}.
	 */
	public SpiritGraahkDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code GertrudesCatDialogue} {@code Object}.
	 * @param player the player.
	 */
	public SpiritGraahkDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new SpiritGraahkDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		if (!(npc instanceof Familiar)) {
			return false;
		}
		final Familiar fam = (Familiar) npc;
		if (fam.getOwner() != player) {
			player.getPacketDispatch().sendMessage("This is not your familiar.");
			return true;
		} else {
			interpreter.sendOptions("Select an Option", "Chat", "Teleport");
		}
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (buttonId) {
		case 1:
			player.sendMessage("The Graahk does not feel like talking now.");
			end();
			break;
		case 2:
			if (!WildernessZone.checkTeleport(player, 20)) {
				player.sendMessage("You cannot teleport with the Graahk above level 20 wilderness.");
				end();
			} else {
				player.getTeleporter().send(new Location(2786, 3002), TeleportType.NORMAL);
				end();
			}
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 7353, 7364 };
	}
}
