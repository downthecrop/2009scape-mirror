package org.crandor.game.node.entity.player.ai.general.scriptrepository;

import org.crandor.game.node.entity.player.Player;
import plugin.skill.thieving.ThievingOptionPlugin;

public class ManThiever extends Script {

    @Override
    public void runLoop() {
        new ThievingOptionPlugin().handle(me, scriptAPI.getNearestNPC(me.getLocation(), "Man"), "pickpocket");
    }
}
