package core.game.content.global.action;

import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.audio.Audio;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.scenery.Constructed;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.map.path.Pathfinder;
import kotlin.Unit;
import rs09.game.system.config.DoorConfigLoader;
import rs09.game.world.GameWorld;

import java.awt.*;

/**
 * Handles door actions.
 *
 * @author Emperor
 */
public final class DoorActionHandler {

    /**
     * The charge indicating the door is already in use.
     */
    private static final int IN_USE_CHARGE = 88;

    /**
     * Handles a door reward.
     *
     * @param player The player.
     * @param object The object.
     */
    public static void handleDoor(final Player player, final Scenery object) {
        final Scenery second = (object.getId() == 1530 || object.getId() == 1531) ? null : getSecondDoor(object, player);
        Scenery o = null;
        if (object instanceof Constructed && (o = ((Constructed) object).getReplaced()) != null) {
            player.getAudioManager().send(new Audio(43));
            SceneryBuilder.replace(object, o);
            if (second instanceof Constructed && (o = ((Constructed) second).getReplaced()) != null) {
                SceneryBuilder.replace(second, o);
                return;
            }
            return;
        }
        if (object.getDefinition().hasAction("close")) {
            if (second != null) {
                player.getPacketDispatch().sendMessage("The doors appear to be stuck.");
                return;
            }
            DoorConfigLoader.Door d = DoorConfigLoader.Companion.forId(object.getId());
            if (d == null) {
                player.getPacketDispatch().sendMessage("The door appears to be stuck.");
                return;
            }
            int firstDir = (object.getRotation() + 3) % 4;
            Point p = getCloseRotation(object);
            Location firstLoc = object.getLocation().transform((int) p.getX(), (int) p.getY(), 0);
            SceneryBuilder.replace(object, object.transform(d.getReplaceId(), firstDir, firstLoc));
            return;
        }
        DoorConfigLoader.Door d = DoorConfigLoader.Companion.forId(object.getId());
        if (d == null) {
            handleAutowalkDoor(player, object);
            return;
        }
        if (d.isMetal()) {
            player.getAudioManager().send(new Audio(71));
        } else {
            player.getAudioManager().send(new Audio(81));
        }
        if (second != null) {
            DoorConfigLoader.Door s = DoorConfigLoader.Companion.forId(second.getId());
            open(object, second, d.getReplaceId(), s == null ? second.getId() : s.getReplaceId(), true, 500, d.isFence());
            return;
        }
        open(object, null, d.getReplaceId(), -1, true, 500, d.isFence());
    }

    /**
     * Handles the opening and walking through a door.
     *
     * @param entity The entity walking through the door.
     * @param object The door object.
     * @return
     */
    public static boolean handleAutowalkDoor(final Entity entity, final Scenery object, final Location endLocation) {
        if (object.getCharge() == IN_USE_CHARGE) {
            return false;
        }
        final Scenery second = (object.getId() == 3) ? null : getSecondDoor(object, entity);
        entity.lock(4);
        final Location loc = entity.getLocation();
        if (entity instanceof Player) {
            ((Player) entity).getAudioManager().send(new Audio(3419));
            entity.asPlayer().logoutListeners.put("autowalk", player -> {
                player.setLocation(loc);
                return Unit.INSTANCE;
            });
        }
		GameWorld.getPulser().submit(new Pulse(1) {
            boolean opened = false;
            @Override
            public boolean pulse() {
                if (!opened) {
                    open(object, second, object.getId(), second == null ? -1 : second.getId(), false, 2, false);
                    entity.getWalkingQueue().reset();
                    entity.getWalkingQueue().addPath(endLocation.getX(), endLocation.getY());
                    opened = true;

                    // Set the door in_use
                    object.setCharge(IN_USE_CHARGE);
                    if (second != null) {
                        second.setCharge(IN_USE_CHARGE);
                    }
                    return false;
                }
                if (entity instanceof Player) {
                    Player player = entity.asPlayer();
                    if (object.getId() == 2112 && player.getLocation().withinDistance(new Location(3046, 9756, 0), 10)) {
                        player.getAchievementDiaryManager().finishTask(player, DiaryType.FALADOR, 2, 6);
                    }

                    // pass the al kharid gate
                    if (object.getId() == 35549 || object.getId() == 35551 && player.getViewport().getRegion().getId() == 13106) {
                        player.getAchievementDiaryManager().finishTask(player, DiaryType.LUMBRIDGE, 0, 4);
                    }

                    // Search the shed in Lumbridge Swamp
                    if (object.getId() == 2406 && player.getLocation().withinDistance(Location.create(3202,3169,0))) {
                        player.getAchievementDiaryManager().finishTask(player, DiaryType.LUMBRIDGE, 1, 6);
                    }

                    entity.asPlayer().logoutListeners.remove("autowalk");
                }

                // Reset door to inactive
                object.setCharge(1000);
                if (second != null) {
                    second.setCharge(1000);
                }
                return true;
            }
        });
        return true;
    }

