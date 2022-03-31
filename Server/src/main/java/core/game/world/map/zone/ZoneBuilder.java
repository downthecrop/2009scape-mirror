package core.game.world.map.zone;

import api.StartupListener;
import core.game.world.map.zone.impl.*;


/**
 * Loads all the default zones.
 * @author Emperor
 */
public class ZoneBuilder implements StartupListener {

	@Override
	public void startup() {
		configure(WildernessZone.getInstance());
		configure(MultiwayCombatZone.getInstance());
		configure(new ModeratorZone());
		configure(new DarkZone());
		configure(new KaramjaZone());
		configure(new BankZone());
	}

	/**
	 * Configures the map zone.
	 * @param zone The map zone.
	 */
	public static void configure(MapZone zone) {
		zone.setUid(zone.getName().hashCode());
		zone.configure();
	}
}