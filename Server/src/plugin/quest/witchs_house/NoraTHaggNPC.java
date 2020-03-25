package plugin.quest.witchs_house;

import org.crandor.game.component.Component;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.CombatSwingHandler;
import org.crandor.game.node.entity.combat.DeathTask;
import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.system.task.Pulse;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.map.Direction;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.path.Pathfinder;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.net.packet.PacketRepository;
import org.crandor.net.packet.context.MinimapStateContext;
import org.crandor.net.packet.out.MinimapState;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

import java.util.List;

/**
 * Created for 2009Scape
 * User: Ethan Kyle Millard
 * Date: March 15, 2020
 * Time: 1:14 PM
 */
@InitializablePlugin
public final class NoraTHaggNPC extends AbstractNPC {

    private static final Location[] MOVEMENT_PATH = {Location.create(2904, 3463, 0), Location.create(2908, 3463, 0), Location.create(2912, 3463, 0), Location.create(2916, 3463, 0), Location.create(2920, 3463, 0), Location.create(2924, 3463, 0), Location.create(2930, 3463, 0)};
    private int tilesIndex = 0;

    public NoraTHaggNPC() {
        super(896, Location.create(2904, 3463, 0));
    }

    private NoraTHaggNPC(int id, Location location) {
        super(id, location);
    }

    private boolean canTeleport(Entity t) {
        int playerX = t.getLocation().getX();
        int npcX = getLocation().getX();
        int[] sectors = { 2904, 2907, 2910, 2914, 2918, 2922, 2926, 2928 };
        for (int i = 0; i < sectors.length -1; i++) {
            if (npcX >= sectors[i] && npcX <= sectors[i+1] && playerX >= sectors[i] && playerX <= sectors[i+1]) {
                if (playerX < npcX && getDirection() == Direction.WEST || playerX > npcX && getDirection() == Direction.EAST) {
                    return true;
                }
                if (playerX == npcX) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void configure() {
        super.configure();
//        if (isWalks()) {
//            configureMovementPath(MOVEMENT_PATH);
//        }
//        setWalks(true);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new NoraTHaggNPC(id, location);
    }

    @Override
    public int[] getIds() {
        return new int[] { 896 };
    }

    private Location getRespawnLocation() {
        return Location.create(2901, 3466, 0);
    }

    @Override
    public int getWalkRadius() {
        return 50;
    }


    @Override
    public void tick() {
        super.tick();
        List<Player> players = getViewport().getCurrentPlane().getPlayers();
        for (Player player : players) {
            if (player == null || !player.isActive() || player.getLocks().isInteractionLocked() || DeathTask.isDead(player) || !canTeleport(player) || !CombatSwingHandler.isProjectileClipped(this, player, false)) {
                continue;
            }
            animate(new Animation(5803));
            sendTeleport(player);
        }
        if (getLocation().equals(Location.create(2904, 3463, 0))) {
            Pathfinder.find(this, Location.create(2930, 3463, 0)).walk(this);
        } else if (getLocation().equals(Location.create(2930, 3463, 0))) {
            Pathfinder.find(this, Location.create(2904, 3463, 0)).walk(this);
        }
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        return super.newInstance(arg);
    }

    private void sendTeleport(final Player player) {
        player.lock();
        GameWorld.submit(new Pulse(1) {
            int delay = 0;

            @Override
            public boolean pulse() {
                if (delay == 0) {
                    player.getPacketDispatch().sendMessage("You've been spotted by the witch.");
                    player.graphics(new Graphics(110, 100));
                    player.getInterfaceManager().openOverlay(new Component(115));
                    PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 2));
                    face(player);
                } else if (delay == 6) {
                    player.getProperties().setTeleportLocation(Location.create(getRespawnLocation()));
                    PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
                    player.getInterfaceManager().closeOverlay();
                    player.getInterfaceManager().close();
                    face(null);
                    player.unlock();
                    return true;
                }
                delay++;
                return false;
            }
        });
    }

}