    /**
     * Method wrapper for handling the auto walk door.
     *
     * @param entity the entity.
     * @param object the object.
     */
    public static boolean handleAutowalkDoor(final Entity entity, final Scenery object) {
        return handleAutowalkDoor(entity, object, getEndLocation(entity, object));
    }

    /**
     * Gets the end location to walk to.
     *
     * @param entity the entity.
     * @param object the object.
     * @return the end location.
     */
    public static Location getEndLocation(Entity entity, Scenery object){
        return getEndLocation(entity,object,false);
    }
    public static Location getEndLocation(Entity entity, Scenery object, Boolean isAutoWalk) {
        Location l = object.getLocation();
        switch (object.getRotation()) {
            case 0:
                if (entity.getLocation().getX() >= l.getX()) {
                    l = l.transform(-1, 0, 0);
                }
                break;
            case 1:
                if (entity.getLocation().getY() <= l.getY()) {
                    l = l.transform(0, 1, 0);
                }
                break;
            case 2:
                if (entity.getLocation().getX() <= l.getX()) {
                    l = l.transform(1, 0, 0);
                }
                break;
            default:
                if (entity.getLocation().getY() >= l.getY()) {
                    l = l.transform(0, -1, 0);
                }
                break;
        }
        return l;
    }

    /**
     * Gets the destination for the door.
     *
     * @param door The door.
     * @return The destination location.
     */
    public static Location getDestination(Entity entity, Scenery door) {
        Location l = door.getLocation();
        int rotation = door.getRotation();
        if (door instanceof Constructed && door.getDefinition().hasAction("close")) {
            Scenery o = ((Constructed) door).getReplaced();
            if (o != null) {
                l = o.getLocation();
                rotation = o.getRotation();
            }
        }
        if (door.getType() == 9) { // Diagonal doors
            switch (rotation) {
                case 0:
                case 2:
                    if (entity.getLocation().getY() < l.getY() || entity.getLocation().getX() < l.getX()) {
                        return l.transform(0, -1, 0);
                    }
                    return l.transform(0, 1, 0);
                case 1:
                case 3:
                    if (entity.getLocation().getX() > l.getX() || entity.getLocation().getY() > l.getY()) {
                        return l.transform(1, 0, 0);
                    }
                    return l.transform(-1, 0, 0);
            }
        }
        switch (rotation) {
            case 0:
                if (entity.getLocation().getX() < l.getX()) {
                    if(Pathfinder.find(entity,l.transform(-1,0,0)).isMoveNear()){
                        return l.transform(0,0,0);
                    }
                    return l.transform(-1, 0, 0);
                }
                break;
            case 1:
                if (entity.getLocation().getY() > l.getY()) {
                    if(Pathfinder.find(entity,l.transform(0,1,0)).isMoveNear()){
                        return l.transform(0,0,0);
                    }
                    return l.transform(0, 1, 0);
                }
                break;
            case 2:
                if (entity.getLocation().getX() > l.getX()) {
                    if(Pathfinder.find(entity,l.transform(1,0,0)).isMoveNear()){
                        return l.transform(0,0,0);
                    }
                    return l.transform(1, 0, 0);
                }
                break;
            case 3:
                if (entity.getLocation().getY() < l.getY()) {
                    if(Pathfinder.find(entity,l.transform(0,-1,0)).isMoveNear()){
                        return l.transform(0,0,0);
                    }
                    return l.transform(0, -1, 0);
                }
                break;
        }
        return l;
    }

