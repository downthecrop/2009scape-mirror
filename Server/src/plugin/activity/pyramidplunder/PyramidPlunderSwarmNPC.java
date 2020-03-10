package plugin.activity.pyramidplunder;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.map.Location;
import org.crandor.tools.RandomFunction;

/**
 * Handles the swarm NPC in pyramid plunder
 * @author ceik
 */
public final class PyramidPlunderSwarmNPC extends PyramidPlunderNPC {

    /**
     * The swarm id.
     */
    private static final int[] IDS = new int[] { 2001 };

    public PyramidPlunderSwarmNPC(int id, Location location, Player player) {
        super(id, location, player);
    }

    @Override
    public void init() {
        super.init();
        setRespawn(false);
        getProperties().getCombatPulse().attack(player);
        sendChat("bzzzzz");
    }

    @Override
    public void handleTickActions() {
        super.handleTickActions();
        if (!getProperties().getCombatPulse().isAttacking()) {
            getProperties().getCombatPulse().attack(player);
        }
        if (getProperties().getCombatPulse().isAttacking()) {
            if (RandomFunction.random(40) < 2) {
                sendChat("bzzzzz");
            }
        }
    }

    @Override
    public boolean isIgnoreMultiBoundaries(Entity victim) {
        return victim == player;
    }

    @Override
    public int[] getIds() {
        return IDS;
    }

}
