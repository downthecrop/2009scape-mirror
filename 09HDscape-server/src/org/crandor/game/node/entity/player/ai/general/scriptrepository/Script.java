package org.crandor.game.node.entity.player.ai.general.scriptrepository;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.ai.general.ScriptAPI;

public abstract class Script {
    public Player me;
    public ScriptAPI scriptAPI = new ScriptAPI();

    public void setPlayer(Player me)
    {
        this.me = me;
    }

    public abstract void runLoop();
}