    /**
     * Opens the doors.
     *
     * @param object          The door object.
     * @param second          The second door object.
     * @param replaceId       The replace id.
     * @param secondReplaceId The second replace id.
     * @param clip            If clipping should be changed due to opening the door.
     * @param restoreTicks    The amount of ticks before the door(s) should be
     *                        closed again.
     */
    public static void open(Scenery object, Scenery second, int replaceId, int secondReplaceId, boolean clip, int restoreTicks, boolean fence) {
        object = object.getWrapper();
        int mod = object.getType() == 9 ? -1 : 1;
        int firstDir = (object.getRotation() + ((mod + 4) % 4)) % 4;
        Point p = getRotationPoint(object.getRotation());
        Location firstLoc = object.getLocation().transform((int) p.getX() * mod, (int) p.getY() * mod, 0);
        if (second == null) {
            if (replaceId == 4577) {
                replaceId = 4578;
                firstDir = 3;
                firstLoc = firstLoc.transform(0, 1, 0);
            }
            SceneryBuilder.replace(object, object.transform(replaceId, firstDir, firstLoc), restoreTicks, clip);
            return;
        }
        second = second.getWrapper();
        if (fence) {
            openFence(object, second, replaceId, secondReplaceId, clip, restoreTicks);
            return;
        }
        Direction offset = Direction.getDirection(second.getLocation().getX() - object.getLocation().getX(), second.getLocation().getY() - object.getLocation().getY());
        int secondDir = (second.getRotation() + mod) % 4;
        if (firstDir == 1 && offset == Direction.NORTH) {
            firstDir = 3;
        } else if (firstDir == 2 && offset == Direction.EAST) {
            firstDir = 0;
        } else if (firstDir == 3 && offset == Direction.SOUTH) {
            firstDir = 1;
        } else if (firstDir == 0 && offset == Direction.WEST) {
            firstDir = 2;
        }
        if (firstDir == secondDir) {
            secondDir = (secondDir + 2) % 4;
        }
        Location secondLoc = second.getLocation().transform((int) p.getX(), (int) p.getY(), 0);
        SceneryBuilder.replace(object, object.transform(replaceId, firstDir, firstLoc), restoreTicks, clip);
        SceneryBuilder.replace(second, second.transform(secondReplaceId, secondDir, secondLoc), restoreTicks, clip);
    }

    /**
     * Handles the opening of a fence.
     *
     * @param object          The fence object.
     * @param second          The second fence object.
     * @param replaceId       The replace id.
     * @param secondReplaceId The second replace id.
     * @param clip            If clipping should be changed due to opening the door.
     * @param restoreTicks    The amount of ticks before the door(s) should be
     *                        closed again.
     */
    private static void openFence(Scenery object, Scenery second, int replaceId, int secondReplaceId, boolean clip, int restoreTicks) {
        Direction offset = Direction.getDirection(second.getLocation().getX() - object.getLocation().getX(), second.getLocation().getY() - object.getLocation().getY());
        int firstDir = (object.getRotation() + 3) % 4;
        Point p = getRotationPoint(object.getRotation());
        Location firstLoc = null;
        int secondDir = (second.getRotation() + 3) % 4;
        if (offset == Direction.WEST || offset == Direction.SOUTH) {
            firstLoc = second.getLocation().transform((int) p.getX(), (int) p.getY(), 0);
            int s = replaceId;
            replaceId = secondReplaceId;
            secondReplaceId = s;
        } else {
            firstLoc = object.getLocation().transform((int) p.getX(), (int) p.getY(), 0);
        }
        if (object.getRotation() == 3 || object.getRotation() == 2) {
            firstDir = (firstDir + 2) % 4;
            secondDir = (secondDir + 2) % 4;
        }
        Location secondLoc = firstLoc.transform((int) p.getX(), (int) p.getY(), 0);
        if ((object.getId() == 36917 || object.getId() == 36919)) {
            switch (object.getDirection()) {
                case SOUTH:
                    SceneryBuilder.replace(object, object.transform(36919, firstDir, firstLoc), restoreTicks, true);
                    SceneryBuilder.replace(second, second.transform(36917, secondDir, secondLoc), restoreTicks, true);
                    break;
                case EAST:
                    SceneryBuilder.replace(object, object.transform(36917, firstDir, firstLoc), restoreTicks, true);
                    SceneryBuilder.replace(second, second.transform(36919, secondDir, secondLoc), restoreTicks, true);
                    break;
                default:
                    SceneryBuilder.replace(object, object.transform(36919, firstDir, firstLoc), restoreTicks, true);
                    SceneryBuilder.replace(second, second.transform(36917, secondDir, secondLoc), restoreTicks, true);
                    break;
            }
        } else {
            SceneryBuilder.replace(object, object.transform(replaceId, firstDir, firstLoc), restoreTicks, clip);
            SceneryBuilder.replace(second, second.transform(secondReplaceId, secondDir, secondLoc), restoreTicks, clip);
        }
    }

