package org.crandor.game.node.entity.player.ai.general.scriptrepository;

import org.crandor.game.node.Node;

public class ManThiever extends Script {

    @Override
    public void runLoop() {
        Node man = scriptAPI.getNearestEntity(me, "Man");

        man.getInteraction().handle(me, man.getInteraction().get(2));
    }
}
