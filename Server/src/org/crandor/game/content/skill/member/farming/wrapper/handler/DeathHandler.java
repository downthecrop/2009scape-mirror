package org.crandor.game.content.skill.member.farming.wrapper.handler;

import org.crandor.game.content.skill.member.farming.wrapper.PatchCycle;

/**
 * Represents the class used to handle death of a plant.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class DeathHandler {

    /**
     * Represents the patch cycle.
     */
    private final PatchCycle cycle;

    /**
     * Constructs a new {@code DeathHandler} {@code Object}.
     *
     * @param cycle the cycle.
     */
    public DeathHandler(final PatchCycle cycle) {
        this.cycle = cycle;
    }

    /**
     * Method used to handle the death effect.
     */
    public void handle() {
        cycle.setGrowthTime(0L);
        cycle.getDiseaseHandler().removeDisease();
        cycle.getWrapper().addConfigValue(getDeathBase() + ((cycle.getWrapper().getState() - cycle.getNode().getBase())) - 1);
    }

    /**
     * Checks if the patch is dead.
     *
     * @return {@code True} if so.
     */
    public boolean isDead() {
        if (cycle.getNode() == null) {
            return false;
        }
        int state = cycle.getState();
        for (int i = 1; i < cycle.getNode().getGrowthCycles(); i++) {
            if (state == (getDeathBase() + i) - 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the death base config.
     *
     * @return the base.
     */
    public int getDeathBase() {
        int val = cycle.getWrapper().getNode().getBase() + cycle.getNode().getDeathBase();
        return (val);
    }
}
