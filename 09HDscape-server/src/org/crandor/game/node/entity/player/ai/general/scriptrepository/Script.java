package org.crandor.game.node.entity.player.ai.general.scriptrepository;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.ai.general.ScriptAPI;
import org.crandor.game.node.item.Item;

import java.util.ArrayList;

public abstract class Script {
    public ScriptAPI scriptAPI = new ScriptAPI();
    public Player me;
    public ArrayList<Item> inventory = new ArrayList<>();
    public ArrayList<Item> equipment = new ArrayList<>();

    public void setPlayer(Player me)
    {
        this.me = me;
    }

    public abstract void runLoop();
}
