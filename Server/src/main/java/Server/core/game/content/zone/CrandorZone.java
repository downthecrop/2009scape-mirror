package core.game.content.zone;

import core.game.node.entity.Entity;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneBuilder;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the crandor zone.
 *
 * @author Vexia
 */
@Initializable
public class CrandorZone extends MapZone implements Plugin<Object> {

    /**
     * Constructs a new {@code CrandorZone} {@code Object}
     */
    public CrandorZone() {
        super("crandor", true);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ZoneBuilder.configure(this);
        return this;
    }

    @Override
    public boolean enter(Entity entity) {
        return super.enter(entity);
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public void configure() {
        register(new ZoneBorders(2813, 3223, 2864, 3312));
    }

}
