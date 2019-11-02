package plugin.random.zombie;

import org.crandor.game.content.ame.AntiMacroEvent;
import org.crandor.game.content.ame.AntiMacroNPC;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.map.Location;
import org.crandor.tools.RandomFunction;

/**
 * Handles the zombie npc.
 * @author Vexia
 */
public final class ZombieAmeNPC extends AntiMacroNPC {

	/**
	 * The zombie npc.
	 */
	private static final int[] IDS = new int[] { 419, 420, 421, 422, 423, 424 };

	/**
	 * Constructs a new {@code ZombieNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 * @param event the event.
	 * @param player the player.
	 * @param quotes the quotes.
	 */
	public ZombieAmeNPC(int id, Location location, AntiMacroEvent event, Player player) {
		super(id, location, event, player);
	}

	@Override
	public void init() {
		super.init();
		setRespawn(false);
		getProperties().getCombatPulse().attack(player);
		sendChat("Braaiinnzzzzzzzzzz");
	}

	@Override
	public void handleTickActions() {
		super.handleTickActions();
		if (!getProperties().getCombatPulse().isAttacking()) {
			getProperties().getCombatPulse().attack(player);
		}
		if (getProperties().getCombatPulse().isAttacking()) {
			if (RandomFunction.random(20) < 3) {
				sendChat("Braaiinnzzzzzzzzzz");
			}
		}
	}

	@Override
	public boolean isIgnoreMultiBoundaries(Entity victim) {
		return victim == player;
	}

	@Override
	public int[] getIds() {
		return IDS;
	}

}
