package plugin.interaction.city.lumbridge;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.node.object.ObjectBuilder;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles the chest in farmer fred's house
 *
 * @author ceik
 */

@InitializablePlugin
public class FredChest extends OptionHandler {
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable{
        ObjectDefinition.forId(37009).getConfigurations().put("option:open",this);
        ObjectDefinition.forId(37010).getConfigurations().put("option:shut",this);
        ObjectDefinition.forId(37010).getConfigurations().put("option:search",this);
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
