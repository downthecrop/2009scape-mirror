package core.game.content.global.action;

import api.ContentAPIKt;
import core.game.container.impl.EquipmentContainer;
import org.rs09.consts.Items;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;

import static api.ContentAPIKt.isEquipped;

/**
 * Handles a ladder climbing reward.
 * @author Emperor
 */
public final class ClimbActionHandler {

    /**
     * Represents the climb up animation of ladders.
     */
    public static final Animation CLIMB_UP = new Animation(828);

    /**
     * Represents the climb down animation of ladders.
     */
    public static final Animation CLIMB_DOWN = new Animation(827);

    /**
     * The climb dialogue.
     */
    public static DialoguePlugin CLIMB_DIALOGUE = new ClimbDialogue();

    /**
     * Handles the climbing of a rope.
     *
     * @param player The player.
     * @param object The rope object.
     * @param option The option.
     */
    public static void climbRope(Player player, Scenery object, String option) {

    }

    /**
     * Handles the climbing of a trap door.
     *
     * @param player The player.
     * @param object The trap door object.
     * @param option The option.
     */
    public static void climbTrapdoor(Player player, Scenery object, String option) {

    }

    /**
     * Handles the climbing of a ladder.
     *
     * @param player      The player.
     * @param startLadder The scenery.
     * @param option      The option.
     * @return True if successfully climbed
     */
    public static boolean climbLadder(Player player, Scenery startLadder, String option) {
        Scenery endLadder = null;
        Animation animation = CLIMB_UP;
        if (startLadder.getName().startsWith("Stair")) {
            animation = null;
        }
        if (SpecialLadders.getDestination(startLadder.getLocation()) != null) {
            Location destination = SpecialLadders.getDestination(startLadder.getLocation());
            climb(player, animation, destination);
            if(SpecialLadders.getSpecialLadder(startLadder.getLocation()) != null) {
                SpecialLadders.getSpecialLadder(startLadder.getLocation()).checkAchievement(player);
            }
            return true;
        }
        switch (option) {
            case "climb-up":
                endLadder = getLadder(startLadder, false);
                break;
            case "climb-down":
                if (startLadder.getName().equals("Trapdoor")) {
                    animation = CLIMB_DOWN;
                }
                endLadder = getLadder(startLadder, true);
                break;
            case "climb":
                Scenery upperLadder = getLadder(startLadder, false);
                Scenery downLadder = getLadder(startLadder, true);
                if (upperLadder == null && downLadder != null) {
                    return climbLadder(player, startLadder, "climb-down");
                }
                if (upperLadder != null && downLadder == null) {
                    return climbLadder(player, startLadder, "climb-up");
                }
                DialoguePlugin dialogue = CLIMB_DIALOGUE.newInstance(player);
                if (dialogue != null && dialogue.open(startLadder)) {
                    player.getDialogueInterpreter().setDialogue(dialogue);
                }
                return false;
        }
        Location destination = endLadder != null ? getDestination(endLadder) : null;
        if (endLadder == null || destination == null) {
            player.getPacketDispatch().sendMessage("The ladder doesn't seem to lead anywhere.");
            return false;
        }
        climb(player, animation, destination);
        return true;
    }

    /**
     * Gets the teleport destination.
     *
     * @param object The object to teleport to.
     * @return The teleport destination.
     */
    public static Location getDestination(Scenery object) {
        int sizeX = object.getDefinition().sizeX;
        int sizeY = object.getDefinition().sizeY;
        if (object.getRotation() % 2 != 0) {
            int switcher = sizeX;
            sizeX = sizeY;
            sizeY = switcher;
        }
        Direction dir = Direction.forWalkFlag(object.getDefinition().getWalkingFlag(), object.getRotation());
        if (dir != null) {
            return getDestination(object, sizeX, sizeY, dir, 0);
        }
        switch (object.getRotation()) {
            case 0:
                return getDestination(object, sizeX, sizeY, Direction.SOUTH, 0);
            case 1:
                return getDestination(object, sizeX, sizeY, Direction.EAST, 0);
            case 2:
                return getDestination(object, sizeX, sizeY, Direction.NORTH, 0);
            case 3:
                return getDestination(object, sizeX, sizeY, Direction.WEST, 0);
        }
        return null;
    }

