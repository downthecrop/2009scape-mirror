package core.game.content.zone;

import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneBuilder;
import core.game.world.map.zone.ZoneRestriction;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the tzhaar zone.
 * @author Vexia
 */
@Initializable
public class TzhaarZone extends MapZone implements Plugin<Object> {

	/**
	 * Constructs a new {@code TzhaarZone} {@code Object}
	 */
	public TzhaarZone() {
		super("Tzhaar zone", true, ZoneRestriction.CANNON);
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
		register(new ZoneBorders(2369, 5054, 2549, 5188));
	}

}
