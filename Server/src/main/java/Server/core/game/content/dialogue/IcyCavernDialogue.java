package core.game.content.dialogue;

import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.game.world.map.Location;

/**
 * Represents the icy cavern dialogue plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class IcyCavernDialogue extends DialoguePlugin {

	/**
	 * Represents the dialogue id.
	 */
	public static final int ID = 238284;

	/**
	 * Represents the location to teleport to.
	 */
	private static final Location LOCATION = Location.create(3056, 9555, 0);

	/**
	 * Constructs a new {@code IcyCavernDialogue} {@code Object}.
	 */
	public IcyCavernDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code IcyCavernDialogue} {@code Object}.
	 * @param player the player.
	 */
	public IcyCavernDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new IcyCavernDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		interpreter.sendDialogue("STOP! The creatures in this cave are VERY Dangerous. Are you", "sure you want to enter?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendOptions("Select an Option", "Yes, I'm not afraid of death!", "No thanks, I don't want to die!");
			stage = 1;
			break;
		case 1:
			switch (buttonId) {
			case 1:
				end();
				player.getProperties().setTeleportLocation(LOCATION);
				player.getPacketDispatch().sendMessage("You venture into the icy cavern.");
				break;
			case 2:
				end();
				break;
			}
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { ID };
	}
}
