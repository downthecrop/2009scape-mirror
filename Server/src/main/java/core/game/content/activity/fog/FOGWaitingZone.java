package core.game.content.activity.fog;

import core.game.interaction.Option;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBuilder;
import core.plugin.Plugin;

/**
 * Represents the zone where players wait for a match.
 * @author Vexia
 */
public class FOGWaitingZone extends MapZone implements Plugin<Object> {

	/**
	 * Constructs a new {@code FOGLobbyZone} {@code Object}
	 */
	public FOGWaitingZone() {
		super("Fog Waiting Room", true);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ZoneBuilder.configure(this);
		return this;
	}

	@Override
	public boolean enter(Entity e) {
		return super.enter(e);
	}

	@Override
	public boolean interact(Entity e, Node target, Option option) {
		return super.interact(e, target, option);
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public void configure() {
		super.registerRegion(6487);
	}

}
