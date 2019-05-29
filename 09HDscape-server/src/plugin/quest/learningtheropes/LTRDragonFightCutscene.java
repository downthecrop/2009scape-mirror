package plugin.quest.learningtheropes;

import org.crandor.game.content.activity.ActivityPlugin;
import org.crandor.game.content.activity.CutscenePlugin;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.system.task.Pulse;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.map.Direction;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.build.DynamicRegion;
import org.crandor.game.world.map.path.Path;
import org.crandor.game.world.map.path.Pathfinder;
import org.crandor.net.packet.PacketRepository;
import org.crandor.net.packet.context.CameraContext;
import org.crandor.net.packet.out.CameraViewPacket;
import org.crandor.plugin.InitializablePlugin;

/**
 * @author Ethan Kyle Millard <skype:pumpklins>
 * @since Mon, October 08, 2018 @ 5:00 PM
 */
@InitializablePlugin
public class LTRDragonFightCutscene extends CutscenePlugin {

    private static final NPC[] NPCS = new NPC[] {
            /** dragon */
            NPC.create(7943, Location.create(2524, 5015, 0), Direction.SOUTH),
            /** sir vant */
            NPC.create(7938, Location.create(2524, 5005, 0), Direction.NORTH),
           };

    private final FightPulse fightPulse = new FightPulse();

    public LTRDragonFightCutscene() {
        this(null);
    }

    public LTRDragonFightCutscene(final Player player) {
        super("ltr:dragon_fight_cs");
        this.player = player;
    }

    @Override
    public ActivityPlugin newInstance(Player p) throws Throwable {
        return new LTRDragonFightCutscene(p);
    }

    @Override
    public Location getSpawnLocation() {
        return getBase().transform(28, 12, 0);
    }

    @Override
    public Location getStartLocation() {
        return getBase().transform(28, 12, 0);
    }

    @Override
    public void register() {
        super.register();
    }

    @Override
    public void open() {
        setNpcs();
        GameWorld.submit(fightPulse);
        player.lock();
        player.getLocks().lockMovement(1000000);
        camera(31, 12, -45, 0, 300, 95);
    }



    public final class FightPulse extends Pulse {

        /**
         * Represents the counter.
         */
        private int counter;

        /**
         * Constructs a new {@code FightPulse} {@code Object}.
         */
        public FightPulse() {
            super(1, player);
        }

        @Override
        public boolean pulse() {
            switch (counter++) {
                case 1:
                    System.out.println("Fight begin");
                    break;

                case 4:
                    camera(28, 21, 1, 4, 220, 100);
                    Path path = Pathfinder.find(getDragon(), getDragon().getLocation().transform(-1, -9, 0), true, Pathfinder.DUMB, (a, b, c) -> 0);
                    path.walk(getDragon());
                    break;

                case 9:
                    camera(31, 12, -5, 3, 300, 100);
                    break;

                case 13:
                    camera(32, 10, -3, 5, 450, 100);
                    break;

                case 20:
                    System.out.println("Fight end");
                    LTRDragonFightCutscene.this.stop(true);
                    return true;
            }
            return !player.isActive();
        }

        @Override
        public void stop() {
            super.stop();
            clearNpcs();
        }

        /**
         * Gets the counter.
         *
         * @return the counter.
         */
        public int getCounter() {
            return counter;
        }
    }

    /**
     * Method used to handle the camera.
     *
     * @param x
     *            the x offset.
     * @param y
     *            the y offset.
     * @param xRot
     *            the xRotation.
     * @param yRot
     *            the yRotation.
     * @Param height the height.
     */
    private void camera(int x, int y, int xRot, int yRot, int height, int speed) {
        Location loc = base.transform(x, y, 0);
        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraContext.CameraType.POSITION, loc.getX(), loc.getY(), height, 1, speed));
        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraContext.CameraType.ROTATION, loc.getX() + xRot, loc.getY() + yRot, height, 1, speed));
    }

    @Override
    public void configure() {
        region = DynamicRegion.create(10062);
        setRegionBase();
        registerRegion(region.getId());
    }

    private void setNpcs() {
        for (NPC n : NPCS) {
            n = NPC.create(n.getId(), n.getLocation(), n.getDirection());
            n.setLocation(base.transform(n.getLocation().getLocalX(), n.getLocation().getLocalY(), 0));
            n.setRespawn(false);
            n.init();
            n.setWalks(false);
        }
    }

    @Override
    public void stop(boolean fade) {
        super.stop(false);
        player.teleport(Location.create(2524, 5004, 0));
    }

    /**
     * Method used to clear all the npcs.
     */
    private void clearNpcs() {
        GameWorld.submit(new Pulse(5) {
            @Override
            public boolean pulse() {
                for (NPC n : region.getPlanes()[0].getNpcs()) {
                    n.clear();
                }
                return true;
            }

        });
    }
    /**
     * Gets the npc.
     *
     * @param id
     *            the id.
     * @return the npc.
     */
    private NPC getNpc(int id) {
        for (NPC n : region.getPlanes()[0].getNpcs()) {
            if (n.getId() == id) {
                return n;
            }
        }
        return null;
    }

    public NPC getDragon() {
        return getNpc(7943);
    }
    public NPC getSirVant() {
        return getNpc(7938);
    }
}
