package plugin.activity.pyramidplunder;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.map.Location;
import org.crandor.tools.RandomFunction;

/**
 * Handles the mummy NPC in pyramid plunder
 * @author ceik
 */
public final class PyramidPlunderMummyNPC extends PyramidPlunderNPC {

    /**
     * The swarm id.
     */
    private static final int[] IDS = new int[] { 1958 };

    public PyramidPlunderMummyNPC(int id, Location location, Player player) {
        super(id, location, player);
    }

    @Override
    public void init() {
        super.init();
        setRespawn(false);
        getProperties().getCombatPulse().attack(player);
        sendChat("How dare you disturb my rest!");
    }

    @Override
    public void handleTickActions() {
        super.handleTickActions();
        if (!getProperties().getCombatPulse().isAttacking()) {
            getProperties().getCombatPulse().attack(player);
        }
        if (getProperties().getCombatPulse().isAttacking()) {
            if (RandomFunction.random(40) < 2) {
                sendChat("Leave this place!");
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
