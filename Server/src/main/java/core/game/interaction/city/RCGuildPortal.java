package core.game.interaction.city;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Portal in wiz tower
 * @author ceik
 */

@Initializable
public class RCGuildPortal extends OptionHandler {
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        SceneryDefinition.forId(38279).getHandlers().put("option:examine",this);
        SceneryDefinition.forId(38279).getHandlers().put("option:enter",this);
        return null;
    }

    @Override
    public boolean handle(Player player, Node node, String string){
        if(string.equals("examine")){
            player.getPacketDispatch().sendMessage("The portal to the runecrafting guild.");
        }else {
            player.getPacketDispatch().sendMessage("Currently under construction.");
        }
        return true;
    }
}
