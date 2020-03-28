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
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.net.packet.PacketRepository;
import org.crandor.net.packet.context.MinimapStateContext;
import org.crandor.net.packet.out.MinimapState;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

import java.util.List;

/**
 * Nora T Hagg NPC
 * @author Ethan, cleaned up/fixed by ceik
 * @date y'know
 */

@InitializablePlugin
public class NoraTHaggNPC extends AbstractNPC {
    boolean walkdir;
    public NoraTHaggNPC() {
        super(896, Location.create(2904, 3463, 0));
    }
    private Location getRespawnLocation() {
        return Location.create(2901, 3466, 0);
    }

    @Override
    public int[] getIds() {
        return new int[] { 896 };
    }

    private NoraTHaggNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        this.configure();
        init();
        return super.newInstance(arg);
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
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new NoraTHaggNPC(id, location);
    }

    @Override
    public void configure() {
        super.configure();
        setWalks(false);
        setPathBoundMovement(true);

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

    @Override
    public void tick() {
        super.tick();
        List<Player> players = getViewport().getCurrentPlane().getPlayers();
        if(getLocation().getX() == 2930){
            walkdir = false;
        } else if(getLocation().getX() == 2904){
            walkdir = true;
        }
        for (Player player : players) {
            if (player == null || !player.isActive() || player.getLocks().isInteractionLocked() || DeathTask.isDead(player) || !canTeleport(player) || !CombatSwingHandler.isProjectileClipped(this, player, false)) {
                continue;
            }
            animate(new Animation(5803));
            sendTeleport(player);
            player.getPacketDispatch().sendMessage("" + getLocation() + " matches? " + (getLocation().getX() == 2904));
        }
        if (location.getX() != 2930 && walkdir) {
            this.getWalkingQueue().reset();
            this.getWalkingQueue().addPath(location.getX() + 1,3463,true);
        } else if (location.getX() != 2904 && !walkdir) {
            this.getWalkingQueue().reset();
            this.getWalkingQueue().addPath(location.getX() - 1,3463,true);
        }
    }

}
