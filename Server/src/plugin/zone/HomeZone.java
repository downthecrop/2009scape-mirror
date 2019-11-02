package plugin.zone;

import org.crandor.game.node.object.GameObject;
import org.crandor.game.node.object.ObjectBuilder;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.zone.MapZone;
import org.crandor.game.world.map.zone.ZoneBorders;
import org.crandor.game.world.map.zone.ZoneBuilder;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.PluginManager;

import plugin.zone.GrandExchangeZone.TeleporterDialogue;

public class HomeZone extends MapZone implements Plugin<Object> {

	/**
	 * Constructs a new {@code HomeZone} {@code Object}.
	 */
	public HomeZone() {
		super("home", true);
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
		addObjects();
		PluginManager.definePlugin(new TeleporterDialogue());
		super.register(new ZoneBorders(3075, 3460, 3115, 3519));
	}
	
	/**
	 * The objects to spawn in the Grand Exchange Zone.
	 */
	private final void addObjects(){
		ObjectBuilder.add(new GameObject(29534, new Location(3084, 3490), 1));
	}

}
