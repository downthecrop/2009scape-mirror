package core.game.interaction.city.lumbridge;

import core.game.node.entity.Entity;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneBuilder;
import core.plugin.Initializable;
import core.plugin.Plugin;
import rs09.GlobalStats;

/**
 * Zone for the lumbridge cow pen
 * @author ceik
 */


@Initializable
public class CowPenZone extends MapZone implements Plugin<Object> {
    public static int CowDeaths;

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }
    public CowPenZone() {
        super("lumbridge cows", true);
    }
    @Override
    public void configure() {
        super.register(new ZoneBorders(3242, 3255, 3265, 3297));
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ZoneBuilder.configure(this);
        return this;
    }

    @Override
    public boolean death(Entity e, Entity killer) {
        if (killer instanceof Player && e instanceof NPC) {
            GlobalStats.incrementDailyCowDeaths();
        }
        return false;
    }

}