    /**
     * Handles the opening of a fence.
     *
     * @param object          The fence object.
     * @param replaceId       The replace id.
     * @param secondReplaceId The second replace id.
     *                        closed again.
     */
    public static boolean autowalkFence(final Entity entity, final Scenery object, final int replaceId, final int secondReplaceId) {
        final Scenery second = getSecondDoor(object, entity);
        if (object.getCharge() == IN_USE_CHARGE || second == null) {
            return false;
        }
        entity.lock(4);
        final Location loc = entity.getLocation();
        if(entity instanceof Player)
        {
            entity.asPlayer().logoutListeners.put("autowalk", player -> {
                player.setLocation(loc);
                return Unit.INSTANCE;
            });
        }
        object.setCharge(IN_USE_CHARGE);
        second.setCharge(IN_USE_CHARGE);
		GameWorld.getPulser().submit(new Pulse(1) {
            boolean opened = false;

            @Override
            public boolean pulse() {
                if (!opened) {
                    openFence(object, second, replaceId, secondReplaceId, false, 2);
                    Location l = getEndLocation(entity, object);
                    entity.getWalkingQueue().reset();
                    entity.getWalkingQueue().addPath(l.getX(), l.getY());
                    opened = true;
                    return false;
                }
                if(entity instanceof Player)
                {
                    entity.asPlayer().logoutListeners.remove("autowalk");
                }
                object.setCharge(1000);
                if (second != null) {
                    second.setCharge(1000);
                }
                return true;
            }
        });
        return true;
    }

    /**
     * Gets the closing rotation point.
     *
     * @param object The object.
     * @return The point.
     */
    private static Point getCloseRotation(Scenery object) {
        switch (object.getRotation()) {
            case 0:
                return new Point(0, 1);
            case 1:
                return new Point(1, 0);
            case 2:
                return new Point(0, -1);
            case 3:
                return new Point(-1, 0);
        }
        return new Point(0, 0);
    }

    /**
     * Gets the rotation point for the object.
     *
     * @return The rotation point.
     */
    public static Point getRotationPoint(int rotation) {
        switch (rotation) {
            case 0:
                return new Point(-1, 0);
            case 1:
                return new Point(0, 1);
            case 2:
                return new Point(1, 0);
            case 3:
                return new Point(0, -1);
        }
        return null;
    }

    /**
     * Gets the door next to this door.
     *
     * @param object The door.
     * @return The second door, if any, {@code null} if no second door existed.
     */
    public static Scenery getSecondDoor(Scenery object, Entity entity) {
        Location l = object.getLocation();
        Player player = entity instanceof Player ? (Player) entity : null;
        Scenery o = null;
        if ((o = RegionManager.getObject(l.transform(-1, 0, 0))) != null && o.getName().equals(object.getName())) {
            return o;
        }
        if ((o = RegionManager.getObject(l.transform(1, 0, 0))) != null && o.getName().equals(object.getName())) {
            return o;
        }
        if ((o = RegionManager.getObject(l.transform(0, -1, 0))) != null && o.getName().equals(object.getName())) {
            return o;
        }
        if ((o = RegionManager.getObject(l.transform(0, 1, 0))) != null && o.getName().equals(object.getName())) {
            return o;
        }
        return null;
    }

    /**
     * Gets the rotations to set the opened doors to.
     *
     * @param object The first door.
     * @param second The second door.
     * @param rp     The rotation point.
     * @return An int-array, with index 0 being first door rotation and index 1
     * being second door rotation.
     */
    public static int[] getRotation(Scenery object, Scenery second, Point rp) {
        if (second == null) {
            return new int[]{(object.getRotation() + 1) % 4};
        }
        int[] rotations = new int[]{3, 1};
        Location fl = object.getLocation();
        Location sl = second.getLocation();
        if (fl.getX() > sl.getX()) {
            rotations = new int[]{2, 0};
        }
        if (fl.getX() < sl.getX()) {
            rotations = new int[]{0, 2};
        }
        if (fl.getY() > sl.getY()) {
            rotations = new int[]{1, 3};
        }
        if (rp.getY() > 0 || rp.getX() > 0) {
            rotations = new int[]{rotations[1], rotations[0]};
        }
        return rotations;
    }
}