    /**
     * Gets the destination for the given object.
     *
     * @param object The object.
     * @param dir    The preferred direction from the object.
     * @return The teleporting destination.
     */
    private static Location getDestination(Scenery object, int sizeX, int sizeY, Direction dir, int count) {
        Location loc = object.getLocation();
        if (dir.toInteger() % 2 != 0) {
            int x = dir.getStepX();
            if (x > 0) {
                x *= sizeX;
            }
            for (int y = 0; y < sizeY; y++) {
                Location l = loc.transform(x, y, 0);
                if (RegionManager.isTeleportPermitted(l) && dir.canMove(l)) {
                    return l;
                }
            }
        } else {
            int y = dir.getStepY();
            if (y > 0) {
                y *= sizeY;
            }
            for (int x = 0; x < sizeX; x++) {
                Location l = loc.transform(x, y, 0);
                if (RegionManager.isTeleportPermitted(l) && dir.canMove(l)) {
                    return l;
                }
            }
        }
        if (count == 3) {
            return null;
        }
        return getDestination(object, sizeX, sizeY, Direction.get((dir.toInteger() + 1) % 4), count + 1);
    }

    /**
     * Executes the climbing reward.
     *
     * @param player      The player.
     * @param animation   The climbing animation.
     * @param destination The destination.
     */
    public static void climb(final Player player, Animation animation, final Location destination, final String... messages) {
        player.lock(2);
        player.animate(animation);
		GameWorld.getPulser().submit(new Pulse(1) {
            @Override
            public boolean pulse() {
                player.getProperties().setTeleportLocation(destination);
                for (String message : messages) {
                    player.getPacketDispatch().sendMessage(message);
                }
                return true;
            }
        });
    }

    /**
     * Gets the ladder the object leads to.
     *
     * @param object The ladder object.
     * @param down   If the player is going down a floor.
     * @return The ladder the current ladder object leads to.
     */
    private static Scenery getLadder(Scenery object, boolean down) {
        int mod = down ? -1 : 1;
        Scenery ladder = RegionManager.getObject(object.getLocation().transform(0, 0, mod));
        if (ladder == null || !isLadder(ladder)) {
            if (ladder != null && ladder.getName().equals(object.getName())) {
                ladder = RegionManager.getObject(ladder.getLocation().transform(0, 0, mod));
                if (ladder != null) {
                    return ladder;
                }
            }
            ladder = findLadder(object.getLocation().transform(0, 0, mod));
            if (ladder == null) {
                ladder = RegionManager.getObject(object.getLocation().transform(0, mod * -6400, 0));
                if (ladder == null) {
                    ladder = findLadder(object.getLocation().transform(0, mod * -6400, 0));
                }
            }
        }
        return ladder;
    }

    /**
     * Finds a ladder (by searching a 10x10 area around the given location).
     *
     * @param l The location.
     * @return The ladder.
     */
    private static Scenery findLadder(Location l) {
        for (int x = -5; x < 6; x++) {
            for (int y = -5; y < 6; y++) {
                Scenery object = RegionManager.getObject(l.transform(x, y, 0));
                if (object != null && isLadder(object)) {
                    return object;
                }
            }
        }
        return null;
    }

    /**
     * Checks if the object is a ladder.
     *
     * @param object The object.
     * @return {@code True} if so.
     */
    private static boolean isLadder(Scenery object) {
        for (String option : object.getDefinition().getOptions()) {
            if (option != null && (option.contains("Climb"))) {
                return true;
            }
        }
        return object.getName().equals("Trapdoor");
    }

    /**
     * Represents the dialogue plugin used for climbing stairs or a ladder.
     *
     * @author 'Vexia
     * @version 1.0
     */
    static final class ClimbDialogue extends DialoguePlugin {

        /**
         * Represents the climbing dialogue id.
         */
        public static final int ID = 8 << 16;

        /**
         * Constructs a new {@code ClimbDialogue} {@code Object}.
         */
        public ClimbDialogue() {
            /**
             * empty.
             */
        }

        /**
         * Constructs a new {@code ClimbDialogue} {@code Object}.
         *
         * @param player the player.
         */
        public ClimbDialogue(final Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new ClimbDialogue(player);
        }

        /**
         * Represents the object to use.
         */
        private Scenery object;

        @Override
        public boolean open(Object... args) {
            object = (Scenery) args[0];
            interpreter.sendOptions("What would you like to do?", "Climb Up.", "Climb Down.");
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, int buttonId) {
            switch (stage) {
                case 0:
                    switch (buttonId) {
                        case 1:
                            player.lock(1);
					GameWorld.getPulser().submit(new Pulse(1) {
                                @Override
                                public boolean pulse() {
                                    climbLadder(player, object, "climb-up");
                                    return true;
                                }
                            });
                            end();
                            break;
                        case 2:
                            player.lock(1);
					GameWorld.getPulser().submit(new Pulse(1) {
                                @Override
                                public boolean pulse() {
                                    climbLadder(player, object, "climb-down");
                                    return true;
                                }
                            });
                            end();
                            break;

                    }
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ID};
        }

    }
}
