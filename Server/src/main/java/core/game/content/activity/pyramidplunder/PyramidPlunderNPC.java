package core.game.content.activity.pyramidplunder;

import core.game.interaction.MovementPulse;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.map.path.Pathfinder;
import core.plugin.Plugin;

/**
 * Handles a Pyramid Plunder enemy, adapted from AntiMacroNPC.java by ceikry
 * @author Vexia
 */
public abstract class PyramidPlunderNPC extends AbstractNPC {

    /**
     * The player.
     */
    protected final Player player;

    /**
     * The quotes the npc will say(null if none)
     */
    private String[] quotes;

    /**
     * The counter representing speech cycles.
     */
    private int count;

    /**
     * The time until the next speech.
     */
    private int nextSpeech;

    /**
     * If the players time is up.
     */
    protected boolean timeUp;

    /**
     * The end time.
     */
    private int endTime;

    /**
     * Constructs a new {@code AntiMacroNPC} {@code Object}.
     * @param id the id.
     * @param location the location.
     * @param player the player.
     */
    public PyramidPlunderNPC(int id, Location location, Player player) {
        super(id, location);
        this.player = player;
        this.endTime = (int) (GameWorld.getTicks() + (1000 / 0.6));
    }

    @Override
    public void init() {
        location = RegionManager.getSpawnLocation(player, this);
        if (location == null) {
            clear();
            return;
        }
        super.init();
        startFollowing();
    }

    @Override
    public void handleTickActions() {
        if (GameWorld.getTicks() > endTime) {
            clear();
        }
        if (!getLocks().isMovementLocked()) {
            if (dialoguePlayer == null || !dialoguePlayer.isActive() || !dialoguePlayer.getInterfaceManager().hasChatbox()) {
                dialoguePlayer = null;
            }
        }
        if (!player.isActive() || !getLocation().withinDistance(player.getLocation(), 10)) {
            handlePlayerLeave();
        }
        if (!getPulseManager().hasPulseRunning()) {
            startFollowing();
        }
        if (quotes != null) {
            if (nextSpeech < GameWorld.getTicks() && this.getDialoguePlayer() == null && !this.getLocks().isMovementLocked()) {
                if (count > quotes.length - 1) {
                    return;
                }
                nextSpeech = (int) (GameWorld.getTicks() + (20 / 0.5));
                if (++count >= quotes.length) {
                    setTimeUp(true);
                    handleTimeUp();
                    return;
                }
            }
        }
    }

    /**
     * Called when the player is gone.
     */
    public void handlePlayerLeave() {
        clear();
    }

    /**
     * Called when the quotes are finished.
     */
    public void handleTimeUp() {
    }

    @Override
    public boolean isAttackable(Entity entity, CombatStyle style, boolean message) {
        if (entity instanceof Player && entity != player) {
            ((Player) entity).getPacketDispatch().sendMessage("It's not after you.");
            return false;
        }
        return super.isAttackable(entity, style, message);
    }

    @Override
    public void onRegionInactivity() {
        super.onRegionInactivity();
        clear();
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return null;
    }



    /**
     * Starts following the player.
     */
    public void startFollowing() {
        getPulseManager().run(new MovementPulse(this, player, Pathfinder.DUMB) {
            @Override
            public boolean pulse() {
                return false;
            }
        }, "movement");
        face(player);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        return super.newInstance(arg);
    }

    /**
     * Gets the player.
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }


    /**
     * Gets the timeUp.
     * @return The timeUp.
     */
    public boolean isTimeUp() {
        return timeUp;
    }

    /**
     * Sets the timeUp.
     * @param timeUp The timeUp to set.
     */
    public void setTimeUp(boolean timeUp) {
        this.timeUp = timeUp;
    }

}
