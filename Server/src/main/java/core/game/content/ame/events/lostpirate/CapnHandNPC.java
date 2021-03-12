package core.game.content.ame.events.lostpirate;

import core.game.content.ame.AntiMacroEvent;
import core.game.content.ame.AntiMacroNPC;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;

/**
 * Handles the capn hand npc.
 * @author Vexia
 */
public final class CapnHandNPC extends AntiMacroNPC {

	/**
	 * The quotes to say.
	 */
	private static final String[] QUOTES = new String[] { "Heave to, @gender @name!", "That be an order, @gender @name!" };

	/**
	 * Constructs a new {@code CapnHandNPC} {@code Object}.
	 * @param id the id.
	 * @param location the loc.
	 * @param event the event.
	 * @param player the player.
	 */
	public CapnHandNPC(int id, Location location, AntiMacroEvent event, Player player) {
		super(id, location, event, player, QUOTES);
	}

	@Override
	public void handleTickActions() {
		super.handleTickActions();
		if (timeUp && getDialoguePlayer() == null) {
			if (!getProperties().getCombatPulse().isAttacking()) {
				getProperties().getCombatPulse().attack(player);
			}
		}
	}

	@Override
	public void handleTimeUp() {
	}

	@Override
	public boolean isIgnoreAttackRestrictions(Entity victim) {
		return true;
	}

	@Override
	public boolean isIgnoreMultiBoundaries(Entity victim) {
		return victim == player;
	}

	@Override
	public int[] getIds() {
		return new int[] { 2539 };
	}

}
