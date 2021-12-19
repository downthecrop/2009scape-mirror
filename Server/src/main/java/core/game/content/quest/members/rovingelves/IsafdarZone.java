package core.game.content.quest.members.rovingelves;

import static api.ContentAPIKt.*;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.ImpactHandler;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneBuilder;
import core.plugin.Initializable;
import core.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

/**
 * locationUpdate hook for Isafdar traps
 * @authors downthecrop
 */

@Initializable
public final class IsafdarZone extends MapZone implements Plugin<Object> {

    public IsafdarZone() { super("Isafdar", true); }

    Location LEAF_TRAP_PIT = new Location(2336,9656,0);

    private String STICK_FAIL_MSG = "You set off the trap as you pass.";
    private String WIRE_FAIL_MSG = "You snag the trip wire as you step over it.";
    private String LEAF_FAIL_MSG = "You fall through and onto some spikes.";

    List<Location> WIRE_TRAPS = Arrays.asList(
            new Location(2215,3154,0),
            new Location(2220,3153,0),
            new Location(2285,3188,0)
    );

    List<Location> LEAF_TRAPS = Arrays.asList(
            new Location(2274,3174,0)
    );

    List<Location> STICK_TRAPS = Arrays.asList(
            new Location(2236,3181,0),
            new Location(2201,3169,0),
            new Location(2276,3163,0)
    );

    @Override
    public void configure() {
        register(new ZoneBorders(2178, 3150, 2304, 3196));
    }

    @Override
    public void locationUpdate(Entity e, Location last) {
        if (e instanceof Player) {
            Player player = (Player) e;
            if(LEAF_TRAPS.contains(player.getLocation())){
                sendMessage(player,LEAF_FAIL_MSG);
                impact(player,1, ImpactHandler.HitsplatType.NORMAL);
                player.teleport(LEAF_TRAP_PIT);
            } else if(STICK_TRAPS.contains(player.getLocation())) {
                sendMessage(player,STICK_FAIL_MSG);
                impact(player,1, ImpactHandler.HitsplatType.NORMAL);
            } else if (WIRE_TRAPS.contains(player.getLocation())){
                sendMessage(player,WIRE_FAIL_MSG);
                impact(player,1, ImpactHandler.HitsplatType.NORMAL);
            }
        }
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
}
