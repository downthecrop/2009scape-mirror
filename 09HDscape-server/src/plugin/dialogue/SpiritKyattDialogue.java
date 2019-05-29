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
 * Represents the spirit kyatt's dialogue
 * @author Splinter
 * @version 1.0
 */
@InitializablePlugin
public final class SpiritKyattDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code GertrudesCatDialogue} {@code Object}.
	 */
	public SpiritKyattDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code GertrudesCatDialogue} {@code Object}.
	 * @param player the player.
	 */
	public SpiritKyattDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new SpiritKyattDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		if (!(npc instanceof Familiar)) {
			return false;
		}
		final Familiar f = (Familiar) npc;
		if (f.getOwner() != player) {
			player.getPacketDispatch().sendMessage("This is not your follower.");
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
			player.sendMessage("The Kyatt does not feel like talking now.");
			end();
			break;
		case 2:
			if (!WildernessZone.checkTeleport(player, 20)) {
				player.sendMessage("You cannot teleport with the Kyatt above level 20 wilderness.");
				end();
			} else {
				player.getTeleporter().send(new Location(2326, 3636), TeleportType.NORMAL);
				end();
			}
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 7365, 7366 };
	}
}
