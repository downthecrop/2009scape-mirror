package core.game.interaction;

import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.impl.WalkingQueue;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.Point;
import core.game.world.map.path.Path;
import core.game.world.map.path.Pathfinder;
import core.net.packet.PacketRepository;
import core.net.packet.context.PlayerContext;
import core.net.packet.out.ClearMinimapFlag;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import rs09.game.system.SystemLogger;

import java.util.Deque;

/**
 * Handles a movement task.
 *
 * @author Emperor
 */
public abstract class MovementPulse extends Pulse {

    /**
     * The moving entity.
     */
    protected Entity mover;

    /**
     * The destination node.
     */
    protected Node destination;

    /**
     * The destination's last location.
     */
    private Location last;

    /**
     * The pathfinder.
     */
    private Pathfinder pathfinder;

    /**
     * If running should be forced.
     */
    private boolean forceRun;

    /**
     * The option handler.
     */
    private OptionHandler optionHandler;

    /**
     * The use with handler.
     */
    private UseWithHandler useHandler;

    /**
     * The destination flag.
     */
    private DestinationFlag destinationFlag;

    /**
     * The location to interact from.
     */
    private Location interactLocation;

    /**
     * If the path couldn't be fully found.
     */
    private boolean near;

    private Function2<Entity,Node,Location> overrideMethod;

    /**
     * Constructs a new {@code MovementPulse} {@code Object}.
     *
     * @param mover       The moving entity.
     * @param destination The destination node.
     */
    public MovementPulse(Entity mover, Node destination) {
        this(mover, destination, null, false);
    }

    /**
     * Constructs a new {@code MovementPulse} {@code Object}.
     *
     * @param mover       The moving entity.
     * @param destination The destination node.
     * @param forceRun    If the entity is forced to run.
     */
    public MovementPulse(Entity mover, Node destination, boolean forceRun) {
        this(mover, destination, null, forceRun);
    }

    /**
     * Constructs a new {@code MovementPulse} {@code Object}.
     *
     * @param mover       The moving entity.
     * @param destination The destination node.
     * @param pathfinder  The pathfinder to use.
     */
    public MovementPulse(Entity mover, Node destination, Pathfinder pathfinder) {
        this(mover, destination, pathfinder, false);
    }

    /**
     * Constructs a new {@code MovementPulse} {@code Object}.
     *
     * @param mover         The moving entity.
     * @param destination   The destination node.
     * @param optionHandler The option handler used.
     */
    public MovementPulse(Entity mover, Node destination, OptionHandler optionHandler) {
        this(mover, destination, null, false);
        this.optionHandler = optionHandler;
    }

    /**
     * Constructs a new {@code MovementPulse} {@code Object}.
     *
     * @param mover       The moving entity.
     * @param destination The destination node.
     * @param useHandler  The use with handler used.
     */
    public MovementPulse(Entity mover, Node destination, UseWithHandler useHandler) {
        this(mover, destination, null, false);
        this.useHandler = useHandler;
    }

    /**
     * Constructs a new {@code MovementPulse} {@code Object}.
     *
     * @param mover           The moving entity.
     * @param destination     The destination node.
     * @param destinationFlag The destination flag.
     */
    public MovementPulse(Entity mover, Node destination, DestinationFlag destinationFlag) {
        this(mover, destination, null, false);
        this.destinationFlag = destinationFlag;
    }

    public MovementPulse(Entity mover, Node destination, DestinationFlag destinationFlag, Function2<Entity,Node,Location> method){
        this(mover,destination,null,false);
        this.destinationFlag = destinationFlag;
        this.overrideMethod = method;
    }

    /**
     * Constructs a new {@code MovementPulse} {@code Object}.
     *
     * @param mover       The moving entity.
     * @param destination The destination node.
     * @param pathfinder  The pathfinder to use.
     * @param forceRun    If the entity is forced to run.
     */
    public MovementPulse(Entity mover, Node destination, Pathfinder pathfinder, boolean forceRun) {
        super(1, mover, destination);
        this.mover = mover;
        this.destination = destination;
        if (pathfinder == null) {
            if (mover instanceof Player) {
                this.pathfinder = Pathfinder.SMART;
            } else {
                this.pathfinder = Pathfinder.DUMB;
            }
        } else {
            this.pathfinder = pathfinder;
        }
        this.forceRun = forceRun;
    }

