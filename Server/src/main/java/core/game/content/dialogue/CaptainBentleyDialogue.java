package core.game.content.dialogue;

import core.game.component.Component;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.net.packet.PacketRepository;
import core.net.packet.context.MinimapStateContext;
import core.plugin.Initializable;
import core.net.packet.out.MinimapState;

/**
 * Handles the captain bentley dialogue.
 * @author Vexia
 */
@Initializable
public class CaptainBentleyDialogue extends DialoguePlugin {

	/**
	 * If we're on the isle.
	 */
	private boolean onIsle;

	/**
	 * Constructs a new {@Code CaptainBentleyDialogue} {@Code
	 * Object}
	 */
	public CaptainBentleyDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@Code CaptainBentleyDialogue} {@Code
	 * Object}
	 * @param player the player.
	 */
	public CaptainBentleyDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new CaptainBentleyDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		onIsle = npc.getLocation().getRegionId() == 8508;
		npc("Hi, would you like to travel to " + (onIsle ? "Pirate's Cove" : "Lunar Isle") + "?");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			options("Yes, please.", "No, thanks.");
			stage++;
			break;
		case 1:
			end();
			if (buttonId == 1) {
				travel(player, !onIsle ? Location.create(2131, 3900, 2) : Location.create(2216, 3797, 2));
				return true;
			}
			break;
		}
		return true;
	}

	/**
	 * Travels to a location.
	 * @param location the location.
	 */
	private void travel(final Player player, final Location location) {
		player.lock();
		GameWorld.getPulser().submit(new Pulse(1, player) {
			int counter;

			@Override
			public boolean pulse() {
				switch (counter++) {
				case 1:
					player.lock();
					player.getInterfaceManager().openOverlay(new Component(115));
					break;
				case 3:
					PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 2));
					break;
				case 4:
					player.getProperties().setTeleportLocation(location);
					break;
				case 5:
					player.getInterfaceManager().closeOverlay();
					player.getInterfaceManager().close();
					PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
					player.unlock();
					return true;
				}
				return false;
			}

		});
	}

	@Override
	public int[] getIds() {
		return new int[] { 4540 };
	}

}
