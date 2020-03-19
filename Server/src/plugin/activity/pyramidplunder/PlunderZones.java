package plugin.activity.pyramidplunder;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.system.task.LocationLogoutTask;
import org.crandor.game.system.task.LogoutTask;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.zone.MapZone;
import org.crandor.game.world.map.zone.ZoneBorders;
import org.crandor.game.world.map.zone.ZoneBuilder;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Defines the zones for the pyramid plunder rooms
 * @author ceik
 */

@InitializablePlugin
public class PlunderZones implements Plugin<Object> {
    PlunderZone[] ROOMS = {
            new PlunderZone("PR1", 1,1923, 4464, 1932, 4474),
            new PlunderZone("PR2", 2,1925, 4449, 1941, 4458),
            new PlunderZone("PR3", 3,1941, 4421, 1954, 4432),
            new PlunderZone("PR4", 4,1949, 4464, 1959, 4477),
            new PlunderZone("PR5", 5,1968, 4420, 1978, 4436),
            new PlunderZone("PR6", 6,1969, 4452, 1980, 4473),
            new PlunderZone("PR7", 7,1923, 4424, 1931, 4439),
            new PlunderZone("PR8", 8, 1950, 4442, 1969, 4455)
    };

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for(PlunderZone ROOM : ROOMS){
            ZoneBuilder.configure(ROOM);
        }
        return this;
    }
    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    public class PlunderZone extends MapZone {
        int swx, swy, nex, ney;
        String name;
        int roomnum;

        public PlunderZone(String name, int roomnum, int swx, int swy, int nex, int ney) {
            super(name, true);
            this.name = name;
            this.swx = swx;
            this.swy = swy;
            this.nex = nex;
            this.ney = ney;
            this.roomnum = roomnum;
        }

        @Override
        public void configure() {
            ZoneBorders borders = new ZoneBorders(swx, swy, nex, ney,0);
            register(borders);
        }

        @Override
        public boolean enter(Entity e){
            if(e instanceof Player) {
                e.asPlayer().getPacketDispatch().sendMessage("<col=7f03ff>Room: " + (roomnum) + " Level required: " + (21 + ((roomnum - 1) * 10)));
                e.asPlayer().getPlunderObjectManager().resetObjectsFor(e.asPlayer());
                e.asPlayer().addExtension(LogoutTask.class, new LocationLogoutTask(12, Location.create(3288, 2801, 0)));
            }
            return true;
        }
    }
}