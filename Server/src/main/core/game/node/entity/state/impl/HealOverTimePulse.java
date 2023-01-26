package core.game.node.entity.state.impl;

import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.node.entity.state.StatePulse;
import core.game.world.update.flag.context.Graphics;

import java.nio.ByteBuffer;

/**
 * Method for healing a player a certain amount of HP
 * every X amount of ticks for a total of X amount of heals
 * over X amount of ticks.
 * 
 * @author phil lips
 */

public class HealOverTimePulse extends StatePulse {

    /**The total amount of HP to heal*/

    private int totalToHeal;

    /**How many ticks to spread the heals over*/

    private int ticksTotal;

    /**How many times to heal the player during those ticks*/

    private int timesToHeal;

    private int currentTick;

    /**
     * The entity.
     */
    protected final Entity entity;

    /**Constructs a new HealOverTimePulse object
     * @param entity the entity to heal over time
     * @param ticks the total time to spread the heal over
     * @param totalHeal the total amount to heal for
     * @param healInc how many times the total amount to heal is divided to*/

    public HealOverTimePulse(Entity entity, int ticks, int totalHeal, int healInc, int currentTick){
        super(entity,0);
        this.entity = entity;
        this.ticksTotal = ticks;
        this.totalToHeal = totalHeal;
        this.timesToHeal = healInc;
        this.currentTick = currentTick;
    }

    @Override
    public StatePulse create(Entity entity, Object... args) {
        return new HealOverTimePulse(entity, (Integer) args[1],(Integer) args[2],(Integer) args[3],1);
    }

    /** Checks if it can heal the player every pulse
     * the mod is funny math haha :)
     */
    @Override
    public boolean pulse() {
        if(currentTick != 0){
            if(currentTick % (ticksTotal / timesToHeal) == 0){
                entity.getSkills().heal(totalToHeal / timesToHeal);
            }
        }
        currentTick += 1;
        return currentTick > ticksTotal;
    }

    @Override
    public boolean isSaveRequired() {
        return true;
    }

    @Override
    public void save(ByteBuffer buffer) {
    }

    @Override
    public StatePulse parse(Entity entity, ByteBuffer buffer) {
        return null;
    }
}
