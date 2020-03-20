package plugin.npc.other;

import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.Plugin;

/**
 * Package -> plugin.npc
 * Created on -> 9/13/2016 @5:07 PM for 530
 *
 * @author Ethan Kyle Millard
 */
public class SandwichLadyNPC extends AbstractNPC {

    /**
     * The default spawn location.
     */
    private static final Location DEFAULT_SPAWN = Location.create(3091, 3489, 0);

    /**
     * The movement path.
     */
    private static final Location[] MOVEMENT_PATH = { Location.create(3091, 3489, 0), Location.create(3092, 3492, 0), Location.create(3091, 3492, 0), Location.create(3092, 3492, 0), Location.create(3092, 3495, 0), Location.create(3093, 3495, 0), Location.create(3093, 3498, 0), Location.create(3096, 3499, 0), Location.create(3098, 3497, 0), Location.create(3094, 3494, 0), Location.create(3093, 3496, 0), Location.create(3091, 3488, 0) };

    public SandwichLadyNPC() {
        super(3117, DEFAULT_SPAWN);
    }

    public SandwichLadyNPC(int id, Location location) {
        super(id, location, false);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new SandwichLadyNPC(id, location);
    }

    @Override
    public void configure() {
        super.configure();
        if (isWalks()) {
            configureMovementPath(MOVEMENT_PATH);
        }
        setWalks(true);
    }

    @Override
    public int[] getIds() {
        return new int[] {3117};
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        init();
        return super.newInstance(arg);
    }
}
