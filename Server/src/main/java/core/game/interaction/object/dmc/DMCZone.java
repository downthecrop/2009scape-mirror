package core.game.interaction.object.dmc;

import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneBuilder;
import core.game.world.map.zone.ZoneRestriction;
import core.plugin.Plugin;

/**
 * A restricted DMC zone.
 * @author Vexia
 */
public class DMCZone extends MapZone implements Plugin<Object> {

	/**
	 * Constructs a new {@code DMCZone} {@code Object}
	 */
	public DMCZone() {
		super("DMC Zone", true, ZoneRestriction.CANNON);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ZoneBuilder.configure(this);
		return this;
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public void configure() {
		// dwarven mine
		registerRegion(11929);
		registerRegion(12185);
		registerRegion(12184);
		// WG:
		register(new ZoneBorders(2838, 3536, 2875, 3555));
		// KQ:
		registerRegion(13972);
		// ice mountain
		register(new ZoneBorders(2995, 3465, 3022, 3509));
		// Fremmy dungeon
		register(new ZoneBorders(2690, 9934, 2831, 10050));
		// entrana
		registerRegion(11316);
		// black knight fortrss
		registerRegion(12086);
		// KBD
		registerRegion(9033);
	}

}
