package org.crandor.game.node.entity.player.ai.general.scriptrepository;

import org.crandor.game.node.entity.player.ai.AIPlayer;
import org.crandor.game.node.entity.player.ai.general.ScriptAPI;
import org.crandor.game.node.item.Item;

import java.util.ArrayList;

public abstract class Script {
    public ScriptAPI scriptAPI;
    public ArrayList<Item> inventory = new ArrayList<>();
    public ArrayList<Item> equipment = new ArrayList<>();

    public AIPlayer bot;

    public void init()
    {
        bot.init();
        scriptAPI = new ScriptAPI(bot);

        for (Item i : inventory)
        {
            bot.getInventory().add(i);
        }
        for (Item i : equipment)
        {
            bot.getEquipment().add(i, true, false);
        }
    }

    public abstract void runLoop();
}