    @Override
    public boolean update() {
        mover.face(null);
        if (mover == null || destination == null || mover.getViewport().getRegion() == null) {
            return false;
        }

        if (hasInactiveNode() || !mover.getViewport().getRegion().isActive()) {
            stop();
            return true;
        }
        if (!isRunning()) {
            return true;
        }
        findPath();
        Location ml = mover.getLocation();
        // Allow being within 1 square of moving entities to interact with them.
        int radius = destination instanceof Entity && ((Entity)destination).getWalkingQueue().hasPath() ? 1 : 0;
        if (Math.max(Math.abs(ml.getX() - interactLocation.getX()), Math.abs(ml.getY() - interactLocation.getY())) <= radius) {
            try {
                if (near || pulse()) {
                    if (mover instanceof Player) {
                        if (near) {
                            ((Player) mover).getPacketDispatch().sendMessage("I can't reach that.");
                        }
                        PacketRepository.send(ClearMinimapFlag.class, new PlayerContext((Player) mover));
                    }
                    stop();
                    return true;
                }
            } catch (Exception e){
                e.printStackTrace();
                stop();
            }
        }
        return false;
    }

    @Override
    public void stop() {
        super.stop();
        if (destination instanceof Entity) {
            mover.face(null);
        }
        last = null;
    }

    /**
     * Finds a path to the destination, if necessary.
     */
    public void findPath() {
        if (mover instanceof NPC && mover.asNpc().isNeverWalks()) {
            return;
        }
        if(destination.getLocation() == null){
            SystemLogger.logAlert(destination.getId() + " < ID");
            SystemLogger.logAlert(destination.getName() + " < NAME");
            SystemLogger.logAlert("ASDAD");
            return;
        }
        boolean inside = isInsideEntity(mover.getLocation());
        if (last != null && last.equals(destination.getLocation()) && !inside) {
            return;
        }
        Location loc = null;
        if (destinationFlag != null && overrideMethod == null) {
            loc = destinationFlag.getDestination(mover, destination);
        }
        if(overrideMethod != null){
            loc = overrideMethod.invoke(mover,destination);
            if(loc == destination.getLocation()) loc = destinationFlag.getDestination(mover,destination);
        }
        if (loc == null && optionHandler != null) {
            loc = optionHandler.getDestination(mover, destination);
        }
        if (loc == null && useHandler != null) {
            loc = useHandler.getDestination((Player) mover, destination);
        }
        if (loc == null && inside) {
            loc = findBorderLocation();
        }
        if (destination == null) {
            return;
        }
        Location ml = mover.getLocation();
        Location dl = destination.getLocation();
        // Lead the target if they're walking/running, unless they're already within interaction range
        if(loc != null && destination instanceof Entity && Math.max(Math.abs(ml.getX() - dl.getX()), Math.abs(ml.getY() - dl.getY())) > 1) {
            WalkingQueue wq = ((Entity)destination).getWalkingQueue();
            if(wq.hasPath()) {
                Point[] points = wq.getQueue().toArray(new Point[0]);
                if(points.length > 0) {
                    Point p = points[0];
                    for(int i=0; i<points.length; i++) {
                        // Target the farthest point along target's planned movement that's within 1 tick's running,
                        // this ensures the player will run to catch up to the target if able.
                        if(Math.max(Math.abs(ml.getX() - points[i].getX()), Math.abs(ml.getY() - points[i].getY())) <= 2) {
                            p = points[i];
                        }
                    }
                    loc.setX(p.getX());
                    loc.setY(p.getY());
                }
            }
        }
        Path path = Pathfinder.find(mover, loc != null ? loc : destination, true, pathfinder);
        near = !path.isSuccessful() || path.isMoveNear();
        interactLocation = mover.getLocation();
        if (!path.getPoints().isEmpty()) {
            Point point = path.getPoints().getLast();
            interactLocation = Location.create(point.getX(), point.getY(), mover.getLocation().getZ());
            if (forceRun) {
                mover.getWalkingQueue().reset(forceRun);
            } else {
                mover.getWalkingQueue().reset();
            }
            int size = path.getPoints().toArray().length;
            Deque points = path.getPoints();
            for (int i = 0; i < size; i++) {
                point = path.getPoints().pop();
                mover.getWalkingQueue().addPath(point.getX(), point.getY());
                if (destination instanceof Entity) {
                    mover.face((Entity) destination);
                } else {
                    mover.face(null);
                }
            }
        }
        last = destination.getLocation();
    }

