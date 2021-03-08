package core.game.interaction.city.lumbridge;

import core.cache.def.impl.ObjectDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.object.GameObject;
import core.game.node.object.ObjectBuilder;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the chest in farmer fred's house
 *
 * @author ceik
 */

@Initializable
public class FredChest extends OptionHandler {
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable{
        ObjectDefinition.forId(37009).getHandlers().put("option:open",this);
        ObjectDefinition.forId(37010).getHandlers().put("option:shut",this);
        ObjectDefinition.forId(37010).getHandlers().put("option:search",this);
        return this;
    }
    @Override
    public boolean handle(Player player, Node node, String option){
        if(option.equals("open")){
            ObjectBuilder.replace(node.asObject(),new GameObject(37010,node.asObject().getLocation(),node.asObject().getRotation()));
        } else if (option.equals("shut")){
            ObjectBuilder.replace(node.asObject(),new GameObject(37009,node.asObject().getLocation(),node.asObject().getRotation()));
        } else if (option.equals("search")){
            player.getPacketDispatch().sendMessage("You search the chest but find nothing.");
        }
        return true;
    }
}
