package core.game.content.ame.events.genie;

import core.game.content.ame.AntiMacroNPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Graphics;

/**
 * Handles the random event genie npc.
 * @author Vexia
 */
public final class GenieNPC extends AntiMacroNPC {

	/**
	 * The force chats to pronounce.
	 */
	private static final String[] CHATS = new String[] { "Greetings, @gL @name!", "Ehem... @gender @name?", "Are you there, @gL @name?", "No one ignores me!" };

	/**
	 * Constructs a new {@code GenieNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 * @param event the event.
	 * @param player the player.
	 */
	public GenieNPC(int id, Location location, GenieRandomEvent event, Player player) {
		super(id, location, event, player, CHATS);
	}

	/**
	 * Constructs a new {@code GenieNPC} {@code Object}.
	 */
	public GenieNPC() {
		super(0, null, null, null);
	}

	@Override
	public void init() {
		super.init();
		graphics(new Graphics(86));
	}

	@Override
	public int[] getIds() {
		return new int[] { 409 };
	}

}
