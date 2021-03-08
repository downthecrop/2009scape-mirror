package core.game.content.zone.rellekka;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;

/**
 * Handles the sailor dialogue.
 * @author Vexia
 */
public final class SailorDialogue extends DialoguePlugin {

	/**
	 * If its the relleka npc.
	 */
	private boolean rellekaNpc;

	/**
	 * Constructs a new {@Code SailorDialogue} {@Code Object}
	 */
	public SailorDialogue() {

	}

	/**
	 * Constructs a new {@Code SailorDialogue} {@Code Object}
	 * @param player the player.
	 */
	public SailorDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new SailorDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		rellekaNpc = npc.getId() == 1385;
		player("Hello. Can I get a ride on your ship?");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			npc("Hello again, brother " + player.getUsername() + ". If you're ready to jump", "aboard, we're all ready to set sail with the tide!");
			stage++;
			break;
		case 1:
			player("Let's go!");
			stage++;
			break;
		case 2:
			end();
			player.lock();
			player.sendMessage("You board the longship...");
			RellekkaZone.sail(player, rellekaNpc ? "Miscellania" : "Rellekka", rellekaNpc ? Location.create(2581, 3845, 0) : Location.create(2629, 3693, 0));
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 1304 };
	}

}
