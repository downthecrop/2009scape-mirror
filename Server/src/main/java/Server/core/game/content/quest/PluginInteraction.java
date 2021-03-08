package core.game.content.quest;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.Option;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Plugin;

public abstract class PluginInteraction implements Plugin<Object> {
    int[] ids;
    Item item;
    public boolean handle(Player player, Node node){
        return false;
    };
    public boolean handle(Player player, NodeUsageEvent event){
        return false;
    };
    public boolean handle(Player player, NPC npc, Option option){
        return false;
    }
    public boolean handle(Player player, Item item, Option option){return false;}
    public PluginInteraction(int... ids){
        this.ids = ids;
    }
    public PluginInteraction(Item item){this.item = item;}

    public void setIds(int[] ids) {
        this.ids = ids;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
