package plugin.interaction.city.lumbridge;

import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.GroundItemManager;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.map.zone.MapZone;
import org.crandor.game.world.map.zone.ZoneBorders;
import org.crandor.game.world.map.zone.ZoneBuilder;
import org.crandor.game.world.map.zone.ZoneRestriction;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;
import org.crandor.tools.RandomFunction;

/**
 * Zone for the lumbridge cow pen
 * @author ceik
 */


@InitializablePlugin
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
            CowDeaths++;
        }
        return false;
    }

}
