package core.game.content.activity.pestcontrol;

import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneRestriction;

/**
 * The Pest control island map zone extension.
 * @author Emperor
 */
public final class PCIslandZone extends MapZone {

	/**
	 * Constructs a new {@code PCIslandZone} {@code Object}.
	 */
	public PCIslandZone() {
		super("pest control island", true, ZoneRestriction.CANNON, ZoneRestriction.FIRES, ZoneRestriction.RANDOM_EVENTS);
	}

	@Override
	public boolean death(Entity e, Entity killer) {
		if (e instanceof Player) { // Ensure players can't die on the island.
			// System.out.println("[PCIslandZone] Saved player from death!");
			e.getProperties().setTeleportLocation(e.getLocation());
			return true;
		}
		return false;
	}

	@Override
	public void configure() {
		registerRegion(10537);
	}

}