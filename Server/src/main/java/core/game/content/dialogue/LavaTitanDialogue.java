package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.TeleportManager.TeleportType;
import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.world.map.Location;
import core.game.world.map.zone.impl.WildernessZone;
import core.plugin.Initializable;

/**
 * Represents the lava titan's dialogue
 */
@Initializable
public final class LavaTitanDialogue extends DialoguePlugin {

	public LavaTitanDialogue() {
	}

	/**
	 * Constructs a new {@code LavaTitanDialogue} {@code Object}.
	 * @param player the player.
	 */
	public LavaTitanDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new LavaTitanDialogue(player);
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
			interpreter.sendOptions("Select an Option", "Chat", "Teleport to Lava Maze");
		}
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (buttonId) {
		case 1:
			player.sendMessage("The lava titan does not feel like talking now.");
			end();
			break;
		case 2:
			if (!WildernessZone.checkTeleport(player, 20)) {
				player.sendMessage("You cannot teleport with the Lava Titan above level 20 wilderness.");
				end();
			} else {
				player.getTeleporter().send(new Location(3048, 3820), TeleportType.NORMAL);
				end();
			}
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 8700 };
	}
}
