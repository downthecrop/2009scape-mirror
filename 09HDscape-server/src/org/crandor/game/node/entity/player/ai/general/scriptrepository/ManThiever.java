package org.crandor.game.node.entity.player.ai.general.scriptrepository;

import org.crandor.game.node.Node;

public class ManThiever extends Script {

    @Override
    public void runLoop() {
        Node man = scriptAPI.getNearestEntity(me, "Man");

        System.out.println("Current pulse: " + me.getPulseManager().getCurrent());
        System.out.println("Is moving pulse? " + me.getPulseManager().isMovingPulse());
        if (!me.getPulseManager().isMovingPulse())
        {
            man.getInteraction().handle(me, man.getInteraction().get(2));
        }
        /*scriptAPI.walk(me, man);
        new ThievingOptionPlugin().handle(me, man, "pickpocket");*/
    }
}
