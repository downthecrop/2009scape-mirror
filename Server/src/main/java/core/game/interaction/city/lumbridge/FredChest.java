package core.game.interaction.city.lumbridge;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
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
        SceneryDefinition.forId(37009).getHandlers().put("option:open",this);
        SceneryDefinition.forId(37010).getHandlers().put("option:shut",this);
        SceneryDefinition.forId(37010).getHandlers().put("option:search",this);
        return this;
    }
    @Override
    public boolean handle(Player player, Node node, String option){
        if(option.equals("open")){
            SceneryBuilder.replace(node.asScenery(),new Scenery(37010,node.asScenery().getLocation(),node.asScenery().getRotation()));
        } else if (option.equals("shut")){
            SceneryBuilder.replace(node.asScenery(),new Scenery(37009,node.asScenery().getLocation(),node.asScenery().getRotation()));
        } else if (option.equals("search")){
            player.getPacketDispatch().sendMessage("You search the chest but find nothing.");
        }
        return true;
    }
}