    /**
     * Finds the closest location next to the node.
     *
     * @return The location to walk to.
     */
    private Location findBorderLocation() {
        int size = destination.size();
        Location centerDest = destination.getLocation().transform(size >> 1, size >> 1, 0);
        Location center = mover.getLocation().transform(mover.size() >> 1, mover.size() >> 1, 0);
        Direction direction = Direction.getLogicalDirection(centerDest, center);
        Location delta = Location.getDelta(destination.getLocation(), mover.getLocation());
        main:
        for (int i = 0; i < 4; i++) {
            int amount = 0;
            switch (direction) {
                case NORTH:
                    amount = size - delta.getY();
                    break;
                case EAST:
                    amount = size - delta.getX();
                    break;
                case SOUTH:
                    amount = mover.size() + delta.getY();
                    break;
                case WEST:
                    amount = mover.size() + delta.getX();
                    break;
                default:
                    return null;
            }
            for (int j = 0; j < amount; j++) {
                for (int s = 0; s < mover.size(); s++) {
                    switch (direction) {
                        case NORTH:
                            if (!direction.canMove(mover.getLocation().transform(s, j + mover.size(), 0))) {
                                direction = Direction.get((direction.toInteger() + 1) & 3);
                                continue main;
                            }
                            break;
                        case EAST:
                            if (!direction.canMove(mover.getLocation().transform(j + mover.size(), s, 0))) {
                                direction = Direction.get((direction.toInteger() + 1) & 3);
                                continue main;
                            }
                            break;
                        case SOUTH:
                            if (!direction.canMove(mover.getLocation().transform(s, -(j + 1), 0))) {
                                direction = Direction.get((direction.toInteger() + 1) & 3);
                                continue main;
                            }
                            break;
                        case WEST:
                            if (!direction.canMove(mover.getLocation().transform(-(j + 1), s, 0))) {
                                direction = Direction.get((direction.toInteger() + 1) & 3);
                                continue main;
                            }
                            break;
                        default:
                            return null;
                    }
                }
            }
            Location location = mover.getLocation().transform(direction, amount);
            return location;
        }
        return null;
    }

    /**
     * Checks if the mover is standing on an invalid position.
     *
     * @param l The location.
     * @return {@code True} if so.
     */
    private boolean isInsideEntity(Location l) {
        if (!(destination instanceof Entity)) {
            return false;
        }
        if (((Entity) destination).getWalkingQueue().isMoving()) {
            return false;
        }
        Location loc = destination.getLocation();
        int size = destination.size();
        return Pathfinder.isStandingIn(l.getX(), l.getY(), mover.size(), mover.size(), loc.getX(), loc.getY(), size, size);
    }

    /**
     * Gets the forceRun.
     *
     * @return The forceRun.
     */
    public boolean isForceRun() {
        return forceRun;
    }

    /**
     * Sets the forceRun.
     *
     * @param forceRun The forceRun to set.
     */
    public void setForceRun(boolean forceRun) {
        this.forceRun = forceRun;
    }

    /**
     * Sets the current destination.
     *
     * @param destination The destination.
     */
    public void setDestination(Node destination) {
        this.destination = destination;
    }

    /**
     * Sets the last location.
     *
     * @param last The last location.
     */
    public void setLast(Location last) {
        this.last = last;
    }

}
